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
package org.mazarineblue.keyworddriven;

import org.mazarineblue.eventdriven.AbstractFeed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

/**
 * A {@code AbstractInstructionFeed} is a {@code Feed}, which provides a
 * default implementation for instruction based feeds. The
 * {@code AbstractInstructionFeed} provides a
 * {@link #createEvent(String, Object...) createEvent}
 * method that calls
 * {@link #createExecuteInstructionLineEvent(String, Object...)}, which by
 * default creates {@code ExecuteInstructionLineEvent}, until an exception was
 * thrown and then calls
 * {@link #createValidateInstructionLineEvent(String, Object...)}, which by
 * default creates {@code ValidateInstructionLineEvent}, until the
 * {@link #reset()} method was called.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractInstructionFeed
        extends AbstractFeed {

    private enum Mode {

        EXECUTING,
        VALIDATING,
    }

    private Mode mode = Mode.EXECUTING;

    @Override
    public String toString() {
        return "mode=" + mode;
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ExecuteInstructionLineEvent
     */
    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        if (event.hasException())
            mode = Mode.VALIDATING;
    }

    /**
     * Creates a {@code ExecuteInstructionLineEvent} or a
     * {@code ValidateInstructionLineEvent}, depending on the mode of the feed.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     * @return the created event.
     */
    protected Event createEvent(String path, Object... arguments) {
        switch (mode) {
            case EXECUTING:
                return createExecuteInstructionLineEvent(path, arguments);
            case VALIDATING:
                return createValidateInstructionLineEvent(path, arguments);
            default:
                throw new NeverThrownException();
        }
    }

    /**
     * Creates a {@code ExecuteInstructionLineEvent} using the specified path and arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     * @return the created event.
     */
    protected ExecuteInstructionLineEvent createExecuteInstructionLineEvent(String path, Object... arguments) {
        return new ExecuteInstructionLineEvent(path, arguments);
    }

    /**
     * Creates a {@code ValidateInstructionLineEvent} using the specified path and arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     * @return the created event.
     */
    protected ValidateInstructionLineEvent createValidateInstructionLineEvent(String path, Object... arguments) {
        return new ValidateInstructionLineEvent(path, arguments);
    }

    @Override
    public void reset() {
        mode = Mode.EXECUTING;
    }
}
