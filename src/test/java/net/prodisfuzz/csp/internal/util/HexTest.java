/*
 * This file is part of ProDisFuzz, modified on 3/24/19 11:15 AM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"ZeroLengthArrayAllocation", "HardCodedStringLiteral"})
public class HexTest {

    @Test
    public void testByte2Hex() {
        assertEquals(Hex.byte2HexBin((byte) 0), "00");
        assertEquals(Hex.byte2HexBin(new byte[]{0, 127, -128}), "007f80");
    }

    @Test
    public void testHex2Byte() {
        assertEquals(Hex.hexBin2Byte("007f80"), new byte[]{0, 127, -128});

        assertEquals(Hex.hexBin2Byte("7F"), new byte[]{127});
        assertEquals(Hex.hexBin2Byte("007f800"), new byte[]{});
        assertEquals(Hex.hexBin2Byte("0z"), new byte[]{});

        assertEquals(Hex.hexBin2Byte("7f"), new byte[]{127});
    }
}
