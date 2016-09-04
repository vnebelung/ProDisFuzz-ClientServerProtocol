/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:45 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StateTest {

    private State state;

    @BeforeMethod
    public void setUp() throws Exception {
        state = new State();
        state.addTransition(StateMachine.ClientRequestCommand.RST, StateMachine.StateType.NEW);
        state.addTransition(StateMachine.ClientRequestCommand.CTF, StateMachine.StateType.CONNECTOR_READY);
    }

    @Test
    public void testGetNextStateFor() {
        Assert.assertEquals(state.getNextStateFor(StateMachine.ClientRequestCommand.RST), StateMachine.StateType.NEW);
        Assert.assertEquals(state.getNextStateFor(StateMachine.ClientRequestCommand.CTF),
                StateMachine.StateType.CONNECTOR_READY);
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.CTT));
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.AYT));
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SCO));
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SCP));
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.SWA));
        Assert.assertNull(state.getNextStateFor(StateMachine.ClientRequestCommand.GWA));
    }

    @SuppressWarnings("EmptyMethod")
    @Test
    public void testAddTransition() throws Exception {
        // See setUp() / testGetNextStateFor()
    }

}
