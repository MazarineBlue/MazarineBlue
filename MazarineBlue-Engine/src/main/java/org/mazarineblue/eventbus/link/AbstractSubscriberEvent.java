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
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventbus.events.AbstractEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
abstract class AbstractSubscriberEvent
        extends AbstractEvent {

    private Class<?> type = Event.class;
    private Filter<Event> filter = null;
    private final Subscriber<Event> subscriber;

    protected AbstractSubscriberEvent(Subscriber<Event> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public String message() {
        return toString();
    }

    @Override
    public String toString() {
        return "type=" + typeToString() + ", filter={" + filterToString() + "}, subscriber={" + subscriber + '}';
    }

    private String typeToString() {
        return type.getSimpleName();
    }

    private String filterToString() {
        return String.valueOf(filter);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Filter<Event> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Event> filter) {
        this.filter = filter;
    }

    public Subscriber<Event> getSubscriber() {
        return subscriber;
    }

    @Override
    public int hashCode() {
        return 37 * 37 * 37
                + 37 * 37 * Objects.hashCode(type)
                + 37 * Objects.hashCode(filter)
                + Objects.hashCode(subscriber);
    }

    @Override
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.type, ((AbstractSubscriberEvent) obj).type)
                && Objects.equals(this.filter, ((AbstractSubscriberEvent) obj).filter)
                && Objects.equals(this.subscriber, ((AbstractSubscriberEvent) obj).subscriber);
    }
}
