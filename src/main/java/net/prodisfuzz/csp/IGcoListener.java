/*
 * This file is part of ProDisFuzz, modified on 14.07.18 16:34.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import java.util.Set;

/**
 * This interface represents a listener for the RST command.
 */
public interface IGcoListener {

    /**
     * Receives an GCO command and returns the server's available connectors. If the listener cannot process the command
     * for whatever reason it must throw an ProtocolExecutionException.
     *
     * @return the server's available connectors
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    Set<String> receive() throws ProtocolExecutionException;
}
