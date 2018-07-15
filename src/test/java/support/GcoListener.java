/*
 * This file is part of ProDisFuzz, modified on 15.07.18 16:32.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.IGcoListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

import java.util.Set;

public class GcoListener implements IGcoListener {

    private boolean triggered = false;
    private Set<String> outValue;
    private String exception = "";

    public GcoListener(Set<String> outValue) {
        this.outValue = outValue;
    }

    public boolean isTriggered() {
        return triggered;
    }

    @Override
    public Set<String> receive() throws ProtocolExecutionException {
        triggered = true;
        if (!exception.isEmpty()) {
            throw new ProtocolExecutionException(exception);
        }
        return outValue;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void reset() {
        triggered = false;
        exception = "";
    }
}
