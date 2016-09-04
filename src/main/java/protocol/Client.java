/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:42 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import message.client.IncomingMessage;
import message.client.OutgoingMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * This class represents the protocol used to communicate from a client to a server. It provides
 * functionality to send and receive messages.
 */
public class Client {

    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private StateMachine stateMachine;

    /**
     * Constructs a new monitor protocol responsible for communicating with the server component.
     *
     * @param inputStream  the monitor socket's input stream
     * @param outputStream the monitor socket's output stream
     */
    public Client(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        stateMachine = new StateMachine();
    }

    /**
     * Sends a given message to the server and returns its response.
     *
     * @param message the message to be sent to the monitor
     * @return the server's response
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    private IncomingMessage sendReceive(OutgoingMessage message) throws IOException, ProtocolStateException {
        if (!stateMachine.isAllowedAtCurrentState(message.getCommand())) {
            throw new ProtocolStateException("Protocol error: Command '" + message.getCommand() + "' is not allowed " +
                    "at the current protocol state");
        }
        outputStream.write(message.getBytes());
        outputStream.flush();
        StateMachine.ServerAnswerCommand command = readCommand();
        int length = readLength();
        byte[] body = readBody(length);
        if (command == StateMachine.ServerAnswerCommand.ROK) {
            stateMachine.updateWith(message.getCommand());
        }
        return new IncomingMessage(command, body);
    }

    /**
     * Reads the packet's three characters command in the input data stream. Packet structure: "CCC L… B…", CCC = three
     * character command, L… = length of body, B… = body with length L…
     *
     * @return the packet's command
     * @throws ProtocolStateException if the received command is invalid
     * @throws IOException            if an I/O error occurs
     */
    private StateMachine.ServerAnswerCommand readCommand() throws IOException, ProtocolStateException {
        StringBuilder stringBuilder = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            stringBuilder.append((char) inputStream.readByte());
        }
        String command = stringBuilder.toString();
        switch (command) {
            //noinspection HardCodedStringLiteral
            case "ROK":
                return StateMachine.ServerAnswerCommand.ROK;
            //noinspection HardCodedStringLiteral
            case "ERR":
                return StateMachine.ServerAnswerCommand.ERR;
            default:
                throw new ProtocolStateException("Protocol error: Received command '" + command + "' is invalid");
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

    /**
     * Sends an "are you there" message that checks whether the server is reachable. When the monitor is
     * receiving this message, it must respond with its version number.
     *
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage ayt() throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "get connectors" message that receives all connectors the monitor has to offer. When the monitor is
     * receiving this message, it must respond with a list of connector types.
     *
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage gco() throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.GCO);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "set connector parameters" message that sets parameters of the monitor's chosen connector.
     *
     * @param parameters the parameters to be sent
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage scp(Map<String, String> parameters) throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.SCP, parameters);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "set connector" message that signals the monitor to use the given connector to connect to the target.
     *
     * @param connector the connector type
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage sco(String connector) throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.SCO, connector);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "call target for testing" message that tells the monitor to call the target without any data to verify
     * that calling the target can be done successfully.
     *
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage ctt() throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.CTT);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "set watcher" message that signals the monitor to use the given watcher to detect crashes of the target.
     *
     * @param connector the watcher type
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage swa(String connector) throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.SWA, connector);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "get watchers" message that receives all watchers the monitor has to offer. When the monitor is receiving
     * this message, it must respond with a list of watcher types.
     *
     * @param connector the watcher type
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage gwa(String connector) throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.GWA, connector);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "call target with fuzz data" message that tells the monitor to call the target with the given fuzzed
     * data. The monitor responds whether the target has crashed when executing the data.
     *
     * @param data the fuzzed data
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage ctf(byte... data) throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.CTF, data);
        return sendReceive(transmitMessage);
    }

    /**
     * Sends a "reset" message that resets the protocol to its default state.
     *
     * @return the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     */
    public IncomingMessage rst() throws IOException, ProtocolStateException {
        OutgoingMessage transmitMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.RST);
        return sendReceive(transmitMessage);
    }

}
