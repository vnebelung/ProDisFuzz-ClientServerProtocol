/*
 * This file is part of ProDisFuzz, modified on 27.09.16 22:02.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package packet;

import message.client.IncomingMessage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import protocol.StateMachine;
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
    }

    private class PacketParser extends AbstractPacketParser<IncomingMessage, StateMachine.ServerAnswerCommand> {

        PacketParser(InputStream inputStream) {
            super(inputStream);
        }
    }

}
