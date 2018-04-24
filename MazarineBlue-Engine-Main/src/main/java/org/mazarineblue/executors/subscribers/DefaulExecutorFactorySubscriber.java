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
package org.mazarineblue.executors.subscribers;

import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.functions.FunctionRegistry;
import org.mazarineblue.functions.events.GetFunctionRegistryEvent;
import org.mazarineblue.functions.events.SetFunctionRegistryEvent;
import org.mazarineblue.variablestore.VariableStore;
import org.mazarineblue.variablestore.events.GetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetGlobalVariableStoreEvent;

/**
 * A {@code DefaulExecutorFactoryLink} is a link that contains default
 * functionality.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaulExecutorFactorySubscriber
        extends ReflectionSubscriber<Event> {

    private final ExecutorFactory factory;

    public DefaulExecutorFactorySubscriber(ExecutorFactory factory) {
        this.factory = factory;
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CreateFeedExecutorEvent
     */
    @EventHandler
    public void eventHandler(CreateFeedExecutorEvent<Executor> event) {
        Invoker invoker = event.invoker();
        Executor executor = factory.create();
        init(executor, invoker);        
        event.setResult(executor);
        event.setConsumed(true);
    }

    private void init(Executor executor, Invoker invoker) {
        VariableStore store = getGlobalVariableStore(invoker);
        FunctionRegistry registry = getFunctionRegistry(invoker);
        executor.execute(new MemoryFeed(new SetGlobalVariableStoreEvent(store),
                                        new SetFunctionRegistryEvent(registry)));
    }

    private VariableStore getGlobalVariableStore(Invoker invoker) {
        GetGlobalVariableStoreEvent e = new GetGlobalVariableStoreEvent();
        invoker.publish(e);
        return e.getStore();
    }

    private FunctionRegistry getFunctionRegistry(Invoker invoker) {
        GetFunctionRegistryEvent e = new GetFunctionRegistryEvent();
        invoker.publish(e);
        return e.getFunctionRegistry();
    }

    @Override
    public int hashCode() {
        return 41 * 7 + Objects.hashCode(this.factory);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ||  obj != null && getClass() == obj.getClass()
                && Objects.equals(this.factory, ((DefaulExecutorFactorySubscriber) obj).factory);
    }
}
