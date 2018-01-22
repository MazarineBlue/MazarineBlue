/*
 * Copyright (c) 2012 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventnotifier;

import static java.util.Arrays.stream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import static org.mazarineblue.eventnotifier.EventServiceUtil.checkSubscriber;
import org.mazarineblue.eventnotifier.exceptions.SubscriberAlreadyRegisteredException;
import org.mazarineblue.eventnotifier.exceptions.SubscriberNotRegisteredException;

/**
 * A {@code SimpleEventService} is publisher of {@link Event Events} to its
 * {@link Subscriber Subscribers}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <E> the type of events this service publish.
 */
public class SimpleEventService<E extends Event>
        implements EventService<E> {

    private final Set<Subscriber<E>> subscriptions;

    /**
     * Constructs a {@code SimpleEventService} that publishes all
     * {@link Event Events}.
     * @param subscribers initialise the service with these subscribers.
     */
    public SimpleEventService(Subscriber<E>... subscribers) {
        subscriptions = new HashSet<>();
        stream(subscribers).forEach(this::subscribe);
    }

    @Override
    public String toString() {
        return "size = " + subscriptions.size();
    }

    @Override
    public void publish(E event) {
        for (Subscriber<E> subscriber : subscriptions) {
            subscriber.eventHandler(event);
            if (event.isConsumed())
                return;
        }
    }

    @Override
    @SuppressWarnings("AssignmentToMethodParameter")
    public final void subscribe(Subscriber<E> subscriber) {
        checkSubscriber(subscriber);
        doSubscribe(subscriber);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for subscribe()">
    private void doSubscribe(Subscriber<E> subscriber) {
        if (subscriptions.contains(subscriber))
            throw new SubscriberAlreadyRegisteredException(subscriber);
        subscriptions.add(subscriber);
    }
    // </editor-fold>

    @Override
    public void unsubscribe(Subscriber<E> subscriber) {
        if (!subscriptions.contains(subscriber))
            throw new SubscriberNotRegisteredException(subscriber);
        subscriptions.remove(subscriber);
    }

    @Override
    public int hashCode() {
        return 5 * 19
                + Objects.hashCode(this.subscriptions);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.subscriptions, ((SimpleEventService<?>) obj).subscriptions);
    }
}
