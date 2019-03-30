/*
 * This file is part of ProDisFuzz, modified on 3/24/19 11:00 AM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.packet.server;

import net.prodisfuzz.csp.internal.message.server.IncomingMessage;
import net.prodisfuzz.csp.internal.packet.ProtocolFormatException;
import net.prodisfuzz.csp.internal.protocol.StateMachine;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

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

        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            streamSimulator.writeForInputStream(each + " 17 testkey=testvalue");
            IncomingMessage incomingMessage = null;
            try {
                incomingMessage = packetParser.readIncomingMessage();
            } catch (ProtocolFormatException e) {
                fail();
            }
            assertEquals(incomingMessage.getCommand(), each);
            assertEquals(incomingMessage.getBody(), "testkey=testvalue".getBytes(StandardCharsets.UTF_8));
        }

        String[] wrong = {"AY 17 testkey=testvalue", "AYTT 17 testkey=testvalue", "AAA 4 17 testkey=testvalue",
                "AYT " + "testkey=testvalue", "AYT 17 testkey=testvalu", "AYT 17 ", "AYT 17", "AYT 0",
                "AYT 16 " + "testkeytestvalue", "AYT 18 testkey=testvalue,", "AYT 25 testkey=testvalue,testkey",
                "AYT 18 " + "testkey=testvalue,", "AYT 27 testkey=testvalue=testvalue"};
        for (String each : wrong) {
            streamSimulator.writeForInputStream(each);
            try {
                packetParser.readIncomingMessage();
                fail();
            } catch (ProtocolFormatException ignored) {
            }
        }

    }
}
