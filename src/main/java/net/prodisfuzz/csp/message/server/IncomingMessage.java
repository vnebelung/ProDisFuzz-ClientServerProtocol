/*
 * This file is part of ProDisFuzz, modified on 25.06.18 20:11.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.message.server;

import net.prodisfuzz.csp.message.AbstractIncomingMessage;
import net.prodisfuzz.csp.protocol.StateMachine;

/**
 * This class represents a message that is received by the server from the client.
 */
public class IncomingMessage extends AbstractIncomingMessage<StateMachine.ClientRequestCommand> {

    /**
     * Constructs a message received by the server from the client.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public IncomingMessage(StateMachine.ClientRequestCommand command, byte... body) {
        super(command, body);
    }

}
