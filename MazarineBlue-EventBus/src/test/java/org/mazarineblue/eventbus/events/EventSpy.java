/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus.events;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Event.Status;
import static org.mazarineblue.eventbus.Event.Status.OK;
import org.mazarineblue.utililities.SerializableClonable;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@SuppressWarnings("serial")
public class EventSpy
        implements Event {

    private int count;

    public int getCount() {
        return count;
    }

    @Override
    public boolean isConsumed() {
        ++count;
        return false;
    }

    @Override
    public String message() {
        ++count;
        return "";
    }

    @Override
    public String responce() {
        ++count;
        return "";
    }

    @Override
    public Status status() {
        ++count;
        return OK;
    }

    @Override
    public void setConsumed(boolean consumed) {
        ++count;
    }

    @Override
    public boolean isAutoConsumable() {
        return false;
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T src) {
    }
}
