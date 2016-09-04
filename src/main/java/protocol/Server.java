/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:45 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import message.server.OutgoingMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the protocol used to communicate from a server to a client. It provides
 * functionality to send and receive messages.
 */
public class Server {

    private static final Map<String, StateMachine.ClientRequestCommand> COMMANDS = new HashMap<>(9);

    static {
        COMMANDS.put("AYT", StateMachine.ClientRequestCommand.AYT);
        COMMANDS.put("SCO", StateMachine.ClientRequestCommand.SCO);
        COMMANDS.put("SWA", StateMachine.ClientRequestCommand.SWA);
        COMMANDS.put("GWA", StateMachine.ClientRequestCommand.GWA);
        COMMANDS.put("SCP", StateMachine.ClientRequestCommand.SCP);
        COMMANDS.put("RST", StateMachine.ClientRequestCommand.RST);
        COMMANDS.put("CTT", StateMachine.ClientRequestCommand.CTT);
        COMMANDS.put("CTF", StateMachine.ClientRequestCommand.CTF);
        COMMANDS.put("GCO", StateMachine.ClientRequestCommand.GCO);
    }

    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private StateMachine stateMachine;

    /**
     * Constructs a new monitor protocol responsible for communicating with the remote monitor component.
     *
     * @param inputStream  the monitor socket's input stream
     * @param outputStream the monitor socket's output stream
     */
    public Server(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        stateMachine = new StateMachine();
    }

    /**
     * Sends an "return ok" answer with a number as body. It means that the monitor signals that the preceding request
     * was correctly received and parsed.
     *
     * @param command the command of the request that this answer is the answer to
     * @param body    the body
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public void rok(StateMachine.ClientRequestCommand command, int body) throws IOException, ProtocolStateException {
        stateMachine.updateWith(command);
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ROK, body);
        send(message);
    }

    /**
     * Sends a given message to the main component.
     *
     * @param message the message to be sent
     * @throws IOException if an I/O error occurs
     */
    private void send(OutgoingMessage message) throws IOException {
        // Inspection suppressed because the two streams can used multiple times by calling this method. It is the
        // Monitor class' responsibility to close the socket correctly

        //noinspection IOResourceOpenedButNotSafelyClosed,resource
        DataOutputStream out = new DataOutputStream(outputStream);
        out.write(message.getBytes());
        out.flush();
    }

    /**
     * Receives a message of the input stream and returns it.
     *
     * @return the received message or null, if the received request has an invalid command and cannot be processed
     * @throws IOException if an I/O error occurs
     */
    public message.server.IncomingMessage receive() throws IOException {
        StateMachine.ClientRequestCommand command;
        try {
            command = readCommand();
        } catch (ProtocolStateException e) {
            err("Protocol error: " + e.getMessage());
            //noinspection ReturnOfNull
            return null;
        }
        int length = readLength();
        byte[] body = readBody(length);
        if (!stateMachine.isAllowedAtCurrentState(command)) {
            err("Command '" + command + "' is not allowed at the current protocol state");
            //noinspection ReturnOfNull
            return null;
        }
        return new message.server.IncomingMessage(command, body);
    }

    /**
     * Sends a "return ok" message. It means that the monitor signals that the preceding request was correctly received
     * and parsed.
     *
     * @param command the command of the request that this answer is the answer to
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public void rok(StateMachine.ClientRequestCommand command) throws IOException, ProtocolStateException {
        stateMachine.updateWith(command);
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ROK);
        send(message);
    }

    /**
     * Sends a "return ok" message. It means that the monitor signals that the preceding request was correctly received
     * and parsed.
     *
     * @param command the command of the request that this answer is the answer to
     * @param body    the message's body
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     * @throws IOException            if an I/O error occurs
     */
    public void rok(StateMachine.ClientRequestCommand command, Map<String, String> body) throws IOException,
            ProtocolStateException {
        stateMachine.updateWith(command);
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ROK, body);
        send(message);
    }

    /**
     * Sends an error response to the main component. This error indicates a failure in parsing a request from the main
     * component.
     *
     * @param cause the cause of the error
     * @throws IOException if an I/O error occurs
     */
    public void err(String cause) throws IOException {
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ERR, cause);
        send(message);
    }

    /**
     * Reads the Packet's three characters command in the input data stream. Packet structure: "CCC L… B…", CCC = three
     * character command, L… = length of body, B… = body with length L…
     *
     * @return the packet's command
     * @throws ProtocolStateException if the received command is invalid
     * @throws IOException            if an I/O error occurs
     */
    private StateMachine.ClientRequestCommand readCommand() throws IOException, ProtocolStateException {
        StringBuilder stringBuilder = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            stringBuilder.append((char) inputStream.readByte());
        }
        String command = stringBuilder.toString();
        if (!COMMANDS.containsKey(command)) {
            throw new ProtocolStateException("Received command '" + command + "' is invalid");
        } else {
            return COMMANDS.get(command);
        }
    }

    /**
     * Reads the length of the packet's body in the input stream. Packet structure: "CCC L… B…", CCC = three character
     * command, L… = length of body, B… = body with length L…
     *
     * @return the length of the packet's body
     * @throws IOException if an I/O error occurs
     */
    private int readLength() throws IOException {
        inputStream.readByte();
        StringBuilder length = new StringBuilder();
        while (true) {
            char c = (char) inputStream.readByte();
            if (c == ' ') {
                break;
            } else {
                length.append(c);
            }
        }
        return Integer.parseInt(length.toString());
    }

    /**
     * Reads packet's body in the input stream. Packet structure: "CCC L… B…", CCC = three character command, L… =
     * length of body, B… = body with length L…
     *
     * @return the packet's body
     * @throws IOException if an I/O error occurs
     */
    private byte[] readBody(int length) throws IOException {
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = inputStream.readByte();
        }
        return result;
    }

}
