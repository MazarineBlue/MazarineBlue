/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.logger.events;

import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * A {@code EngineLoggerEvent} is the base {@link Event} of all events in the
 * logger component.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLoggerEvent<T>
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private transient T listener;

    EngineLoggerEvent(T listener) {
        this.listener = listener;
    }

    public T getListener() {
        return listener;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        listener = (T) ((EngineLoggerEvent<?>) other).listener;
    }
}
