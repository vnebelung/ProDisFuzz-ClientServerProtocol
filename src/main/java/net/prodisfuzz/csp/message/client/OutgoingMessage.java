/*
 * This file is part of ProDisFuzz, modified on 25.06.18 20:11.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.message.client;

import net.prodisfuzz.csp.message.AbstractOutgoingMessage;
import net.prodisfuzz.csp.protocol.StateMachine;

import java.util.Map;

/**
 * This class represents a message that is sent from the client to the server.
 */
public class OutgoingMessage extends AbstractOutgoingMessage<StateMachine.ClientRequestCommand> {

    /**
     * Constructs a new message that represents a message sent from the client to the server.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ClientRequestCommand command, byte... body) {
        super(command, body);
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ClientRequestCommand command, String body) {
        super(command, body);
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server. This message
     * has an empty body.
     *
     * @param command the message's command
     */
    public OutgoingMessage(StateMachine.ClientRequestCommand command) {
        super(command);
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ClientRequestCommand command, Map<String, String> body) {
        super(command, body);
    }
}
