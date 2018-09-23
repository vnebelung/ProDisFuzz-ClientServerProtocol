/*
 * This file is part of ProDisFuzz, modified on 23.09.18 09:11.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.internal.packet.ProtocolFormatException;
import net.prodisfuzz.csp.internal.protocol.ProtocolStateException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import support.StreamSimulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ClientProtocolTest {

    private StreamSimulator streamSimulator;
    private ClientProtocol client;

    @BeforeClass
    public void setUp() throws IOException {
        streamSimulator = new StreamSimulator();
        client = new ClientProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream());
    }

    @AfterClass
    public void tearDown() throws IOException {
        streamSimulator.exit();
    }

    @Test(priority = 1)
    public void testGco1() throws IOException, ProtocolExecutionException {
        try {
            client.gco();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testSco1() throws IOException, ProtocolExecutionException {
        try {
            client.sco("");
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testScp1() throws IOException, ProtocolExecutionException {
        try {
            client.scp(Collections.emptyMap());
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testTco1() throws IOException, ProtocolExecutionException {
        try {
            client.tco();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testGwa1() throws IOException, ProtocolExecutionException {
        try {
            client.gwa();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testSwa1() throws IOException, ProtocolExecutionException {
        try {
            client.swa("");
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testSwp1() throws IOException, ProtocolExecutionException {
        try {
            client.swp(Collections.emptyMap());
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testTwa1() throws IOException, ProtocolExecutionException {
        try {
            client.twa();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testFuz1() throws IOException, ProtocolExecutionException {
        try {
            client.fuz();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 1)
    public void testRst1() throws IOException, ProtocolExecutionException {
        try {
            client.rst();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 2)
    public void testAyt2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("AAA 4 test");
        try {
            client.ayt();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
            streamSimulator.readLastFromOutputStream();
        }

        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.ayt();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        Map<String, String> received = client.ayt();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, Collections.emptyMap());
    }

    @Test(priority = 3)
    public void testAyt1() throws IOException, ProtocolExecutionException {
        try {
            client.ayt();
            Assert.fail();
        } catch (ProtocolFormatException | ProtocolStateException ignored) {
        }
    }

    @Test(priority = 4)
    public void testGco2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.gco();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 49 connector1=test,connector2=test2,connector1=test3");
        Set<String> received = client.gco();
        Set<String> reference = new HashSet<>(2);
        reference.add("test2");
        reference.add("test3");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "GCO 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, reference);
    }

    @Test(priority = 5)
    public void testSco2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.sco("testconnector");
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SCO 13 testconnector".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        client.sco("testconnector");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SCO 13 testconnector".getBytes(StandardCharsets.UTF_8));
    }

    @Test(priority = 6)
    public void testScp2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        try {
            client.scp(map);
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SCP 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        client.scp(map);
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SCP 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test(priority = 7)
    public void testTco2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.tco();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "TCO 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        Map<String, String> received = client.tco();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "TCO 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, Collections.singletonMap("testkey", "testvalue"));
    }

    @Test(priority = 8)
    public void testGwa2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.gwa();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "GWA 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 44 watcher1=test1,watcher2=test2,watcher1=test3");
        Set<String> received = client.gwa();
        Set<String> reference = new HashSet<>(2);
        reference.add("test2");
        reference.add("test3");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "GWA 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, reference);
    }

    @Test(priority = 9)
    public void testSwa2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.swa("testwatcher");
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SWA 11 testwatcher".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        client.swa("testwatcher");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SWA 11 testwatcher".getBytes(StandardCharsets.UTF_8));
    }

    @Test(priority = 10)
    public void testSwp2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        try {
            client.swp(map);
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SWP 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        client.swp(map);
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "SWP 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test(priority = 11)
    public void testTwa2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.twa();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "TWA 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        Map<String, String> received = client.twa();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "TWA 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, Collections.singletonMap("testkey", "testvalue"));
    }

    @Test(priority = 12)
    public void testFuz2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.fuz("test".getBytes(StandardCharsets.UTF_8));
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "FUZ 4 test".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 39 testkey1=testvalue1,testkey2=testvalue2");
        Map<String, String> reference = new HashMap<>(2);
        reference.put("testkey1", "testvalue1");
        reference.put("testkey2", "testvalue2");
        Map<String, String> received = client.fuz("test".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "FUZ 4 test".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, reference);
    }

    @Test(priority = 13)
    public void testRst2() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ERR 4 test");
        try {
            client.rst();
            Assert.fail();
        } catch (ProtocolExecutionException e) {
            Assert.assertEquals(e.getMessage(), "test");
        }
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "RST 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("ROK 17 testkey=testvalue");
        client.rst();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "RST 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test(priority = 14)
    public void testAyt3() throws IOException, ProtocolStateException, ProtocolFormatException,
            ProtocolExecutionException {
        streamSimulator.writeForInputStream("ROK 12 version=test");
        Map<String, String> received = client.ayt();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(received, Collections.singletonMap("version", "test"));

    }

}
