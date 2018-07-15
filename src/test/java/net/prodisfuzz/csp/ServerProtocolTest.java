/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:09.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import org.testng.Assert;
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

public class ServerProtocolTest {

    private StreamSimulator streamSimulator;
    private ServerProtocol serverProtocol;
    private AytListener aytListener;
    private CtfListener ctfListener;
    private CttListener cttListener;
    private GcoListener gcoListener;
    private GwaListener gwaListener;
    private RstListener rstListener;
    private ScoListener scoListener;
    private ScpListener scpListener;
    private SwaListener swaListener;

    @BeforeMethod
    public void setUp() throws Exception {
        streamSimulator = new StreamSimulator();
        aytListener = new AytListener(123456);
        Map<String, String> ctfParams = new HashMap<>(2);
        ctfParams.put("a", "b");
        ctfParams.put("c", "d");
        ctfListener = new CtfListener(ctfParams);
        cttListener = new CttListener(ctfParams);
        Set<String> gcoParams = new HashSet<>(2);
        gcoParams.add("a");
        gcoParams.add("b");
        gcoListener = new GcoListener(gcoParams);
        gwaListener = new GwaListener(gcoParams);
        rstListener = new RstListener();
        scoListener = new ScoListener();
        scpListener = new ScpListener();
        swaListener = new SwaListener();
        serverProtocol =
                new ServerProtocol(streamSimulator.getInputStream(), streamSimulator.getOutputStream(), aytListener,
                        gcoListener, scoListener, scpListener, cttListener, gwaListener, swaListener, ctfListener,
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
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 68 Protocol format error: Received command has not the structure 'CCC '"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive2() throws IOException {
        streamSimulator.writeForInputStream("RST 0 ");
        serverProtocol.receive();
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 80 Protocol state error: Command 'RST' is not allowed at the current protocol state"
                        .getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReceive3() throws IOException {
        streamSimulator.writeForInputStream("AYT 7 abc=def");
        aytListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(aytListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        aytListener.reset();
        streamSimulator.writeForInputStream("AYT 7 abc=def");
        serverProtocol.receive();
        Assert.assertTrue(aytListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 14 version=123456".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("GCO 7 abc=def");
        gcoListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(gcoListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        gcoListener.reset();
        streamSimulator.writeForInputStream("GCO 7 abc=def");
        serverProtocol.receive();
        Assert.assertTrue(gcoListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 25 connector1=b,connector0=a".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SCO 35 dummy=dummy,connector=a,connector=b");
        scoListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(scoListener.isTriggered());
        Assert.assertEquals(scoListener.getInValue(), "b");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        scoListener.reset();
        streamSimulator.writeForInputStream("SCO 35 dummy=dummy,connector=a,connector=b");
        serverProtocol.receive();
        Assert.assertTrue(scoListener.isTriggered());
        Assert.assertEquals(scoListener.getInValue(), "b");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SCP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        scpListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(scpListener.isTriggered());
        Map<String, String> tmp = new HashMap<>(2);
        tmp.put("dummy1", "dummy2");
        tmp.put("dummy3", "dummy5");
        Assert.assertEquals(scpListener.getInValue(), tmp);
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        scpListener.reset();
        streamSimulator.writeForInputStream("SCP 41 dummy1=dummy2,dummy3=dummy4,dummy3=dummy5");
        serverProtocol.receive();
        Assert.assertTrue(scpListener.isTriggered());
        Assert.assertEquals(scpListener.getInValue(), tmp);
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("CTT 29 dummy=dummy,data=000102030405");
        cttListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(cttListener.isTriggered());
        Assert.assertEquals(cttListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        cttListener.reset();
        streamSimulator.writeForInputStream("CTT 29 dummy=dummy,data=000102030405");
        serverProtocol.receive();
        Assert.assertTrue(cttListener.isTriggered());
        Assert.assertEquals(cttListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 7 a=b,c=d".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("GWA 7 abc=def");
        gwaListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(gwaListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        gwaListener.reset();
        streamSimulator.writeForInputStream("GWA 7 abc=def");
        serverProtocol.receive();
        Assert.assertTrue(gwaListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 21 watcher0=a,watcher1=b".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("SWA 31 dummy=dummy,watcher=a,watcher=b");
        swaListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(swaListener.isTriggered());
        Assert.assertEquals(swaListener.getInValue(), "b");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        swaListener.reset();
        streamSimulator.writeForInputStream("SWA 31 dummy=dummy,watcher=a,watcher=b");
        serverProtocol.receive();
        Assert.assertTrue(swaListener.isTriggered());
        Assert.assertEquals(swaListener.getInValue(), "b");
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("CTF 29 dummy=dummy,data=000102030405");
        ctfListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(ctfListener.isTriggered());
        Assert.assertEquals(ctfListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        ctfListener.reset();
        streamSimulator.writeForInputStream("CTF 29 dummy=dummy,data=000102030405");
        serverProtocol.receive();
        Assert.assertTrue(ctfListener.isTriggered());
        Assert.assertEquals(ctfListener.getInValue(), new byte[]{0, 1, 2, 3, 4, 5});
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ROK 7 a=b,c=d".getBytes(StandardCharsets.UTF_8));

        streamSimulator.writeForInputStream("RST 7 abc=def");
        rstListener.setException("testexception");
        serverProtocol.receive();
        Assert.assertTrue(rstListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(),
                "ERR 13 testexception".getBytes(StandardCharsets.UTF_8));

        rstListener.reset();
        streamSimulator.writeForInputStream("RST 7 abc=def");
        serverProtocol.receive();
        Assert.assertTrue(rstListener.isTriggered());
        Assert.assertEquals(streamSimulator.readLastFromOutputStream(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));
    }

}
