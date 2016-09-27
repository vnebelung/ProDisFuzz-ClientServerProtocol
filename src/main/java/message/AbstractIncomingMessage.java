/*
 * This file is part of ProDisFuzz, modified on 22.09.16 20:46.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package message;

/**
 * This class represents a message that is received by a client or server.
 */
public abstract class AbstractIncomingMessage<V> {

    private V command;
    private byte[] body;

    /**
     * Constructs a message received by client from the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    public AbstractIncomingMessage(V command, byte... body) {
        this.command = command;
        this.body = body.clone();
    }

    /**
     * Returns the message's command
     *
     * @return the message's command
     */
    public V getCommand() {
        return command;
    }

    /**
     * Returns the message's body
     *
     * @return the message's body
     */
    public byte[] getBody() {
        return body.clone();
    }


}
