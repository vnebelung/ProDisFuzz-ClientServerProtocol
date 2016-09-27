/*
 * This file is part of ProDisFuzz, modified on 27.09.16 22:16.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package packet;

/**
 * Signals that a protocol message has an invalid format.
 */
public class ProtocolFormatException extends Exception {

    /**
     * Constructs an ProtocolFormatException with the specified detail message.  A detail message is a String that
     * describes this particular exception.
     *
     * @param string the String that contains a detailed message
     */
    public ProtocolFormatException(String string) {
        super("Protocol format error: " + string);
    }
}
