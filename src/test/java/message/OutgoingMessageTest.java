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
import java.util.HashMap;
import java.util.Map;

public class OutgoingMessageTest {
    @Test
    public void testGetBytes() throws Exception {
        OutgoingServerMessage outgoingServerMessage =
                new OutgoingServerMessage(StateMachine.ServerAnswerCommand.ROK, 123456);
        Assert.assertEquals(outgoingServerMessage.getBytes(), "ROK 6 123456".getBytes(StandardCharsets.UTF_8));

        outgoingServerMessage = new OutgoingServerMessage(StateMachine.ServerAnswerCommand.ROK,
                "abcdefäöüß".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(outgoingServerMessage.getBytes(),
                ("ROK " + "abcdefäöüß".getBytes(StandardCharsets.UTF_8).length + " abcdefäöüß")
                        .getBytes(StandardCharsets.UTF_8));

        outgoingServerMessage = new OutgoingServerMessage(StateMachine.ServerAnswerCommand.ROK, "abcdefäöüß");
        Assert.assertEquals(outgoingServerMessage.getBytes(),
                ("ROK " + "abcdefäöüß".getBytes(StandardCharsets.UTF_8).length + " abcdefäöüß")
                        .getBytes(StandardCharsets.UTF_8));

        outgoingServerMessage = new OutgoingServerMessage(StateMachine.ServerAnswerCommand.ROK);
        Assert.assertEquals(outgoingServerMessage.getBytes(), "ROK 0 ".getBytes(StandardCharsets.UTF_8));

        Map<String, String> map = new HashMap<>(3);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        outgoingServerMessage = new OutgoingServerMessage(StateMachine.ServerAnswerCommand.ROK, map);
        Assert.assertEquals(outgoingServerMessage.getBytes(),
                "ROK 35 key1=value1,key2=value2,key3=value3".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCommand() throws Exception {
        for (StateMachine.ServerAnswerCommand each : StateMachine.ServerAnswerCommand.values()) {
            OutgoingServerMessage outgoingServerMessage = new OutgoingServerMessage(each);
            Assert.assertEquals(outgoingServerMessage.getCommand(), each);
        }
        for (StateMachine.ClientRequestCommand each : StateMachine.ClientRequestCommand.values()) {
            OutgoingClientMessage outgoingClientMessage = new OutgoingClientMessage(each);
            Assert.assertEquals(outgoingClientMessage.getCommand(), each);
        }
    }

    private static class OutgoingServerMessage extends message.OutgoingMessage<StateMachine.ServerAnswerCommand> {
        OutgoingServerMessage(StateMachine.ServerAnswerCommand command, int body) {
            super(command, body);
        }

        OutgoingServerMessage(StateMachine.ServerAnswerCommand command, byte... body) {
            super(command, body);
        }

        OutgoingServerMessage(StateMachine.ServerAnswerCommand command, String body) {
            super(command, body);
        }

        OutgoingServerMessage(StateMachine.ServerAnswerCommand command) {
            super(command);
        }

        OutgoingServerMessage(StateMachine.ServerAnswerCommand command, Map<String, String> body) {
            super(command, body);
        }
    }

    private static class OutgoingClientMessage extends message.OutgoingMessage<StateMachine.ClientRequestCommand> {

        OutgoingClientMessage(StateMachine.ClientRequestCommand command) {
            super(command);
        }
    }
}
