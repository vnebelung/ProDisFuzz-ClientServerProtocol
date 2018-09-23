/*
 * This file is part of ProDisFuzz, modified on 23.09.18 10:10.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.IFuzListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

import java.util.Map;

public class FuzListener implements IFuzListener {

    private boolean triggered = false;
    private Map<String, String> outValue;
    private String exception = "";
    private byte[] inValue;

    public FuzListener(Map<String, String> outValue) {
        this.outValue = outValue;
    }

    public boolean isTriggered() {
        return triggered;
    }

    @Override
    public Map<String, String> receive(byte... data) throws ProtocolExecutionException {
        triggered = true;
        inValue = data;
        if (!exception.isEmpty()) {
            throw new ProtocolExecutionException(exception);
        }
        return outValue;
    }

    public byte[] getInValue() {
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
