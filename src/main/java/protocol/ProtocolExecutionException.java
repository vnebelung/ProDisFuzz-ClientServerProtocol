/*
 * This file is part of ProDisFuzz, modified on 23.09.16 13:29.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

/**
 * Signals that the counterpart of the client or server has answered with an error message.
 */
public class ProtocolExecutionException extends Exception {

    /**
     * Constructs an ProtocolExecutionException with the specified detail message.  A detail message is a String that
     * describes this particular exception.
     *
     * @param string the String that contains a detailed message
     */
    public ProtocolExecutionException(String string) {
        super(string);
    }
}
