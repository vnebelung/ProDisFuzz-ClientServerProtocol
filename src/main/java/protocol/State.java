/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:42 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents a state in the protocol used for communication between the client and the server.
 */
class State {

    private Map<StateMachine.ClientRequestCommand, StateMachine.StateType> transitions;

    /**
     * Constructs a new protocol state.
     */
    State() {
        transitions = new EnumMap<>(StateMachine.ClientRequestCommand.class);
    }

    /**
     * Returns the state that is the active one after calling the given command.
     *
     * @param command the command that is going to be called
     * @return the new protocol state or null, if the command is not allowed to be called at this state
     */
    StateMachine.StateType getNextStateFor(StateMachine.ClientRequestCommand command) {
        return transitions.get(command);
    }

    /**
     * Sets the given pairs of commands and states as the allowed transitions of this state. A command/state pair
     * indicates that the given command shall be allowed in this protocol state and leads to the given state.
     *
     * @param command   the command that is allowed
     * @param stateType the new state the protocol will have after calling the given command
     */
    void addTransition(StateMachine.ClientRequestCommand command, StateMachine.StateType stateType) {
        transitions.put(command, stateType);
    }
}
