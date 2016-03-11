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
package org.mazarineblue.eventbus;

import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.eventbus.events.EventSpy;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.filters.BlockingFilter;
import org.mazarineblue.eventbus.filters.PassingFilter;
import org.mazarineblue.eventbus.subscribers.DummySubscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EntryTest {

    private static String message;
    private static Filter<Event> filter;
    private static Subscriber<Event> subscriber;
    private static Entry<Event> expected;

    @BeforeClass
    public static void setupClass() {
        message = "Test";
        filter = new PassingFilter<>();
        subscriber = new DummySubscriber<>();
        expected = new Entry<>(TestEvent.class, filter, subscriber);
    }

    @AfterClass
    public static void teardownClass() {
        message = null;
        filter = null;
        subscriber = null;
        expected = null;
    }

    @Test
    public void equals_SameObject_ReturnsTrue() {
        assertTrue(expected.equals(expected));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null_ReturnsFalse() {
        assertFalse(expected.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_Boolean_ReturnsFalse() {
        assertFalse(expected.equals(true));
    }

    @Test
    public void equals_NullEvent_ReturnFalse() {
        Entry<Event> actual = new Entry<>(null, filter, subscriber);
        assertFalse(expected.equals(actual));
    }

    @Test
    public void equals_UnassinableEvent_ReturnFalse() {
        Entry<Event> actual = new Entry<>(EventSpy.class, filter, subscriber);
        assertFalse(expected.equals(actual));
    }

    @Test
    public void equals_NullFilter_ReturnFalse() {
        Entry<Event> actual = new Entry<>(TestEvent.class, null, subscriber);
        assertFalse(expected.equals(actual));
    }

    @Test
    public void equals_xNullFilter_ReturnFalse() {
        Entry<Event> actual = new Entry<>(TestEvent.class, new BlockingFilter<>(), subscriber);
        assertFalse(expected.equals(actual));
    }
}
