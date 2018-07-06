/*
 * This file is part of ProDisFuzz, modified on 06.07.18 15:01.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.packet.client;

import net.prodisfuzz.csp.message.client.IncomingMessage;
import net.prodisfuzz.csp.packet.AbstractPacketParser;
import net.prodisfuzz.csp.packet.ProtocolFormatException;
import net.prodisfuzz.csp.protocol.StateMachine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a not yet parsed packet that is received by a client.
 */
public class PacketParser extends AbstractPacketParser<IncomingMessage, StateMachine.ServerAnswerCommand> {

    private static final Map<String, StateMachine.ServerAnswerCommand> SERVER_COMMANDS =
            new HashMap<>(StateMachine.ServerAnswerCommand.values().length);

    static {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            SERVER_COMMANDS.put(each.name(), each);
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
     * Reads an incoming message from a server input stream and tries to parse the packet and returns it. A
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
                super.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                        SERVER_COMMANDS);
        if (incomingMessage.getCommand() == StateMachine.ServerAnswerCommand.ROK &&
                !new String(incomingMessage.getBody(), StandardCharsets.UTF_8)
                        .matches("([a-z]+[0-9]*=[^=,]*(,[a-z]+[0-9]*=[^=,]*)*)?")) {
            throw new ProtocolFormatException("Received body has not a valid key=value format");
        }
        return incomingMessage;
    }
}
