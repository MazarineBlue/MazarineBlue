/*
 * Copyright (c) 2012 Alex de Kruijff
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
import java.util.Set;
import org.mazarineblue.eventbus.exceptions.EventHandlerMissingException;
import org.mazarineblue.eventbus.exceptions.IllegalEventHandlerException;
import org.mazarineblue.eventbus.exceptions.IllegalEventTypeException;
import org.mazarineblue.eventbus.exceptions.MissingSubscriberExcpetion;

public class SimpleEventService<E extends Event>
        implements EventService<E> {

    private final Class<?> eventType;
    private final Set<Entry<E>> subscriptions;
    private final Entry<E> tmp = new Entry<>();

    public SimpleEventService() {
        this(Event.class);
    }

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public SimpleEventService(Class<?> eventType) {
        if (Event.class.isAssignableFrom(eventType) == false)
            throw new IllegalEventTypeException(Event.class, eventType);
        this.eventType = eventType;
        subscriptions = new HashSet<>();
    }

    @Override
    public void publish(E event) {
        if (event == null)
            return;
        for (Entry<E> entry : subscriptions) {
            if (isHandlerApplicable(event, entry))
                entry.subscriber.eventHandler(event);
            if (event.isConsumed())
                break;
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
        checkSubscribeInput(eventType, filter, subscriber);
        return doSubscribe(eventType, filter, subscriber);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for subscribe()">
    private void checkSubscribeInput(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        if (this.eventType.isAssignableFrom(eventType) == false)
            throw new IllegalEventTypeException(this.eventType, eventType);
        if (subscriber == null)
            throw new MissingSubscriberExcpetion();
        if (subscriber instanceof ReflectionSubscriber)
            if (hasHandler(subscriber) == false)
                throw new EventHandlerMissingException(subscriber);
    }

    private boolean hasHandler(Subscriber<E> subscriber) {
        for (Method baseHandler : subscriber.getClass().getMethods()) {
            if (isEventHandler(baseHandler) == false)
                continue;
            if (isValidEventHandler(baseHandler) == false)
                throw new IllegalEventHandlerException(subscriber);
            return true;
        }
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
            if (actualType[i].isAssignableFrom(expectedType[i]) == false)
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
        return removeEvent(filter, subscriber);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for unsubscribe()">
    private boolean removeEvent(Filter<E> filter, Subscriber<E> subscriber) {
        tmp.set(eventType, filter, subscriber);
        boolean result = subscriptions.remove(tmp);
        tmp.reset();
        return result;
    }
    // </editor-fold>
}
