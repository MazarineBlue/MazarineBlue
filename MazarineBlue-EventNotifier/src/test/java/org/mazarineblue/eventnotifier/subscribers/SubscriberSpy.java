/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventnotifier.subscribers;

import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.util.TestList;
import org.mazarineblue.utilities.ID;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@SuppressWarnings("serial")
public class SubscriberSpy<E extends Event>
        implements Subscriber<E> {

    private final Predicate<Event> condition;
    private final ID id = new ID();
    private final TestList<Event> list = new TestList<>();

    /**
     * Records all events.
     */
    public SubscriberSpy() {
        this.condition = t -> true;
    }

    /**
     * Records all events that match the specified condition.
     *
     * @param condition records the event, if it evaluates {@code true} using
     *                  this condition.
     */
    public SubscriberSpy(Predicate<Event> condition) {
        this.condition = condition;
    }

    @Override
    public void eventHandler(E event) {
        if (condition.test(event))
            list.add(event);
    }

    public void assertEvents(Event... events) {
        list.assertObjects(events);
    }

    public void assertClasses(Class<?>... expectedTypes) {
        list.assertClasses(expectedTypes);
    }

    public int receivedEvents() {
        return list.size();
    }

    @Override
    public int hashCode() {
        return 7 * 29
                + Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.id, ((SubscriberSpy) obj).id);
    }
}
