/*
 * This file is part of ProDisFuzz, modified on 15.07.18 22:20.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

/**
 * This interface represents a listener for the SWA command.
 */
public interface ISwaListener {

    /**
     * Receives an SWA command. The server must use the given watcher in the following fuzzing process. If the client
     * did not send such a key "watcher", the given watcher will be empty. If the listener cannot process the command
     * for whatever reason it must throw an ProtocolExecutionException.
     *
     * @param watcher the watcher the client has chosen
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    void receive(String watcher) throws ProtocolExecutionException;
}
