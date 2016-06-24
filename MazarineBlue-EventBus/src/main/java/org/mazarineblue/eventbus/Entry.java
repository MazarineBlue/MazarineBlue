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

import java.util.Objects;

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
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.eventType);
        hash = 53 * hash + Objects.hashCode(this.filter);
        hash = 53 * hash + Objects.hashCode(this.subscriber);
        return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entry<E> other = (Entry) obj;
        if (Objects.equals(this.eventType, other.eventType) == false)
            return false;
        if (Objects.equals(this.filter, other.filter) == false)
            return false;
        return Objects.equals(this.subscriber, other.subscriber) != false;
    }
}
