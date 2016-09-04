/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:45 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package protocol;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents the state machine used to track the state of the protocol used for communication between the
 * client and the server.
 */
public class StateMachine {

    private StateType currentStateType;
    private Map<StateType, State> states;

    /**
     * Constructs a new state machine which controls the protocol used to connect a monitor server and client.
     */
    public StateMachine() {
        states = new EnumMap<>(StateType.class);

        State state = new State();
        state.addTransition(ClientRequestCommand.AYT, StateType.MONITOR_SET);
        states.put(StateType.NEW, state);

        state = new State();
        state.addTransition(ClientRequestCommand.SCO, StateType.CONNECTOR_SET);
        state.addTransition(ClientRequestCommand.RST, StateType.NEW);
        state.addTransition(ClientRequestCommand.GCO, StateType.MONITOR_SET);
        states.put(StateType.MONITOR_SET, state);

        state = new State();
        state.addTransition(ClientRequestCommand.SCP, StateType.CONNECTOR_SET);
        state.addTransition(ClientRequestCommand.SCO, StateType.CONNECTOR_SET);
        state.addTransition(ClientRequestCommand.CTT, StateType.CONNECTOR_READY);
        state.addTransition(ClientRequestCommand.RST, StateType.NEW);
        states.put(StateType.CONNECTOR_SET, state);

        state = new State();
        state.addTransition(ClientRequestCommand.SCP, StateType.CONNECTOR_SET);
        state.addTransition(ClientRequestCommand.SCO, StateType.CONNECTOR_SET);
        state.addTransition(ClientRequestCommand.SWA, StateType.WATCHER_SET);
        state.addTransition(ClientRequestCommand.GWA, StateType.CONNECTOR_READY);
        state.addTransition(ClientRequestCommand.RST, StateType.NEW);
        states.put(StateType.CONNECTOR_READY, state);

        state = new State();
        state.addTransition(ClientRequestCommand.SWA, StateType.WATCHER_SET);
        state.addTransition(ClientRequestCommand.CTF, StateType.FUZZING);
        state.addTransition(ClientRequestCommand.RST, StateType.NEW);
        states.put(StateType.WATCHER_SET, state);

        state = new State();
        state.addTransition(ClientRequestCommand.CTF, StateType.FUZZING);
        state.addTransition(ClientRequestCommand.RST, StateType.NEW);
        states.put(StateType.FUZZING, state);

        currentStateType = StateType.NEW;
    }

    /**
     * Updates the state of the connection by indicating that the given command is going to be called. To successfully
     * update the protocol state the given command must be allowed to be called at the current state, otherwise an
     * exception is thrown.
     *
     * @param command the command that is going to be called at caller's side
     * @throws ProtocolStateException if the given command is not allowed to be executed at the current protocol state
     */
    void updateWith(ClientRequestCommand command) throws ProtocolStateException {
        StateType stateType = states.get(currentStateType).getNextStateFor(command);
        if (stateType == null) {
            throw new ProtocolStateException(
                    "Protocol error: Command '" + command + "' not allowed in state '" + currentStateType + '\'');
        }
        currentStateType = stateType;
    }

    /**
     * Checks whether the given command is allowed at the current state.
     *
     * @param command the command to check
     * @return true, if the command is allowed at the current state
     */
    boolean isAllowedAtCurrentState(ClientRequestCommand command) {
        return states.get(currentStateType).getNextStateFor(command) != null;
    }

    enum StateType {NEW, MONITOR_SET, CONNECTOR_SET, CONNECTOR_READY, WATCHER_SET, FUZZING}

    public enum ServerAnswerCommand {ROK, ERR}

    public enum ClientRequestCommand {AYT, RST, GCO, SCO, SCP, CTT, GWA, SWA, CTF}

}
