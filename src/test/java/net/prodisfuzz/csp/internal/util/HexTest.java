/*
 * This file is part of ProDisFuzz, modified on 15.07.18 21:57.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.util;

import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings({"ZeroLengthArrayAllocation", "HardCodedStringLiteral"})
public class HexTest {

    @Test
    public void testByte2Hex() {
        Assert.assertEquals(Hex.byte2HexBin((byte) 0), "00");
        Assert.assertEquals(Hex.byte2HexBin(new byte[]{0, 127, -128}), "007f80");
    }

    @Test
    public void testHex2Byte() {
        Assert.assertEquals(Hex.hexBin2Byte("007f80"), new byte[]{0, 127, -128});

        Assert.assertEquals(Hex.hexBin2Byte("7F"), new byte[]{127});
        Assert.assertEquals(Hex.hexBin2Byte("007f800"), new byte[]{});
        Assert.assertEquals(Hex.hexBin2Byte("0z"), new byte[]{});

        Assert.assertEquals(Hex.hexBin2Byte("7f"), new byte[]{127});
    }
}
