/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventdriven.events;

import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.AbstractEvent;

/**
 * An {@code ExceptionThrownEvent} is fired by an {@link Interpreter} when it
 * encounters an exception during publishing an {@link Event}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionThrownEvent
        extends AbstractEvent {

    private final Event cause;
    private final RuntimeException ex;

    /**
     * Constructs an {@code ExceptionThrownEvent} to indicate an exception was
     * thrown during publishing.
     *
     * @param cause the event that caused the exception to be thrown.
     * @param ex    the exception that was thrown.
     */
    public ExceptionThrownEvent(Event cause, RuntimeException ex) {
        this.cause = cause;
        this.ex = ex;
    }

    @Override
    public String toString() {
        return "cause=" + cause.getClass().getName() + ", message=" + ex.getMessage() + ", " + super.toString();
    }

    @Override
    public String message() {
        return "error=" + ex.getMessage();
    }

    public Event getCause() {
        return cause;
    }

    public RuntimeException getException() {
        return ex;
    }

    @Override
    public int hashCode() {
        return 7 * 19 * 19
                + 19 * Objects.hashCode(cause)
                + Objects.hashCode(ex);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.cause, ((ExceptionThrownEvent) obj).cause)
                && Objects.equals(this.ex, ((ExceptionThrownEvent) obj).ex);
    }
}
