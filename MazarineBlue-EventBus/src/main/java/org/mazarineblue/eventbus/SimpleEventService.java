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
package org.mazarineblue.eventbus;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.mazarineblue.eventbus.exceptions.EventHandlerMissingException;
import org.mazarineblue.eventbus.exceptions.IllegalEventHandlerException;
import org.mazarineblue.eventbus.exceptions.IllegalEventTypeException;
import org.mazarineblue.eventbus.exceptions.MissingSubscriberExcpetion;

/**
 * A {@code SimpleEventService} is publisher of {@link Event Events} to its
 * {@link Subscriber Subscribers}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <E> the type of events this service publish.
 */
public class SimpleEventService<E extends Event>
        implements EventService<E> {

    private final Class<?> eventType;
    private final Set<Entry<E>> subscriptions;
    private final Entry<E> tmp = new Entry<>();

    /**
     * Constructs a {@code SimpleEventService} that publishes all
     * {@link Event Events}.
     */
    public SimpleEventService() {
        this(Event.class);
    }

    /**
     * Constructs a {@code SimpleEventService} that publishes
     * {@link Event Events} of a given type.
     *
     * @param eventType the base type of {@code Events} published.
     */
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public SimpleEventService(Class<?> eventType) {
        if (!Event.class.isAssignableFrom(eventType))
            throw new IllegalEventTypeException(Event.class, eventType);
        this.eventType = eventType;
        subscriptions = new HashSet<>();
    }

    @Override
    public String toString() {
        return "size = " + subscriptions.size();
    }

    @Override
    public void publish(E event) {
        if (event == null)
            return;
        for (Entry<E> entry : subscriptions) {
            if (isHandlerApplicable(event, entry))
                entry.subscriber.eventHandler(event);
            if (event.isConsumed())
                return;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for subscribe()">
    private boolean isHandlerApplicable(E event, Entry<E> entry) {
        boolean assignable = entry.eventType.isAssignableFrom(event.getClass());
        boolean applicable = entry.filter == null || entry.filter.apply(event);
        return assignable && applicable;
    }
    // </editor-fold>

    @Override
    @SuppressWarnings("AssignmentToMethodParameter")
    public boolean subscribe(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        if (eventType == null)
            eventType = Event.class;
        checkSubscriberInput(eventType, subscriber);
        return doSubscribe(eventType, filter, subscriber);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for subscribe()">
    private void checkSubscriberInput(Class<?> eventType, Subscriber<E> subscriber) {
        if (!this.eventType.isAssignableFrom(eventType))
            throw new IllegalEventTypeException(this.eventType, eventType);
        if (subscriber == null)
            throw new MissingSubscriberExcpetion();
        if (subscriber instanceof ReflectionSubscriber && !hasHandler(subscriber))
            throw new EventHandlerMissingException(subscriber);
    }

    private boolean hasHandler(Subscriber<E> subscriber) {
        for (Method baseHandler : subscriber.getClass().getMethods())
            if (isEventHandler(baseHandler))
                if (!isValidEventHandler(baseHandler))
                    throw new IllegalEventHandlerException(subscriber);
                else
                    return true;
        return false;
    }

    private boolean isEventHandler(Method method) {
        return method.getAnnotation(EventHandler.class) != null;
    }

    private boolean isValidEventHandler(Method baseHandler) {
        for (Method method : Subscriber.class.getMethods())
            if (equalMethods(baseHandler, method))
                return true;
        return false;
    }

    private boolean equalMethods(Method expected, Method actual) {
        Class<?>[] expectedType = expected.getParameterTypes();
        Class<?>[] actualType = actual.getParameterTypes();
        if (actualType.length != expectedType.length)
            return false;
        for (int i = 0; i < actualType.length; ++i)
            if (!actualType[i].isAssignableFrom(expectedType[i]))
                return false;
        return true;
    }

    private boolean doSubscribe(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        if (containsSubscriber(filter, subscriber))
            return false;
        Entry<E> entry = new Entry<>(eventType, filter, subscriber);
        return subscriptions.add(entry);
    }

    private boolean containsSubscriber(Filter<E> filter, Subscriber<E> subscriber) {
        tmp.set(eventType, filter, subscriber);
        boolean result = subscriptions.contains(tmp);
        tmp.reset();
        return result;
    }
    // </editor-fold>

    @Override
    public boolean unsubscribe(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        tmp.set(eventType == null ? this.eventType : eventType, filter, subscriber);
        boolean result = subscriptions.remove(tmp);
        tmp.reset();
        return result;
    }

    @Override
    public int hashCode() {
        return 5 * 19 * 19
                + 19 * Objects.hashCode(this.eventType)
                + Objects.hashCode(this.subscriptions);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.eventType, ((SimpleEventService<?>) obj).eventType)
                && Objects.equals(this.subscriptions, ((SimpleEventService<?>) obj).subscriptions);
    }
}
