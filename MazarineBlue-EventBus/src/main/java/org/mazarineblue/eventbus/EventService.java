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

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 * @param <E>
 */
public interface EventService<E extends Event> {

    /**
     * Publish the specified event to the registered subscribers.
     *
     * @param event the specified event to pass to the registered subscribers.
     */
    public void publish(E event);

    /**
     * Subscribes an subscriber to receive events.
     *
     * @param eventType the type of events to listen to
     * @param filter the filter to apply
     * @param subscriber the subscriber to register
     * @return true op on success.
     */
    public boolean subscribe(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber);

    /**
     * Unsubsribes an subscriber to no longer receive events.
     *
     * @param eventType the type of events to listen to
     * @param filter the filter to apply
     * @param subscriber the subscriber to unregister
     * @return true op on success.
     */
    public boolean unsubscribe(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber);
}
