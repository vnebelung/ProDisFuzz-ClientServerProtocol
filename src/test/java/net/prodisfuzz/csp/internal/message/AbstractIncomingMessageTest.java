/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:57.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.message;

import net.prodisfuzz.csp.internal.protocol.StateMachine;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

public class AbstractIncomingMessageTest {
    @Test
    public void testGetCommand() {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            AbstractIncomingMessageTest.IncomingServerMessage incomingServerMessage =
                    new AbstractIncomingMessageTest.IncomingServerMessage(each);
            Assert.assertEquals(incomingServerMessage.getCommand(), each);
        }
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            AbstractIncomingMessageTest.IncomingClientMessage incomingClientMessage =
                    new AbstractIncomingMessageTest.IncomingClientMessage(each);
            Assert.assertEquals(incomingClientMessage.getCommand(), each);
        }
    }

    @Test
    public void testGetBody() {
        String string = "test";
        AbstractIncomingMessageTest.IncomingServerMessage incomingServerMessage =
                new AbstractIncomingMessageTest.IncomingServerMessage(StateMachine.ServerAnswerCommand.ROK,
                        string.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(incomingServerMessage.getBody(), string.getBytes(StandardCharsets.UTF_8));
    }

    private static class IncomingServerMessage extends AbstractIncomingMessage<StateMachine.ServerAnswerCommand> {
        IncomingServerMessage(@SuppressWarnings("SameParameterValue") StateMachine.ServerAnswerCommand command,
                              byte... body) {
            super(command, body);
        }
    }

    private static class IncomingClientMessage extends AbstractIncomingMessage<StateMachine.ClientRequestCommand> {
        IncomingClientMessage(StateMachine.ClientRequestCommand command, byte... body) {
            super(command, body);
        }
    }

}
