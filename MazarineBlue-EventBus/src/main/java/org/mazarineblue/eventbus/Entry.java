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

import static java.lang.String.format;
import java.util.Objects;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
final class Entry<E extends Event> {

    @SuppressWarnings("PackageVisibleField")
    Class<?> eventType;

    @SuppressWarnings("PackageVisibleField")
    Filter<E> filter;

    @SuppressWarnings("PackageVisibleField")
    Subscriber<E> subscriber;

    Entry() {
    }

    Entry(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        set(eventType, filter, subscriber);
    }

    @Override
    public String toString() {
        return eventType == null ? "null"
                : format("eventType=%s, filter={%s} , subscriber={%s}", eventType.getSimpleName(), filter, subscriber);
    }

    void reset() {
        set(null, null, null);
    }

    void set(Class<?> eventType, Filter<E> filter, Subscriber<E> subscriber) {
        this.eventType = eventType;
        this.filter = filter;
        this.subscriber = subscriber;
    }

    @Override
    public int hashCode() {
        return 7 * 53 * 53 * 53
                + 53 * 53 * Objects.hashCode(eventType)
                + 53 * Objects.hashCode(filter)
                + Objects.hashCode(subscriber);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.eventType, ((Entry<?>) obj).eventType)
                && Objects.equals(this.filter, ((Entry<?>) obj).filter)
                && Objects.equals(this.subscriber, ((Entry<?>) obj).subscriber);
    }
}
