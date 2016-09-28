/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:36.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.message.server;

import net.prodisfuzz.csp.message.AbstractOutgoingMessage;
import net.prodisfuzz.csp.protocol.StateMachine;

import java.util.Map;

/**
 * This class represents a message that is sent from the server to the client.
 */
public class OutgoingMessage extends AbstractOutgoingMessage<StateMachine.ServerAnswerCommand> {

    /**
     * Constructs a new message that represents a message sent from the server to the client.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ServerAnswerCommand command, String body) {
        super(command, body);
    }

    /**
     * Constructs a new message that represents a message sent from the server to the client.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ServerAnswerCommand command, Map<String, String> body) {
        super(command, body);
    }
}
