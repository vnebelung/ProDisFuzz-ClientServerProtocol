/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:57.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.message.client;

import net.prodisfuzz.csp.internal.message.AbstractIncomingMessage;
import net.prodisfuzz.csp.internal.protocol.StateMachine;

/**
 * This class represents a message that is received by the client from the server.
 */
public class IncomingMessage extends AbstractIncomingMessage<StateMachine.ServerAnswerCommand> {

    /**
     * Constructs a message received by the client from the server.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public IncomingMessage(StateMachine.ServerAnswerCommand command, byte... body) {
        super(command, body);
    }

}
