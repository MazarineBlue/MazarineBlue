/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.functions;

import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.executors.events.GetFunctionRegistryEvent;
import org.mazarineblue.executors.events.SetFunctionRegistryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.IsAbleToProcessInstructionLineEvent;

public class FunctionSubscriber
        extends ReflectionSubscriber<Event> {

    private FunctionRegistry registry;

    public FunctionSubscriber(FunctionRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void eventHandler(IsAbleToProcessInstructionLineEvent event) {
        if (!registry.containsFunction(event.getKeyword()))
            return;
        event.setResult(true);
        event.setConsumed(true);
    }

    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        if (registry.containsFunction(event.getKeyword()))
            registry.getFunction(event.getKeyword()).execute(event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see GetFunctionRegistryEvent
     */
    @EventHandler
    public void eventHandler(GetFunctionRegistryEvent event) {
        event.setFunctionRegistry(registry);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SetFunctionRegistryEvent
     */
    @EventHandler
    public void eventHandler(SetFunctionRegistryEvent event) {
        registry = new FunctionRegistry(event.getFunctionRegistry());
        event.setConsumed(true);
    }
}
