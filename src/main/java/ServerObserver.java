/*
 * This file is part of ProDisFuzz, modified on 28.09.16 22:46.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

import message.server.IncomingMessage;

/**
 * This interface represents an observer of the server. The observer will be notified when the server receives a
 * packet that must be processed.
 */
public interface ServerObserver {

    /**
     * Handles a client's message and sends the according response to the client.
     *
     * @param message the received message
     */
    void handleMessage(IncomingMessage message);
}
