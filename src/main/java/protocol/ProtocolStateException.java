/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:42 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

/**
 * Signals that a method has been invoked at an illegal protocol state.
 */
class ProtocolStateException extends Exception {

    /**
     * Constructs an ProtocolStateException with the specified detail message.  A detail message is a String that
     * describes this particular exception.
     *
     * @param string the String that contains a detailed message
     */
    ProtocolStateException(String string) {
        super(string);
    }

}
