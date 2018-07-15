/*
 * This file is part of ProDisFuzz, modified on 14.07.18 15:04.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

/**
 * This interface represents a listener for the RST command.
 */
public interface IRstListener {

    /**
     * Receives an RST command. The server must reset all its configurable options to the default values. If the
     * listener cannot process the command for whatever reason it must throw an ProtocolExecutionException.
     *
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    void receive() throws ProtocolExecutionException;
}
