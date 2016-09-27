/*
 * This file is part of ProDisFuzz, modified on 27.09.16 22:16.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

import message.server.IncomingMessage;
import protocol.ProtocolStateException;

import java.io.IOException;

public interface ServerObserver {

    /**
     * Handles a client's message and sends the according response to the client.
     *
     * @param message the received message
     * @throws IOException            if an I/O error occurs
     * @throws ProtocolStateException if the protocol enters an invalid state
     */
    void handleMessage(IncomingMessage message);
}
