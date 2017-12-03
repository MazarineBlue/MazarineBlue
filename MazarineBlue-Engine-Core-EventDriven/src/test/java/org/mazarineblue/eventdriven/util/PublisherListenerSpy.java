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
package org.mazarineblue.eventdriven.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.listeners.PublisherListener;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PublisherListenerSpy
        implements PublisherListener {

    private final List<Event> startEvents = new ArrayList<>();
    private final List<Exception> exceptions = new ArrayList<>();
    private final List<Event> endEvents = new ArrayList<>();
    private final Predicate<Event> recordEvents;

    public PublisherListenerSpy() {
        this(e -> true);
    }

    public PublisherListenerSpy(Predicate<Event> recordEvents) {
        this.recordEvents = recordEvents;
    }

    @Override
    public String toString() {
        return "events{start=" + startEvents.size() + ", exceptions=" + exceptions.size() + ", end=" + endEvents.size() + '}';
    }

    @Override
    public void startPublishingEvent(Event event) {
        if (recordEvents.test(event))
            startEvents.add(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        exceptions.add(ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        if (recordEvents.test(event))
            endEvents.add(event);
    }

    public int countStartEvents() {
        return startEvents.size();
    }

    public int countExceptions() {
        return exceptions.size();
    }

    public int countEndEvents() {
        return endEvents.size();
    }

    public List<Class<?>> getStartEventTypes() {
        return startEvents.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());
    }

    public List<Class<?>> getExceptionTypes() {
        return exceptions.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());
    }

    public List<Class<?>> getEndEventTypes() {
        return endEvents.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());
    }
}
