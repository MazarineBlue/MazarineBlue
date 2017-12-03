/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus.link;

import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.SimpleEventService;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;

/**
 * A {@code EventBusLink} is a {@code Link} that can publish {@code Events} to
 * registered {@code Subscribers}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see SubscribeEvent
 * @see UnsubscribeEvent
 */
public class EventBusLink
        extends Link
        implements EventService<Event> {

    private final EventService<Event> service;

    /**
     * Creates a EventBusLink, with the ability to add other subscribers though the use of events.
     *
     * @see EventBusLinkSubscriber
     * @see SubscriberEvent
     * @see UnsubscribeEvent
     */
    public EventBusLink() {
        service = new SimpleEventService<>();
    }

    /**
     * Creates a EventBusLink, initializes with the given subscriber.
     *
     * @param eventType  the type of events to listen to
     * @param filter     the filter to apply
     * @param subscriber the subscriber to register
     */
    public EventBusLink(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        service = new SimpleEventService<>();
        service.subscribe(eventType, filter, subscriber);
    }

    @Override
    public String toString() {
        return service.toString();
    }

    @Override
    public boolean subscribe(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        return service.subscribe(eventType, filter, subscriber);
    }

    @Override
    public boolean unsubscribe(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        return service.unsubscribe(eventType, filter, subscriber);
    }

    @Override
    public void publish(Event e) {
        eventHandler(e);
    }

    @Override
    protected void uncatchedEventHandler(Event e) {
        service.publish(e);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SubscribeEvent
     */
    @EventHandler
    public void eventHandler(SubscribeEvent event) {
        service.subscribe(event.getType(), event.getFilter(), event.getSubscriber());
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see UnsubscribeEvent
     */
    @EventHandler
    public void eventHandler(UnsubscribeEvent event) {
        service.unsubscribe(event.getType(), event.getFilter(), event.getSubscriber());
        event.setConsumed(true);
    }

    @Override
    public int hashCode() {
        return 3 * 67
                + Objects.hashCode(service);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.service, ((EventBusLink) obj).service);
    }
}
