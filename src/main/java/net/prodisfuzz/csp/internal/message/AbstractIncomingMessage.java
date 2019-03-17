/*
 * This file is part of ProDisFuzz, modified on 3/17/19 11:33 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.message;

import java.util.Arrays;

/**
 * This class represents a message that is received by a client or server.
 *
 * @param <C> the commands that can be used by this message
 */
public abstract class AbstractIncomingMessage<C> {

    private C command;
    private byte[] body;

    /**
     * Constructs a message received by client from the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    protected AbstractIncomingMessage(C command, byte... body) {
        this.command = command;
        this.body = body.clone();
    }

    /**
     * Returns the message's command
     *
     * @return the message's command
     */
    public C getCommand() {
        return command;
    }

    /**
     * Returns the message's body
     *
     * @return the message's body
     */
    public byte[] getBody() {
        return Arrays.copyOf(body, body.length);
    }


}
