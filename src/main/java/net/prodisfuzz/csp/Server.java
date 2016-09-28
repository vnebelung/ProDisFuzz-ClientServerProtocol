/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:37.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.message.server.IncomingMessage;
import net.prodisfuzz.csp.message.server.OutgoingMessage;
import net.prodisfuzz.csp.packet.ProtocolFormatException;
import net.prodisfuzz.csp.packet.server.PacketParser;
import net.prodisfuzz.csp.protocol.ProtocolStateException;
import net.prodisfuzz.csp.protocol.StateMachine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * This class represents the protocol used to communicate from a server to a client. It provides
 * functionality to send and receive messages.
 */
public class Server {

    private OutputStream outputStream;
    private PacketParser packetParser;
    private StateMachine stateMachine;
    private StateMachine.ClientRequestCommand lastValidRequest;

    /**
     * Constructs a new server behavior component responsible for communicating with the client component.
     *
     * @param inputStream  the server socket's input stream
     * @param outputStream the server socket's output stream
     */
    public Server(InputStream inputStream, OutputStream outputStream) {
        this.outputStream = outputStream;
        stateMachine = new StateMachine();
        packetParser = new PacketParser(inputStream);
    }

    /**
     * Sends a given message to the client.
     *
     * @param message the message to be sent
     * @throws IOException if an I/O error occurs
     */
    private void send(OutgoingMessage message) throws IOException {
        // Inspection suppressed because the two streams can used multiple times by calling this method. It is the
        // server class' responsibility to close the socket correctly

        //noinspection IOResourceOpenedButNotSafelyClosed,resource
        DataOutputStream out = new DataOutputStream(outputStream);
        out.write(message.getBytes());
        if (message.getCommand() == StateMachine.ServerAnswerCommand.ROK) {
            lastValidRequest = null;
        }
    }

    /**
     * Receives a message of the input stream and returns it.
     *
     * @return the received message or null, if the received request has an invalid command and cannot be processed
     * @throws IOException if an I/O error occurs
     */
    public IncomingMessage receive() throws IOException {
        IncomingMessage incomingMessage;

        try {
            incomingMessage = packetParser.readIncomingMessage();
        } catch (ProtocolFormatException e) {
            err(e.getMessage());
            //noinspection ReturnOfNull
            return null;
        }
        if (!stateMachine.isAllowedAtCurrentState(incomingMessage.getCommand())) {
            err("Protocol state error: Command '" + incomingMessage.getCommand() + "' is not allowed at the " +
                    "current protocol state");
            //noinspection ReturnOfNull
            return null;
        }
        lastValidRequest = incomingMessage.getCommand();
        return incomingMessage;
    }

    /**
     * Sends a "return ok" message. It means that the server signals that the preceding request was correctly received
     * and parsed.
     *
     * @param body the message's body
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     * @throws IOException            if an I/O error occurs
     */
    public void rok(Map<String, String> body) throws IOException, ProtocolStateException {
        stateMachine.updateWith(lastValidRequest);
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ROK, body);
        send(message);
    }

    /**
     * Sends an error response to the client. This error indicates a failure in parsing a request from the client.
     *
     * @param cause the cause of the error
     * @throws IOException if an I/O error occurs
     */
    public void err(String cause) throws IOException {
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ERR, cause);
        send(message);
    }
}
