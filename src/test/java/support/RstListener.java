/*
 * This file is part of ProDisFuzz, modified on 15.07.18 16:32.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import net.prodisfuzz.csp.IRstListener;
import net.prodisfuzz.csp.ProtocolExecutionException;

public class RstListener implements IRstListener {

    private boolean triggered = false;
    private String exception = "";

    public RstListener() {
    }

    @Override
    public void receive() throws ProtocolExecutionException {
        triggered = true;
        if (!exception.isEmpty()) {
            throw new ProtocolExecutionException(exception);
        }
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void reset() {
        triggered = false;
        exception = "";
    }
}
