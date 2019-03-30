/*
 * This file is part of ProDisFuzz, modified on 3/24/19 11:15 AM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.protocol;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StateMachineTest {

    private StateMachine stateMachine;

    @BeforeMethod
    public void setUp() {
        stateMachine = new StateMachine();
    }

    @Test
    public void testUpdateWithNew() throws ProtocolStateException {
        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
            fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithMonitorSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
            fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithConnectorSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
            fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithConnectorReady() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
            fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithWatcherSet() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
        } catch (ProtocolStateException ignored) {
            fail();
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
            fail();
        } catch (ProtocolStateException ignored) {
        }
    }

    @Test
    public void testUpdateWithWatcherReady() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
        } catch (ProtocolStateException ignored) {
            fail();
        }
    }

    @Test
    public void testUpdateWithFuzzing() throws ProtocolStateException {
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.RST);
        } catch (ProtocolStateException ignored) {
            fail();
        }
        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
        stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SCP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.GWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.SWP);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);
            fail();
        } catch (ProtocolStateException ignored) {
        }

        try {
            stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);
        } catch (ProtocolStateException ignored) {
            fail();
        }
    }

    @Test
    public void testIsAllowedAtCurrentState() throws ProtocolStateException {
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.AYT);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.SCO);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.TCO);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.SWA);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.TWA);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));

        stateMachine.updateWith(StateMachine.ClientRequestCommand.FUZ);

        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.AYT));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SCP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TCO));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.GWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWA));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.SWP));
        assertFalse(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.TWA));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.FUZ));
        assertTrue(stateMachine.isAllowedAtCurrentState(StateMachine.ClientRequestCommand.RST));
    }
}
