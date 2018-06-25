/*
 * This file is part of ProDisFuzz, modified on 25.06.18 20:11.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.packet.client;

import net.prodisfuzz.csp.message.client.IncomingMessage;
import net.prodisfuzz.csp.packet.ProtocolFormatException;
import net.prodisfuzz.csp.protocol.StateMachine;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketParserTest {

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
    public void testReadIncomingMessage() throws IOException {
        PacketParser packetParser = new PacketParser(streamSimulator.getInputStream());

        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            streamSimulator.writeForInputStream(each + " 9 key=value");
            IncomingMessage incomingMessage = null;
            try {
                incomingMessage = packetParser.readIncomingMessage();
            } catch (ProtocolFormatException e) {
                Assert.fail();
            }
            Assert.assertEquals(incomingMessage.getCommand(), each);
            Assert.assertEquals(incomingMessage.getBody(), "key=value".getBytes(StandardCharsets.UTF_8));
        }

        String[] wrong =
                {"RO 9 key=value", "ROKK 9 key=value", "AAA 9 key=value", "ROK key=value", "ROK 10 " + "key=value",
                        "ROK 9 ", "ROK 9", "ROK 0", "ROK 9 Key=value", "ROK 9 keY=value", "ROK 9 kEY=value",
                        "ROK 10 key=value,", "ROK 10 ,key=value", "ROK 10 key=,value", "ROK 10 key,=value",
                        "ROK 10 key=value,", "ROK 5 key=="};
        for (String each : wrong) {
            streamSimulator.writeForInputStream(each);
            try {
                packetParser.readIncomingMessage();
                Assert.fail();
            } catch (ProtocolFormatException ignored) {
            }
        }

    }

}
