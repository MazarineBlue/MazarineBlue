/*
 * Copyright (c) 2012 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus.events;

import org.mazarineblue.eventbus.Event;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractEvent
        implements Event {

    private boolean consumed = false;

    @Override
    public String toString() {
        return "consumed=" + isConsumed();
    }

    @Override
    public Status status() {
        return Status.OK;
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String responce() {
        return "";
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }

    @Override
    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }
}
