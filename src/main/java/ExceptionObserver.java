/*
 * This file is part of ProDisFuzz, modified on 27.09.16 22:16.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

import packet.ProtocolFormatException;
import protocol.ProtocolExecutionException;
import protocol.ProtocolStateException;

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
