/*
 * This file is part of ProDisFuzz, modified on 3/24/19 11:15 AM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import support.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ServerProtocolTest {

    private StreamSimulator streamSimulator;
    private ServerProtocol serverProtocol;
    private AytListener aytListener;
    private FuzListener fuzListener;
    private TcoListener tcoListener;
    private GcoListener gcoListener;
    private GwaListener gwaListener;
    private RstListener rstListener;
    private ScoListener scoListener;
    private ScpListener scpListener;
    private SwaListener swaListener;
    private SwpListener swpListener;
    private TwaListener twaListener;

    @BeforeMethod
    public void setUp() throws Exception {
        streamSimulator = new StreamSimulator();
        aytListener = new AytListener(123456);
        Map<String, String> fuzParams = new HashMap<>(2);
        fuzParams.put("a", "b");
        fuzParams.put("c", "d");
        fuzListener = new FuzListener(fuzParams);
        tcoListener = new TcoListener(fuzParams);
        twaListener = new TwaListener(fuzParams);
        Set<String> gcoParams = new HashSet<>(2);
        gcoParams.add("a");
        gcoParams.add("b");
        gcoListener = new GcoListener(gcoParams);
        gwaListener = new GwaListener(gcoParams);
        rstListener = new RstListener();
        scoListener = new ScoListener();
        scpListener = new ScpListener();
        swaListener = new SwaListener();
        swpListener = new SwpListener();
        serverProtocol =
                new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream(), aytListener,
                        gcoListener, scoListener, scpListener, tcoListener, gwaListener, swaListener, swpListener,
                        twaListener, fuzListener,
                        rstListener);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        streamSimulator.exit();
    }

    @Test
    public void testReceive1() throws IOException {
        streamSimulator.writeForInputStream("RSTT 0 ");
        serverProtocol.receive();
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 68 Protocol format error: Received command has not the structure 'CCC '"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive2() throws IOException {
        streamSimulator.writeForInputStream("RST 0 ");
        serverProtocol.receive();
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 80 Protocol state error: Command 'RST' is not allowed at the current protocol state"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive3() throws IOException {
        streamSimulator.writeForInputStream("AYT 7 abc=def");
        aytListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(aytListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        aytListener.reset();
        streamSimulator.writeForInputStream("AYT 7 abc=def");
        serverProtocol.receive();
        assertTrue(aytListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 14 version=123456".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("GCO 7 abc=def");
        gcoListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(gcoListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        gcoListener.reset();
        streamSimulator.writeForInputStream("GCO 7 abc=def");
        serverProtocol.receive();
        assertTrue(gcoListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 25 connector1=b,connector0=a".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SCO 35 dummy=dummy,connector=a,connector=b");
        scoListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(scoListener.isTriggered());
        assertEquals(scoListener.getInValue(), "b");
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        scoListener.reset();
        streamSimulator.writeForInputStream("SCO 35 dummy=dummy,connector=a,connector=b");
        serverProtocol.receive();
        assertTrue(scoListener.isTriggered());
        assertEquals(scoListener.getInValue(), "b");
        assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SCP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        scpListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(scpListener.isTriggered());
        Map<String, String> tmp = new HashMap<>(2);
        tmp.put("dummy1", "dummy2");
        tmp.put("dummy3", "dummy5");
        assertEquals(scpListener.getInValue(), tmp);
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        scpListener.reset();
        streamSimulator.writeForInputStream("SCP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        serverProtocol.receive();
        assertTrue(scpListener.isTriggered());
        assertEquals(scpListener.getInValue(), tmp);
        assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("TCO 29 dummy=dummy,data=000102030405");
        tcoListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(tcoListener.isTriggered());
        assertEquals(tcoListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        tcoListener.reset();
        streamSimulator.writeForInputStream("TCO 29 dummy=dummy,data=000102030405");
        serverProtocol.receive();
        assertTrue(tcoListener.isTriggered());
        assertEquals(tcoListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 7 a=b,c=d".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("GWA 7 abc=def");
        gwaListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(gwaListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        gwaListener.reset();
        streamSimulator.writeForInputStream("GWA 7 abc=def");
        serverProtocol.receive();
        assertTrue(gwaListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 21 watcher0=a,watcher1=b".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SWA 31 dummy=dummy,watcher=a,watcher=b");
        swaListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(swaListener.isTriggered());
        assertEquals(swaListener.getInValue(), "b");
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        swaListener.reset();
        streamSimulator.writeForInputStream("SWA 31 dummy=dummy,watcher=a,watcher=b");
        serverProtocol.receive();
        assertTrue(swaListener.isTriggered());
        assertEquals(swaListener.getInValue(), "b");
        assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SWP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        swpListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(swpListener.isTriggered());
        tmp = new HashMap<>(2);
        tmp.put("dummy1", "dummy2");
        tmp.put("dummy3", "dummy5");
        assertEquals(swpListener.getInValue(), tmp);
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        swpListener.reset();
        streamSimulator.writeForInputStream("SWP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        serverProtocol.receive();
        assertTrue(swpListener.isTriggered());
        assertEquals(swpListener.getInValue(), tmp);
        assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("TWA 29 dummy=dummy,data=000102030405");
        twaListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(twaListener.isTriggered());
        assertEquals(twaListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        twaListener.reset();
        streamSimulator.writeForInputStream("TWA 29 dummy=dummy,data=000102030405");
        serverProtocol.receive();
        assertTrue(twaListener.isTriggered());
        assertEquals(twaListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 7 a=b,c=d".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("FUZ 29 dummy=dummy,data=000102030405");
        fuzListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(fuzListener.isTriggered());
        assertEquals(fuzListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        fuzListener.reset();
        streamSimulator.writeForInputStream("FUZ 29 dummy=dummy,data=000102030405");
        serverProtocol.receive();
        assertTrue(fuzListener.isTriggered());
        assertEquals(fuzListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 7 a=b,c=d".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("RST 7 abc=def");
        rstListener.setException("testexception");
        serverProtocol.receive();
        assertTrue(rstListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        rstListener.reset();
        streamSimulator.writeForInputStream("RST 7 abc=def");
        serverProtocol.receive();
        assertTrue(rstListener.isTriggered());
        assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));
    }

}
