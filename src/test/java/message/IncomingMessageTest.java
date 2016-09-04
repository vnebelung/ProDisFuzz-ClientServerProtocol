/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:45 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package message;

import org.testng.Assert;
import org.testng.annotations.Test;
import protocol.StateMachine;

import java.nio.charset.StandardCharsets;

public class IncomingMessageTest {
    @Test
    public void testGetCommand() throws Exception {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            IncomingServerMessage incomingServerMessage = new IncomingServerMessage(each);
            Assert.assertEquals(incomingServerMessage.getCommand(), each);
        }
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            IncomingClientMessage incomingClientMessage = new IncomingClientMessage(each);
            Assert.assertEquals(incomingClientMessage.getCommand(), each);
        }
    }

    @Test
    public void testGetBody() throws Exception {
        String string = "test";
        IncomingServerMessage incomingServerMessage = new IncomingServerMessage(StateMachine.ServerAnswerCommand.ROK,
                string.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(incomingServerMessage.getBody(), string.getBytes(StandardCharsets.UTF_8));
    }

    private static class IncomingServerMessage extends message.IncomingMessage<StateMachine.ServerAnswerCommand> {
        IncomingServerMessage(StateMachine.ServerAnswerCommand command, byte... body) {
            super(command, body);
        }
    }

    private static class IncomingClientMessage extends message.IncomingMessage<StateMachine.ClientRequestCommand> {
        IncomingClientMessage(StateMachine.ClientRequestCommand command, byte... body) {
            super(command, body);
        }
    }

}
