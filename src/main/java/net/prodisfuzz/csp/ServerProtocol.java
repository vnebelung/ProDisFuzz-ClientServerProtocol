/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:57.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.internal.message.server.IncomingMessage;
import net.prodisfuzz.csp.internal.message.server.OutgoingMessage;
import net.prodisfuzz.csp.internal.packet.ProtocolFormatException;
import net.prodisfuzz.csp.internal.packet.server.PacketParser;
import net.prodisfuzz.csp.internal.protocol.ProtocolStateException;
import net.prodisfuzz.csp.internal.protocol.StateMachine;
import net.prodisfuzz.csp.internal.util.Hex;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the protocol used to communicate from a server to a client. It provides functionality to send
 * and receive messages.
 */
@SuppressWarnings("WeakerAccess")
public class ServerProtocol {

    private OutputStream outputStream;
    private PacketParser packetParser;
    private StateMachine stateMachine;
    private StateMachine.ClientRequestCommand lastValidRequest;

    private IAytListener aytListener;
    private IGcoListener gcoListener;
    private IScoListener scoListener;
    private IScpListener scpListener;
    private ICttListener cttListener;
    private IGwaListener gwaListener;
    private ISwaListener swaListener;
    private ICtfListener ctfListener;
    private IRstListener rstListener;

    /**
     * Constructs a new server behavior component responsible for communicating with the client component.
     *
     * @param inputStream  the server socket's input stream
     * @param outputStream the server socket's output stream
     */
    public ServerProtocol(InputStream inputStream, OutputStream outputStream, IAytListener aytListener,
                          IGcoListener gcoListener, IScoListener scoListener, IScpListener scpListener,
                          ICttListener cttListener, IGwaListener gwaListener, ISwaListener swaListener,
                          ICtfListener ctfListener, IRstListener rstListener) {
        this.outputStream = outputStream;
        this.aytListener = aytListener;
        this.gcoListener = gcoListener;
        this.scoListener = scoListener;
        this.scpListener = scpListener;
        this.cttListener = cttListener;
        this.gwaListener = gwaListener;
        this.swaListener = swaListener;
        this.ctfListener = ctfListener;
        this.rstListener = rstListener;
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
     * Receives a message of the input stream and returns it to the particular listener that was registered in the
     * constructor.
     *
     * @throws IOException if an I/O error occurs
     */
    public void receive() throws IOException {
        IncomingMessage incomingMessage;

        try {
            incomingMessage = packetParser.readIncomingMessage();
        } catch (ProtocolFormatException e) {
            err(e.getMessage());
            return;
        }
        if (!stateMachine.isAllowedAtCurrentState(incomingMessage.getCommand())) {
            err("Protocol state error: Command '" + incomingMessage.getCommand() + "' is not allowed at the " +
                    "current protocol state");
            //noinspection ReturnOfNull
            return;
        }
        lastValidRequest = incomingMessage.getCommand();

        Map<String, String> body = parseBody(incomingMessage.getBody());
        try {
            switch (incomingMessage.getCommand()) {
                case AYT:
                    int version = aytListener.receive();
                    rok(Collections.singletonMap("version", String.valueOf(version)));
                    break;
                case RST:
                    rstListener.receive();
                    rok();
                    break;
                case GCO:
                    Set<String> connectors = gcoListener.receive();
                    Map<String, String> gcoParameters = new HashMap<>(connectors.size());
                    int i = 0;
                    for (String connector : connectors) {
                        gcoParameters.put("connector" + i, connector);
                        i++;
                    }
                    rok(gcoParameters);
                    break;
                case SCO:
                    scoListener.receive(body.getOrDefault("connector", ""));
                    rok();
                    break;
                case SCP:
                    scpListener.receive(parseBody(incomingMessage.getBody()));
                    rok();
                    break;
                case CTT:
                    Map<String, String> cttResults =
                            cttListener.receive(Hex.hexBin2Byte(body.getOrDefault("data", "")));
                    rok(cttResults);
                    break;
                case GWA:
                    Set<String> watchers = gwaListener.receive();
                    Map<String, String> gwaParameters = new HashMap<>(watchers.size());
                    int j = 0;
                    for (String watcher : watchers) {
                        gwaParameters.put("watcher" + j, watcher);
                        j++;
                    }
                    rok(gwaParameters);
                    break;
                case SWA:
                    swaListener.receive(body.getOrDefault("watcher", ""));
                    rok();
                    break;
                case CTF:
                    Map<String, String> fuzzingResults =
                            ctfListener.receive(Hex.hexBin2Byte(body.getOrDefault("data", "")));
                    rok(fuzzingResults);
                    break;
            }
        } catch (ProtocolExecutionException e) {
            err(e.getMessage());
        } catch (ProtocolStateException ignored) {
            // Irrelevant for the server because the command was checked before
        }
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
     * Sends a "return ok" message. It means that the server signals that the preceding request was correctly received
     * and parsed.
     *
     * @param body the message's body
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     * @throws IOException            if an I/O error occurs
     */
    private void rok(Map<String, String> body) throws IOException, ProtocolStateException {
        stateMachine.updateWith(lastValidRequest);
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ROK, body);
        send(message);
    }

    /**
     * Sends a "return ok" message. It means that the server signals that the preceding request was correctly received
     * and parsed.
     *
     * @throws ProtocolStateException if the command is not allowed at the current protocol state
     * @throws IOException            if an I/O error occurs
     */
    private void rok() throws IOException, ProtocolStateException {
        rok(Collections.emptyMap());
    }

    /**
     * Sends an error response to the client. This error indicates a failure in parsing a request from the client.
     *
     * @param cause the cause of the error
     * @throws IOException if an I/O error occurs
     */
    private void err(String cause) throws IOException {
        OutgoingMessage message = new OutgoingMessage(StateMachine.ServerAnswerCommand.ERR, cause);
        send(message);
    }
}
