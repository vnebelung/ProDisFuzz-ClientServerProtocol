/*
 * This file is part of ProDisFuzz, modified on 3/23/19 3:37 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.message;

import net.prodisfuzz.csp.internal.message.client.IncomingMessage;
import net.prodisfuzz.csp.internal.protocol.StateMachine;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;

public class AbstractIncomingMessageTest {

    @Test
    public void testGetCommand() {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            IncomingMessage incomingMessage = new IncomingMessage(each);
            assertEquals(incomingMessage.getCommand(), each);
        }
    }

    @Test
    public void testGetCommand1() {
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            net.prodisfuzz.csp.internal.message.server.IncomingMessage incomingMessage =
                    new net.prodisfuzz.csp.internal.message.server.IncomingMessage(each);
            assertEquals(incomingMessage.getCommand(), each);
        }
    }

    @Test
    public void testGetBody() {
        String string = "test";
        IncomingMessage incomingServerMessage =
                new IncomingMessage(StateMachine.ServerAnswerCommand.ROK, string.getBytes(StandardCharsets.UTF_8));
        assertEquals(incomingServerMessage.getBody(), string.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBody1() {
        String string = "test";
        net.prodisfuzz.csp.internal.message.server.IncomingMessage incomingMessage =
                new net.prodisfuzz.csp.internal.message.server.IncomingMessage(StateMachine.ClientRequestCommand.AYT,
                        string.getBytes(StandardCharsets.UTF_8));
        assertEquals(incomingMessage.getBody(), string.getBytes(StandardCharsets.UTF_8));
    }

}
