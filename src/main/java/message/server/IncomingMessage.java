/*
 * This file is part of ProDisFuzz, modified on 14.09.16 21:51.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package message.server;

import message.AbstractIncomingMessage;
import protocol.StateMachine;

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
