/*
 * This file is part of ProDisFuzz, modified on 3/30/19 4:19 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.internal.packet.ProtocolFormatException;
import net.prodisfuzz.csp.internal.protocol.ProtocolStateException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ClientProtocolTest {

    private StreamSimulator streamSimulator;
    private ClientProtocol client;

    @BeforeClass
    public void setUp() throws IOException {
        streamSimulator = new StreamSimulator();
    }

    @AfterClass
    public void tearDown() throws IOException {
        streamSimulator.exit();
    }

    @BeforeMethod
    public void setupMethod() throws IOException {
        client = new ClientProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
        streamSimulator.getInputStream().readAllBytes();
    }

    @Test(expectedExceptions = ProtocolStateException.class)
    public void testAyt() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
        client.ayt();
    }

    @Test()
    public void testAyt1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        assertEquals(client.ayt(), "");
        assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testAyt2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 19 version=1,version=2");
        assertEquals(client.ayt(), "2");
        assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testAyt3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ERR 9 test test");
        client.ayt();
    }

    @Test
    public void testGco() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testGco1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        assertEquals(client.gco().size(), 0);
        assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testGco2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 38 connector0=1,connector0=2,connector1=3");
        Set<String> answer = client.gco();
        assertEquals(answer.size(), 2);
        assertTrue(answer.contains("2"));
        assertTrue(answer.contains("3"));
        assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testGco3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.gco();
    }

    @Test
    public void testSco() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCO 10 connector=".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCO 10 connector=".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testSco1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCO 10 connector=".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testSco2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("a");
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCO 11 connector=a".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testSco3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.sco("");
    }

    @Test
    public void testScp() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.scp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCP 0 ".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.scp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCP 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testScp1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.scp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SCP 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testScp2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        Map<String, String> map = new HashMap<>(2);
        map.put("key1", "value1");
        map.put("key2", "value2");
        streamSimulator.writeForInputStream("ROK 0 ");
        client.scp(map);
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "SCP 23 key1=value1,key2=value2".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testScp3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.scp(Collections.emptyMap());
    }

    @Test(expectedExceptions = ProtocolStateException.class)
    public void testTco() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        assertEquals(streamSimulator.readLastFromOutputStream(), "TCO 5 data=".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
    }

    @Test()
    public void testTco1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.tco();
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(), "TCO 5 data=".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testTco2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.tco("testdata".getBytes(StandardCharsets.UTF_8));
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "TCO 21 data=7465737464617461".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testTco3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.tco();
    }

    @Test
    public void testGwa() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.gwa();
        assertEquals(streamSimulator.readLastFromOutputStream(), "GWA 0 ".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gwa();
        streamSimulator.readLastFromOutputStream();
    }

    @Test()
    public void testGwa1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        assertEquals(client.gwa().size(), 0);
        assertEquals(streamSimulator.readLastFromOutputStream(), "GWA 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testGwa2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 32 watcher0=1,watcher0=2,watcher1=3");
        Set<String> answer = client.gwa();
        assertEquals(answer.size(), 2);
        assertTrue(answer.contains("2"));
        assertTrue(answer.contains("3"));
        assertEquals(streamSimulator.readLastFromOutputStream(), "GWA 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testGwa3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.gwa();
    }

    @Test
    public void testSwp() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.swp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SWP 0 ".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SWP 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testSwp1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.swp(Collections.emptyMap());
        assertEquals(streamSimulator.readLastFromOutputStream(), "SWP 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testSwp2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        Map<String, String> map = new HashMap<>(2);
        map.put("key1", "value1");
        map.put("key2", "value2");
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swp(map);
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "SWP 23 key1=value1,key2=value2".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testSwp3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.swp(Collections.emptyMap());
    }

    @Test(expectedExceptions = ProtocolStateException.class)
    public void testTwa() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
        assertEquals(streamSimulator.readLastFromOutputStream(), "TWA 5 data=".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
    }

    @Test()
    public void testTwa1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.twa();
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(), "TWA 5 data=".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testTwa2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.twa("testdata".getBytes(StandardCharsets.UTF_8));
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "TWA 21 data=7465737464617461".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testTwa3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.twa();
    }

    @Test
    public void testFuz() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.fuz();
        assertEquals(streamSimulator.readLastFromOutputStream(), "FUZ 5 data=".getBytes(StandardCharsets.UTF_8));
        streamSimulator.writeForInputStream("ROK 0 ");
        client.fuz();
    }

    @Test()
    public void testFuz1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.fuz();
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(), "FUZ 5 data=".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testFuz2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 35 key1=value1,key2=value2,key1=value3");
        Map<String, String> answer = client.fuz("testdata".getBytes(StandardCharsets.UTF_8));
        assertEquals(answer.size(), 2);
        assertEquals(answer.get("key1"), "value3");
        assertEquals(answer.get("key2"), "value2");
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "FUZ 21 data=7465737464617461".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testFuz3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.gco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.sco("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.tco();
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.swa("");
        streamSimulator.readLastFromOutputStream();
        streamSimulator.writeForInputStream("ROK 0 ");
        client.twa();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.fuz();
    }

    @Test(expectedExceptions = ProtocolStateException.class)
    public void testRst() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.rst();
        assertEquals(streamSimulator.readLastFromOutputStream(), "RST 0 ".getBytes(StandardCharsets.UTF_8));
        client.rst();
    }

    @Test()
    public void testRst1() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 0 ");
        client.rst();
        assertEquals(streamSimulator.readLastFromOutputStream(), "RST 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test()
    public void testRst2() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ROK 19 version=1,version=2");
        client.rst();
        assertEquals(streamSimulator.readLastFromOutputStream(), "RST 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void testRst3() throws IOException, ProtocolExecutionException, ProtocolStateException,
            ProtocolFormatException {
        streamSimulator.writeForInputStream("ROK 0 ");
        client.ayt();
        streamSimulator.readLastFromOutputStream();

        streamSimulator.writeForInputStream("ERR 9 test test");
        client.rst();
    }

}
