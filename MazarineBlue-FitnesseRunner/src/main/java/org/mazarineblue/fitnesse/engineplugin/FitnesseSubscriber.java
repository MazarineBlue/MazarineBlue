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
package org.mazarineblue.fitnesse.engineplugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.fitnesse.events.FitnesseEvent;
import org.mazarineblue.fitnesse.events.FixtureEvent;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.fitnesse.events.PathEvent;
import org.mazarineblue.fitnesse.events.PathFitnesseEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.VariableStoreEvent;

/**
 * A {@code FitnesseSubscirber} processes events by converting a
 * {@link FitnesseEvent} into {@link VariableStoreEvent} or
 * {@link FixtureEvent} and sending those to the {@link Interpreter}, passed in
 * the event.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see InvokerEvent#getInterpreter()
 */
public class FitnesseSubscriber
        extends ReflectionSubscriber<Event> {

    private final Map<String, String> instancesSeen = new HashMap<>(4);

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see PathFitnesseEvent
     */
    @EventHandler
    public void eventHandler(PathFitnesseEvent event) {
        event.getInvoker().publish(new PathEvent(event.getPath()));
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CreateFitnesseEvent
     */
    @EventHandler
    public void eventHandler(CreateFitnesseEvent event) {
        if (isInstanceFirstTime(event.getInstance()) || isRepacementCalledFor(event))
            createFixture(event);
        event.setConsumed(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper method of CreateFitnesseEvent">
    private boolean isInstanceFirstTime(String instance) {
        return !instancesSeen.containsKey(instance);
    }

    private static boolean isRepacementCalledFor(CreateFitnesseEvent event) {
        return event.getFixture() != null;
    }

    private void createFixture(CreateFitnesseEvent event) {
        instancesSeen.put(event.getInstance(), event.getFixture());
        event.getInvoker().publish(new NewInstanceEvent(event.getInstance(), event.getFixture(), event.getArguments()));
    }
    // </editor-fold>

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CallFitnesseEvent
     */
    @EventHandler
    public void eventHandler(CallFitnesseEvent event) {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent(event.getMethod(), event.getArguments());
        event.getInvoker().publish(e);
        event.setResult(e.getResult());
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see AssignFitnesseEvent
     */
    @EventHandler
    public void eventHandler(AssignFitnesseEvent event) {
        event.getInvoker().publish(new AssignVariableEvent(event.getSymbol(), event.getValue()));
        event.setConsumed(true);
    }

    @Override
    public Class<?> getBaseEvent() {
        return FitnesseEvent.class;
    }

    @Override
    public int hashCode() {
        return 5 * 61
                + Objects.hashCode(this.instancesSeen.keySet());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.instancesSeen, ((FitnesseSubscriber) obj).instancesSeen);
    }
}
