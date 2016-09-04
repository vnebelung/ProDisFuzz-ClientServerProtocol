/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:42 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package message.server;

import protocol.StateMachine;

import java.util.Map;

/**
 * This class represents a message that is sent from the server to the client.
 */
public class OutgoingMessage extends message.OutgoingMessage<StateMachine.ServerAnswerCommand> {

    /**
     * Constructs a new message that represents a message sent from the server to the client.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public OutgoingMessage(StateMachine.ServerAnswerCommand command, int body) {
        super(command, body);
    }

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
     * Constructs a new message that represents a message sent from the server to the client. This message
     * has an empty body.
     *
     * @param command the message's command
     */
    public OutgoingMessage(StateMachine.ServerAnswerCommand command) {
        super(command);
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
