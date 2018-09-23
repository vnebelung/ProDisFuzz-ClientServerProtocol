/*
 * This file is part of ProDisFuzz, modified on 23.09.18 10:10.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.ISwpListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

import java.util.Map;

public class SwpListener implements ISwpListener {

    private boolean triggered = false;
    private Map<String, String> inValue;
    private String exception = "";

    public SwpListener() {
    }

    public boolean isTriggered() {
        return triggered;
    }

    public Map<String, String> getInValue() {
        return inValue;
    }

    @Override
    public void receive(Map<String, String> parameters) throws ProtocolExecutionException {
        triggered = true;
        inValue = parameters;
        if (!exception.isEmpty()) {
            throw new ProtocolExecutionException(exception);
        }
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void reset() {
        triggered = false;
        exception = "";
    }
}
