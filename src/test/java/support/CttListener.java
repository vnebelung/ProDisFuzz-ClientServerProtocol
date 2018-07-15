/*
 * This file is part of ProDisFuzz, modified on 15.07.18 20:46.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.ICttListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

import java.util.Map;

public class CttListener implements ICttListener {

    private boolean triggered = false;
    private Map<String, String> outValue;
    private String exception = "";
    private byte[] inValue;

    public CttListener(Map<String, String> outValue) {
        this.outValue = outValue;
    }

    public boolean isTriggered() {
        return triggered;
    }

    @Override
    public Map<String, String> receive(byte... data) throws ProtocolExecutionException {
        inValue = data;
        triggered = true;
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
