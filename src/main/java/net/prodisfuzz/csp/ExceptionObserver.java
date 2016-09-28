/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:36.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import net.prodisfuzz.csp.packet.ProtocolFormatException;
import net.prodisfuzz.csp.protocol.ProtocolExecutionException;
import net.prodisfuzz.csp.protocol.ProtocolStateException;

public interface ExceptionObserver {

    /**
     * Propagates a protocol exception.
     *
     * @param e the protocol state exception
     */
    void propagate(ProtocolStateException e);

    /**
     * Propagates a protocol exception.
     *
     * @param e the protocol execution exception
     */
    void propagate(ProtocolExecutionException e);

    /**
     * Propagates a protocol exception.
     *
     * @param e the protocol state exception
     */
    void propagate(ProtocolFormatException e);
}
