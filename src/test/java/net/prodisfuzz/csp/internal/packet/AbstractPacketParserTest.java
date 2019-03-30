/*
 * This file is part of ProDisFuzz, modified on 3/22/19 9:10 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.packet;

import net.prodisfuzz.csp.internal.message.client.IncomingMessage;
import net.prodisfuzz.csp.internal.protocol.StateMachine;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

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
        assertEquals(incomingMessage.getCommand(), StateMachine.ServerAnswerCommand.ROK);
        assertEquals(incomingMessage.getBody(), "test".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage1() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK test");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class, expectedExceptionsMessageRegExp = "Protocol format " +
            "error: Received command has not the structure 'CCC '")
    public void testReadIncomingMessage2() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("RO");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test
    public void testReadIncomingMessage3() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 4 test");
        BrokenPacketParser brokenPacketParser = new BrokenPacketParser(streamSimulator.getInputStream());
        assertNull(brokenPacketParser.readIncomingMessage(Boolean.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK)));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage4() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK ");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage5() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK test 4");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage6() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK   ");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage7() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 4 tes");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage8() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ABC 4 test");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ROK", StateMachine.ServerAnswerCommand.ROK));
    }

    @Test(expectedExceptions = ProtocolFormatException.class)
    public void testReadIncomingMessage9() throws IOException, ProtocolFormatException {
        streamSimulator.writeForInputStream("ABCD 4 test");
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());
        packetParser.readIncomingMessage(IncomingMessage.class, StateMachine.ServerAnswerCommand.class,
                Collections.singletonMap("ABC", StateMachine.ServerAnswerCommand.ROK));
    }

    private class PacketParser extends AbstractPacketParser<IncomingMessage, StateMachine.ServerAnswerCommand> {

        PacketParser(InputStream inputStream) {
            super(inputStream);
        }
    }

    private class BrokenPacketParser extends AbstractPacketParser<Boolean, StateMachine.ServerAnswerCommand> {

        BrokenPacketParser(InputStream inputStream) {
            super(inputStream);
        }
    }

}
