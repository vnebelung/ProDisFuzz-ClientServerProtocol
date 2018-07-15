/*
 * This file is part of ProDisFuzz, modified on 14.07.18 15:12.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

/**
 * This interface represents a listener for the SCO command.
 */
public interface IScoListener {

    /**
     * Receives an SCO command. The server must use the given connector in the following fuzzing process. If the client
     * did not send such a key "connector", the given connector will be empty. If the listener cannot process the
     * command for whatever reason it must throw an ProtocolExecutionException.
     *
     * @param connector the connector the client has chosen
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    void receive(String connector) throws ProtocolExecutionException;
}
