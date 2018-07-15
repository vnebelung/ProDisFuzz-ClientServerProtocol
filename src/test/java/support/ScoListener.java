/*
 * This file is part of ProDisFuzz, modified on 15.07.18 18:21.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.IScoListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

public class ScoListener implements IScoListener {

    private boolean triggered = false;
    private String inValue;
    private String exception = "";

    public ScoListener() {
    }

    public boolean isTriggered() {
        return triggered;
    }

    @Override
    public void receive(String connector) throws ProtocolExecutionException {
        triggered = true;
        inValue = connector;
        if (!exception.isEmpty()) {
            throw new ProtocolExecutionException(exception);
        }
    }

    public String getInValue() {
        return inValue;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void reset() {
        triggered = false;
        exception = "";
    }
}
