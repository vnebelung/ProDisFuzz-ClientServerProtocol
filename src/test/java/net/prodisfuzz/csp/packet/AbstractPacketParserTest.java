/*
 * This file is part of ProDisFuzz, modified on 06.07.18 15:01.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.packet;

import net.prodisfuzz.csp.message.client.IncomingMessage;
import net.prodisfuzz.csp.protocol.StateMachine;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class AbstractPacketParserTest {

    private StreamSimulator streamSimulator;

    @BeforeClass
    public void setUp() throws IOException {
        streamSimulator = new StreamSimulator();
    }

    @AfterClass
    public void tearDown() throws IOException {
        streamSimulator.exit();
    }

    @Test
    public void testReadIncomingMessage() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 4 test");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        IncomingMessage incomingMessage = packetParser
                .readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                        Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
        Assert.assertEquals(incomingMessage.getCommand(), StateMachine.ServerAnswerCommand.ROK);
        Assert.assertEquals(incomingMessage.getBody(), "test".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK test");
        try {
            packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                    Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
            Assert.fail();
        } catch (ProtocolFormatException ignored) {
        }

        streamSimulator.writeForInputStream("RO");
        try {
            packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                    Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
            Assert.fail();
        } catch (ProtocolFormatException e) {
            Assert.assertEquals(e.getMessage(), "Protocol format error: Received command has not the structure 'CCC '");
        }
    }

    private class PacketParser extends AbstractPacketParser<IncomingMessage, StateMachine.ServerAnswerCommand> {

        PacketParser(InputStream inputStream) {
            super(inputStream);
        }
    }

}
