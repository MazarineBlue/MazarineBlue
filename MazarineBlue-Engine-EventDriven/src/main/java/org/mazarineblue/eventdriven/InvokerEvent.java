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
package org.mazarineblue.eventdriven;

import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.Status.ERROR;
import static org.mazarineblue.eventnotifier.Event.Status.OK;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.events.AbstractEvent;

/**
 * An {@code InvokerEvent} is an {@link Event} that, when passed to a
 * {@link Subscriber}, contains an reference to an {@link Invoker}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Invoker
 * @see Processor
 */
public abstract class InvokerEvent
        extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private transient Invoker invoker;
    private RuntimeException exception;

    public Invoker invoker() {
        return invoker;
    }

    void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Status status() {
        return exception == null ? OK : ERROR;
    }

    /**
     * Returns true if an exception was thrown during the publishing of an
     * {@link Event Event}.
     *
     * @return true this event holds an exception.
     */
    public boolean hasException() {
        return exception != null;
    }

    public RuntimeException getException() {
        return exception;
    }

    protected void setException(RuntimeException exception) {
        this.exception = exception;
    }
}
