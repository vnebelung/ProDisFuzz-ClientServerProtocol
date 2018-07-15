/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:57.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.packet.server;

import net.prodisfuzz.csp.internal.message.server.IncomingMessage;
import net.prodisfuzz.csp.internal.packet.AbstractPacketParser;
import net.prodisfuzz.csp.internal.packet.ProtocolFormatException;
import net.prodisfuzz.csp.internal.protocol.StateMachine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a not yet parsed packet that is received by a server.
 */
public class PacketParser extends AbstractPacketParser<IncomingMessage, StateMachine.ClientRequestCommand> {

    private static final Map<String, StateMachine.ClientRequestCommand> CLIENT_COMMANDS =
            new HashMap<>(StateMachine.ClientRequestCommand.values().length);

    static {
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            CLIENT_COMMANDS.put(each.name(), each);
        }
    }

    /**
     * Constructs a new packet parser with a given input stream.
     *
     * @param inputStream the input stream
     */
    public PacketParser(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * Reads an incoming message from a client input stream and tries to parse the packet and returns it. A
     * valid packet structure is as follows: "CCC L… B…", CCC = three character command, L… = length of body, B… =
     * body with length L…
     *
     * @return the parsed incoming message
     * @throws IOException             if an I/O error occurs
     * @throws ProtocolFormatException if the received package is not of the format "CCC L… B…", CCC = three character
     *                                 command, L… = length of body, B… = body with length L…
     */
    public IncomingMessage readIncomingMessage() throws IOException, ProtocolFormatException {
        IncomingMessage incomingMessage =
                super.readIncomingMessage(IncomingMessage.class, StateMachine.ClientRequestCommand.class,
                        CLIENT_COMMANDS);
        if (!new String(incomingMessage.getBody(), StandardCharsets.UTF_8)
                .matches("([a-zA-Z0-9]+=[a-zA-Z0-9]+(,[a-zA-Z0-9]+=[a-zA-Z0-9]+)*)?")) {
            throw new ProtocolFormatException("Received body has not a valid key=value format");
        }
        return incomingMessage;
    }
}
