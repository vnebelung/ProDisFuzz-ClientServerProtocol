/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:37.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.message.client.IncomingMessage;
import net.prodisfuzz.csp.message.client.OutgoingMessage;
import net.prodisfuzz.csp.packet.ProtocolFormatException;
import net.prodisfuzz.csp.packet.client.PacketParser;
import net.prodisfuzz.csp.protocol.ProtocolExecutionException;
import net.prodisfuzz.csp.protocol.ProtocolStateException;
import net.prodisfuzz.csp.protocol.StateMachine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the protocol used to communicate from a client to a server. It provides
 * functionality to send and receive messages.
 */
public class Client {

    private OutputStream outputStream;
    private PacketParser packetParser;
    private StateMachine stateMachine;

    /**
     * Constructs a new client behavior component responsible for communicating with the server component.
     *
     * @param inputStream  the client socket's input stream
     * @param outputStream the client socket's output stream
     */
    public Client(InputStream inputStream, OutputStream outputStream) {
        this.outputStream = outputStream;
        stateMachine = new StateMachine();
        packetParser = new PacketParser(inputStream);
    }

    /**
     * Sends a given message to the server and returns its response.
     *
     * @param message the message to be sent to the server
     * @return the server's response
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolStateException     if the outgoing command is not allowed at the current protocol state or if the
     *                                    incoming message is not in a valid format
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    private byte[] sendReceive(OutgoingMessage message) throws IOException, ProtocolStateException,
            ProtocolFormatException, ProtocolExecutionException {
        if (!stateMachine.isAllowedAtCurrentState(message.getCommand())) {
            throw new ProtocolStateException(
                    "Command '" + message.getCommand() + "' is not allowed at the current protocol state");
        }
        outputStream.write(message.getBytes());
        IncomingMessage incomingMessage = packetParser.readIncomingMessage();
        if (incomingMessage.getCommand() == StateMachine.ServerAnswerCommand.ERR) {
            throw new ProtocolExecutionException(new String(incomingMessage.getBody(), StandardCharsets.UTF_8));
        }
        stateMachine.updateWith(message.getCommand());
        return incomingMessage.getBody();
    }

    /**
     * Sends an "are you there" message that checks whether the server is reachable. When the server is
     * receiving this message, it must respond with its version number.
     *
     * @return the version parameter sent back from the server
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public Map<String, String> ayt() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT);
        byte[] body = sendReceive(outgoingMessage);
        Map<String, String> parameters = parseBody(body);
        if (!parameters.containsKey("version")) {
            return Collections.emptyMap();
        }
        return Collections.singletonMap("version", parameters.get("version"));
    }

    /**
     * Parses the parameters in the given body as comma-separated key/value pairs. If a key/value pair string contains
     * more than one '=' sign, the value of the key will be an empty string.
     *
     * @param body the message's body
     * @return the key/value pairs
     */
    private Map<String, String> parseBody(byte[] body) {
        String[] keyValuePairs = new String(body, StandardCharsets.UTF_8).split(",");
        Map<String, String> result = new HashMap<>(keyValuePairs.length);
        for (String each : keyValuePairs) {
            String[] keyValue = each.split("=");
            result.put(keyValue[0], keyValue.length == 2 ? keyValue[1] : "");
        }
        return result;
    }

    /**
     * Sends a "get connectors" message that receives all connectors the server has to offer. When the server is
     * receiving this message, it must respond with a list of connector types.
     *
     * @return the connectors the server has available
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public Set<String> gco() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.GCO);
        byte[] body = sendReceive(outgoingMessage);
        Map<String, String> parameters = parseBody(body);
        Set<String> result = new HashSet<>(parameters.size());
        result.addAll(parameters.entrySet().stream().filter(each -> each.getKey().matches("connector[0-9]+"))
                .map(Map.Entry::getValue).collect(Collectors.toList()));
        return result;
    }

    /**
     * Sends a "set connector parameters" message that sets parameters of the server's chosen connector.
     *
     * @param connectorParameters the parameters to be sent
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public void scp(Map<String, String> connectorParameters) throws IOException, ProtocolStateException,
            ProtocolFormatException, ProtocolExecutionException {
        OutgoingMessage outgoingMessage =
                new OutgoingMessage(StateMachine.ClientRequestCommand.SCP, connectorParameters);
        sendReceive(outgoingMessage);
    }

    /**
     * Sends a "set connector" message that signals the server to use the given connector to connect to the target.
     *
     * @param connector the connector type
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public void sco(String connector) throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.SCO, connector);
        sendReceive(outgoingMessage);
    }

    /**
     * Sends a "call target for testing" message that tells the server to call the target without any data to verify
     * that calling the target can be done successfully.
     *
     * @return the parameters sent back from the server
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public Map<String, String> ctt() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.CTT);
        byte[] body = sendReceive(outgoingMessage);
        return parseBody(body);
    }

    /**
     * Sends a "set watcher" message that signals the server to use the given watcher to detect crashes of the target.
     *
     * @param watcher the watcher type
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public void swa(String watcher) throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.SWA, watcher);
        sendReceive(outgoingMessage);
    }

    /**
     * Sends a "get watchers" message that receives all watchers the server has to offer. When the server is receiving
     * this message, it must respond with a list of watcher types.
     *
     * @return the watchers the server has available for its previously set connector
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public Set<String> gwa() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.GWA);
        byte[] body = sendReceive(outgoingMessage);
        Map<String, String> parameters = parseBody(body);
        Set<String> result = new HashSet<>(parameters.size());
        result.addAll(parameters.entrySet().stream().filter(each -> each.getKey().matches("watcher[0-9]+"))
                .map(Map.Entry::getValue).collect(Collectors.toList()));
        return result;
    }

    /**
     * Sends a "call target with fuzz data" message that tells the server to call the target with the given fuzzed
     * data. The server responds whether the target has crashed when executing the data.
     *
     * @param data the fuzzed data
     * @return the parameters sent back from the server
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public Map<String, String> ctf(byte... data) throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.CTF, data);
        byte[] body = sendReceive(outgoingMessage);
        return parseBody(body);
    }

    /**
     * Sends a "reset" message that resets the protocol to its default state.
     *
     * @throws IOException                if an I/O error occurs
     * @throws ProtocolFormatException    if the received package is not of the format "CCC L… B…", CCC = three
     *                                    character command, L… = length of body, B… = body with length L…
     * @throws ProtocolStateException     if the command is not allowed at the current protocol state
     * @throws ProtocolExecutionException if the server answered with an error message
     */
    public void rst() throws IOException, ProtocolStateException, ProtocolFormatException, ProtocolExecutionException {
        OutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.RST);
        sendReceive(outgoingMessage);
    }

}
