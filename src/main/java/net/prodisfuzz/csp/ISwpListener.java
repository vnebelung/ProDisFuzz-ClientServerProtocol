/*
 * This file is part of ProDisFuzz, modified on 22.09.18 01:18.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import java.util.Map;

/**
 * This interface represents a listener for the SWP command.
 */
public interface ISwpListener {

    /**
     * Receives an SWP command. The server must use the given connector parameters in the following fuzzing process. If
     * the client did not send any parameters, the given parameters will be empty. If the listener cannot process the
     * command for whatever reason it must throw an ProtocolExecutionException.
     *
     * @param parameters the connector parameters the server must use
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    void receive(Map<String, String> parameters) throws ProtocolExecutionException;
}
