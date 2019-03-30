/*
 * This file is part of ProDisFuzz, modified on 3/24/19 7:55 PM.
 * Copyright (c) 2013-2019 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.internal.protocol;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class StateTest {

    @Test
    public void testGetNextStateFor() {
        State state = new State();
        state.addTransition(StateMachine.ClientRequestCommand.RST, StateMachine.StateType.NEW);
        state.addTransition(StateMachine.ClientRequestCommand.FUZ, StateMachine.StateType.CONNECTOR_READY);

        assertEquals(state.getNextStateFor(StateMachine.ClientRequestCommand.RST), StateMachine.StateType.NEW);
        assertEquals(state.getNextStateFor(StateMachine.ClientRequestCommand.FUZ),
                StateMachine.StateType.CONNECTOR_READY);
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.TCO));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.AYT));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SCO));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SCP));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SWA));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.GWA));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SWP));
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.TWA));
    }

    @SuppressWarnings("EmptyMethod")
    @Test
    public void testAddTransition() {
        State state = new State();
        assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.RST));

        state.addTransition(StateMachine.ClientRequestCommand.RST, StateMachine.StateType.WATCHER_SET);
        assertEquals(state.getNextStateFor(StateMachine.ClientRequestCommand.RST), StateMachine.StateType.WATCHER_SET);
    }

}
