/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:45 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import message.server.IncomingMessage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerTest {

    private StreamSimulator streamSimulator;

    @BeforeMethod
    public void setUp() throws IOException {
        streamSimulator = new StreamSimulator();
        streamSimulator.init();
    }

    @AfterMethod
    public void tearDown() throws IOException {
        streamSimulator.exit();
    }

    @Test
    public void testErr() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            server.err("testcause");
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                    "ERR 9 testcause".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void testRok11() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            server.rok(StateMachine.ClientRequestCommand.AYT, 44);
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                    "ROK 2 44".getBytes(StandardCharsets.UTF_8));
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testRok12() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            server.rok(StateMachine.ClientRequestCommand.SWA, 44);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testRok21() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            server.rok(StateMachine.ClientRequestCommand.AYT);
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testRok22() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            server.rok(StateMachine.ClientRequestCommand.SWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testRok31() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            Map<String, String> map = new HashMap<>(3);
            map.put("testkey1", "testvalue1");
            map.put("testkey2", "");
            map.put("testkey3", "testvalue3");
            server.rok(StateMachine.ClientRequestCommand.AYT, map);
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                    ("ROK 49 testkey3=testvalue3,testkey2=," + "testkey1=testvalue1").getBytes(StandardCharsets.UTF_8));
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testRok32() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            Map<String, String> map = new HashMap<>(3);
            map.put("testkey1", "testvalue1");
            map.put("testkey2", "");
            map.put("testkey3", "testvalue3");
            server.rok(StateMachine.ClientRequestCommand.SWA, map);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testReceive1() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            streamSimulator.writeForInputStream("AYT 0 ");
            IncomingMessage incomingMessage = server.receive();
            Assert.assertEquals(incomingMessage.getCommand(), StateMachine.ClientRequestCommand.AYT);
            Assert.assertEquals(incomingMessage.getBody(), new byte[0]);
        }
    }

    @Test
    public void testReceive2() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            streamSimulator.writeForInputStream("ABC 1 a");
            IncomingMessage incomingMessage = server.receive();
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                    ("ERR 49 Protocol error: Received command" + " 'ABC' is invalid").getBytes(StandardCharsets.UTF_8));
            Assert.assertNull(incomingMessage);
        }
    }

    @Test
    public void testReceive3() throws IOException {
        try (DataInputStream dataInputStream = streamSimulator.getDataInputStream();
                DataOutputStream dataOutputStream = streamSimulator.getDataOutputStream()) {
            Server server = new Server(dataInputStream, dataOutputStream);
            streamSimulator.writeForInputStream("GWA 0 ");
            IncomingMessage incomingMessage = server.receive();
            Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                    ("ERR 58 Command 'GWA' is not allowed at " + "the current protocol state")
                            .getBytes(StandardCharsets.UTF_8));
            Assert.assertNull(incomingMessage);
        }
    }
}
