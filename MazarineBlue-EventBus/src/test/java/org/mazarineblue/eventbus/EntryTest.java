/*
 * Copyright (c) 2015 Alex de Kruijff
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
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.eventbus.events.EventDummy;
import org.mazarineblue.eventbus.events.UnassignableEventDummy;
import org.mazarineblue.eventbus.filters.BlockingFilterStub;
import org.mazarineblue.eventbus.filters.PassingFilterStub;
import org.mazarineblue.eventbus.subscribers.SubscriberDummy;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class EntryTest {

    static private String message;
    static private Filter filter;
    static private Subscriber subscriber;
    static private Entry expected;

    @BeforeClass
    static public void setupClass() {
        message = "Test";
        filter = new PassingFilterStub();
        subscriber = new SubscriberDummy();
        expected = new Entry(EventDummy.class, filter, subscriber);
    }

    @AfterClass
    static public void teardownClass() {
        message = null;
        filter = null;
        subscriber = null;
        expected = null;
    }

    @Test
    public void equals_SameObject_ReturnsTrue() {
        assertEquals(true, expected.equals(expected));
    }

    @Test
    public void equals_Null_ReturnsFalse() {
        assertEquals(false, expected.equals(null));
    }

    @Test
    public void equals_Boolean_ReturnsFalse() {
        assertEquals(false, expected.equals(true));
    }

    @Test
    public void equals_NullEvent_ReturnFalse() {
        Entry actual = new Entry(null, filter, subscriber);
        assertEquals(false, expected.equals(actual));
    }

    @Test
    public void equals_UnassinableEvent_ReturnFalse() {
        Entry actual = new Entry(UnassignableEventDummy.class, filter,
                                 subscriber);
        assertEquals(false, expected.equals(actual));
    }

    @Test
    public void equals_NullFilter_ReturnFalse() {
        Entry actual = new Entry(EventDummy.class, null, subscriber);
        assertEquals(false, expected.equals(actual));
    }

    @Test
    public void equals_xNullFilter_ReturnFalse() {
        Entry actual = new Entry(EventDummy.class, new BlockingFilterStub(),
                                 subscriber);
        assertEquals(false, expected.equals(actual));
    }
}
