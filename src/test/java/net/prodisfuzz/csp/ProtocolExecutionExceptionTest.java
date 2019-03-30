/*
 * This file is part of ProDisFuzz, modified on 3/30/19 4:19 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp;

import org.testng.annotations.Test;

public class ProtocolExecutionExceptionTest {

    @Test(expectedExceptions = ProtocolExecutionException.class, expectedExceptionsMessageRegExp = "test test")
    public void test() throws ProtocolExecutionException {
        throw new ProtocolExecutionException("test test");

    }

    @Test(expectedExceptions = ProtocolExecutionException.class,
            expectedExceptionsMessageRegExp = "test test 1 test " + "\n")
    public void test1() throws ProtocolExecutionException {
        throw new ProtocolExecutionException("test test %d %s %n", 1, "test");

    }

}
