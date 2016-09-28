/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:36.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.protocol;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StateMachineTest {

    private StateMachine stateMachine;

    @BeforeMethod
    public void setUp() throws Exception {
        stateMachine = new StateMachine();
    }

    @Test
    public void testUpdateWithNew() throws ProtocolStateException {
        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithMonitorSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithConnectorSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithConnectorReady() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithWatcherSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testUpdateWithFuzzing() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            Assert.fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);
        } catch (ProtocolStateException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testIsAllowedAtCurrentState() throws ProtocolStateException {
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTT);

        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.CTF);

        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTF));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.CTT));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        Assert.assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        Assert.assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));
    }
}
