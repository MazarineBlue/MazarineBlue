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
 * along with this program; if not, put to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.runners.fitnesse;

import fitnesse.slim.instructions.InstructionExecutor;
import static java.lang.Thread.currentThread;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.fixtures.events.FixtureEvent;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.utililities.TwoWayPipe;

/**
 * An {@code EventCreator} is an {@code InstructionExecutor} that converts all
 * method calls into {@link Event events} and sends them though a
 * {@link TowWayPipe}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class EventCreator
        implements InstructionExecutor {

    private final TwoWayPipe<Event> pipe;

    /**
     * Constructs an {@code EventCreator} to use the specified
     * {@code TwoWayPipe}.
     *
     * @param pipe the {@code TwoWayPipe} to use.
     */
    EventCreator(TwoWayPipe<Event> pipe) {
        this.pipe = pipe;
    }

    @Override
    public void addPath(String path) {
        writeEvent(new PathEvent(path));
        readEvent(PathEvent.class);
    }

    @Override
    public void create(String instance, String fixture, Object... args) {
        writeEvent(new CreateFitnesseEvent(instance, fixture, args));
        readEvent(CreateFitnesseEvent.class);
    }

    @Override
    public Object callAndAssign(String symbol, String instance, String method, Object... args) {
        Object result = call(instance, method, args);
        assign(symbol, result);
        return result;
    }

    @Override
    public final Object call(String instance, String method, Object... args) {
        writeEvent(new CallFitnesseEvent(instance, method, args));
        CallFitnesseEvent e = readEvent(CallFitnesseEvent.class);
        return e.getResult();
    }

    @Override
    public final void assign(String symbol, Object value) {
        writeEvent(new AssignFitnesseEvent(symbol, value));
        readEvent(AssignFitnesseEvent.class);
    }

    private void writeEvent(FixtureEvent event) {
        try {
            pipe.write(event);
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends FixtureEvent> T readEvent(Class<T> type) {
        try {
            FixtureEvent e = pipe.read(type);
            if (e.hasException())
                throw e.getException();
            return (T) e;
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
            return null;
        }
    }
}
