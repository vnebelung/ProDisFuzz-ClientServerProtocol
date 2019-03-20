/*
 * This file is part of ProDisFuzz, modified on 3/21/19 12:01 AM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

/**
 * Signals that the counterpart of the client or server has answered with an error message.
 */
public class ProtocolExecutionException extends Exception {

    /**
     * Instantiates a protocol execution exception with the specified detail message.  A detail message is a String that
     * describes this particular exception.
     *
     * @param string the String that contains a detailed message
     */
    public ProtocolExecutionException(String string) {
        super(string);
    }

    /**
     * Instantiates a protocol execution exception exception. This exception is thrown by classes that are configurable
     * by the client and receive invalid parameters.
     *
     * @param format A format string
     * @param args   Arguments referenced by the format specifiers in the format string.  If there are more arguments
     *               than format specifiers, the extra arguments are ignored.  The number of arguments is variable and
     *               may be zero.  The maximum number of arguments is limited by the maximum dimension of a Java array
     *               as defined by
     *               <cite>The Java&trade; Virtual Machine Specification</cite>.
     *               The behaviour on a {@code null} argument depends on the conversion.
     * @throws java.util.IllegalFormatException If a format string contains an illegal syntax, a format specifier that
     *                                          is incompatible with the given arguments, insufficient arguments given
     *                                          the format string, or other illegal conditions.  For specification of
     *                                          all possible formatting errors, see the Details section of the formatter
     *                                          class specification.
     */
    public ProtocolExecutionException(String format, Object... args) {
        this(String.format(format, args));
    }
}
