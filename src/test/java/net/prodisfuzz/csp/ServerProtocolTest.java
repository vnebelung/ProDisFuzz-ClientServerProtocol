/*
 * This file is part of ProDisFuzz, modified on 06.07.18 15:30.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.protocol.ProtocolStateException;
import net.prodisfuzz.csp.protocol.StateMachine;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerProtocolTest {

    private StreamSimulator streamSimulator;

    @BeforeMethod
    public void setUp() throws Exception {
        streamSimulator = new StreamSimulator();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        streamSimulator.exit();
    }

    @Test
    public void testReceive1() throws IOException {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.writeForInputStream("RSTT 0 ");
        Assert.assertNull(server.receive());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 68 Protocol format error: Received command has not the structure 'CCC '"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive2() throws IOException {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.writeForInputStream("RST 0 ");
        Assert.assertNull(server.receive());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 80 Protocol state error: Command 'RST' is not allowed at the current protocol state"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive3() throws IOException {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.writeForInputStream("AYT 0 ");
        net.prodisfuzz.csp.message.server.IncomingMessage incomingMessage = server.receive();
        Assert.assertEquals(incomingMessage.getCommand(), StateMachine.ClientRequestCommand.AYT);
        Assert.assertEquals(incomingMessage.getBody(), new byte[]{});
    }

    @Test
    public void testRok1_1() throws Exception {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        try {
            server.rok(Collections.emptyMap());
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testRok1_2() throws Exception {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.writeForInputStream("AYT 0 ");
        server.receive();
        server.rok(Collections.emptyMap());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testRok2_1() throws Exception {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        try {
            server.rok(Collections.singletonMap("key1", "value1"));
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testRok2_2() throws Exception {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.writeForInputStream("AYT 0 ");
        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "");
        map.put("key3", "value3");
        server.receive();
        server.rok(map);
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 29 key1=value1,key2=,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testErr() throws Exception {
        ServerProtocol server = new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        server.err("cause1");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 6 cause1".getBytes(StandardCharsets.UTF_8));
    }

}
