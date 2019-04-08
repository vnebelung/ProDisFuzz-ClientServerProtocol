/*
 * This file is part of ProDisFuzz, modified on 4/8/19 7:41 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import java.util.Map;

/**
 * This interface represents a listener for the FUZ command.
 */
public interface IFuzListener {

    /**
     * Receives an FUZ command and returns the data the server did collect during the execution. If the listener cannot
     * process the command for whatever reason it must throw an ProtocolExecutionException.
     *
     * @param data the data that is used as input for the fuzzing target
     * @return the data that was returned by the fuzzing target
     * @throws ProtocolExecutionException if the listener could not process the command
     */
    Map<String, String> receive(byte... data) throws ProtocolExecutionException;
}
