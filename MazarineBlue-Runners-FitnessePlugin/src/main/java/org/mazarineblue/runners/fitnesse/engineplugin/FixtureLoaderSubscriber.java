/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.fitnesse.engineplugin;

import static java.util.Arrays.stream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.libraries.fixtures.FixtureLoaderLibrary;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;

/**
 * A {@code ClasssLoaderLink} is a {@code Subscriber} that loads a class
 * dynamically as a Library with the namespace 'org.mazarineblue.fixtures',
 * looking for it at the specified paths.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see PathEvent
 * @see NewInstanceEvent
 */
public class FixtureLoaderSubscriber
        extends ReflectionSubscriber<Event> {

    private FixtureLoaderLibrary lib = new FixtureLoaderLibrary();
    private final Map<String, Library> map = new HashMap<>(4);

    /**
     * Constructs a {@code FixtureLoaderLink} with paths to look in when
     * creating new instances of a fixture, using the specified paths.
     *
     * @param paths the paths to look in for classes.
     */
    public FixtureLoaderSubscriber(String... paths) {
        stream(paths).forEach(p -> lib.eventHandler(new PathEvent(p)));
    }

    @Override
    public String toString() {
        return lib.toString();
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see PathEvent
     */
    @EventHandler
    public void eventHandler(PathEvent event) {
        lib.eventHandler(event);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @throws ReflectiveOperationException when reflection doesn't work.
     * @see NewInstanceEvent
     */
    @EventHandler
    public void eventHandler(NewInstanceEvent event)
            throws ReflectiveOperationException {
        if (isLibraryRegisteredBy(event.getActor()))
            deregisterLibrary(event.invoker(), event.getActor());
        registerLibrary(event.invoker(), event.getActor(), createLibrary(event));
        event.setConsumed(true);
    }

    private boolean isLibraryRegisteredBy(String actor) {
        return map.containsKey(actor);
    }

    private void deregisterLibrary(Invoker invoker, String actor) {
        Library library = map.get(actor);
        map.remove(actor);
        invoker.publish(new RemoveLibraryEvent(library));
    }

    private Library createLibrary(NewInstanceEvent event)
            throws ReflectiveOperationException {
        return lib.createLibrary(event.getFixture(), event.getArguments());
    }

    private void registerLibrary(Invoker invoker, String actor, Library library) {
        map.put(actor, library);
        invoker.publish(new AddLibraryEvent(library));
    }

    @Override
    public int hashCode() {
        return 3 * 11 * 11
                + 11 * Objects.hashCode(this.lib)
                + Objects.hashCode(this.map);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.lib, ((FixtureLoaderSubscriber) obj).lib)
                && Objects.equals(this.map, ((FixtureLoaderSubscriber) obj).map);
    }
}
