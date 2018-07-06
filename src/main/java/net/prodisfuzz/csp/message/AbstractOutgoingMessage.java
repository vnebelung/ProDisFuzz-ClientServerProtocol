/*
 * This file is part of ProDisFuzz, modified on 06.07.18 15:01.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.message;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents a message that is transmitted from the client to the server or vice versa.
 */
public abstract class AbstractOutgoingMessage<V> {

    private V command;
    private byte[] body;

    /**
     * Constructs a new message that represents a message sent from the client to the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    AbstractOutgoingMessage(V command, int body) {
        this.command = command;
        this.body = String.valueOf(body).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    protected AbstractOutgoingMessage(V command, byte... body) {
        this.command = command;
        this.body = body;
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    protected AbstractOutgoingMessage(V command, String body) {
        this.command = command;
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server or vice versa. This message
     * has an empty body.
     *
     * @param command the message's command
     */
    protected AbstractOutgoingMessage(V command) {
        this.command = command;
        //noinspection ZeroLengthArrayAllocation
        body = new byte[0];
    }

    /**
     * Constructs a new message that represents a message sent from the client to the server or vice versa.
     *
     * @param command the message's command
     * @param body    the message's body
     */
    protected AbstractOutgoingMessage(V command, Map<String, String> body) {
        this.command = command;
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> each : body.entrySet()) {
            stringBuilder.append(each.getKey()).append('=').append(each.getValue()).append(',');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        this.body = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Returns the raw bytes of this message. The message has the following format: "aaa bb cccc…" with aaa = three
     * character command, bb = length of the cccc… block, and cccc… = body of variable length. The body can be empty.
     *
     * @return the message in bytes
     */
    public byte[] getBytes() {
        byte[] header = (command.toString() + ' ' + body.length + ' ').getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[header.length + body.length];
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(body, 0, result, header.length, body.length);
        return result;
    }

    /**
     * Returns the message's command.
     *
     * @return the message's command.
     */
    public V getCommand() {
        return command;
    }

}
