/*
 * This file is part of ProDisFuzz, modified on 3/30/19 4:17 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.message;

import net.prodisfuzz.csp.internal.message.client.OutgoingMessage;
import net.prodisfuzz.csp.internal.protocol.StateMachine;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AbstractOutgoingMessageTest {

    @Test
    public void testGetBytes() {
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT,
                Collections.singletonMap("version", String.valueOf(123456)));
        assertEquals(outgoingMessage.getBytes(), "AYT 14 version=123456".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBytes1() {
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT,
                Collections.singletonMap("abcdefäöüß", "abcdefäöüß"));
        String string = "abcdefäöüß=abcdefäöüß";
        assertEquals(outgoingMessage.getBytes(),
                (("AYT " + string.getBytes(StandardCharsets.UTF_8).length + " " + string)
                        .getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testGetBytes2() {
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT);
        assertEquals(outgoingMessage.getBytes(), "AYT 0 ".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBytes3() {
        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT, map);
        assertEquals(outgoingMessage.getBytes(),
                "AYT 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBytes4() {
        AbstractOutgoingMessage outgoingMessage =
                new net.prodisfuzz.csp.internal.message.server.OutgoingMessage(StateMachine.ServerAnswerCommand.ROK,
                        Collections.singletonMap("version", String.valueOf(123456)));
        assertEquals(outgoingMessage.getBytes(), "ROK 14 version=123456".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBytes5() {
        AbstractOutgoingMessage outgoingMessage =
                new net.prodisfuzz.csp.internal.message.server.OutgoingMessage(StateMachine.ServerAnswerCommand.ROK,
                        Collections.singletonMap("abcdefäöüß", "abcdefäöüß"));
        String string = "abcdefäöüß=abcdefäöüß";
        assertEquals(outgoingMessage.getBytes(),
                (("ROK " + string.getBytes(StandardCharsets.UTF_8).length + " " + string)
                        .getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testGetBytes6() {
        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        AbstractOutgoingMessage outgoingMessage =
                new net.prodisfuzz.csp.internal.message.server.OutgoingMessage(StateMachine.ServerAnswerCommand.ROK,
                        map);
        assertEquals(outgoingMessage.getBytes(),
                "ROK 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCommand() {
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            OutgoingMessage outgoingMessage = new OutgoingMessage(each);
            assertEquals(outgoingMessage.getCommand(), each);
        }
    }

    @Test
    public void testGetCommand1() {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            net.prodisfuzz.csp.internal.message.server.OutgoingMessage outgoingMessage =
                    new net.prodisfuzz.csp.internal.message.server.OutgoingMessage(each, Collections.emptyMap());
            assertEquals(outgoingMessage.getCommand(), each);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor() {
        //noinspection unused
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT,
                Collections.singletonMap("key", "test test"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor1() {
        //noinspection unused
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT,
                Collections.singletonMap("key", "test,test"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor2() {
        //noinspection unused
        AbstractOutgoingMessage outgoingMessage = new OutgoingMessage(StateMachine.ClientRequestCommand.AYT,
                Collections.singletonMap("key", "test=test"));
    }

}
