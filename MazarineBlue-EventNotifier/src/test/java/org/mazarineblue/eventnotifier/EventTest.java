/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventnotifier;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.function.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventnotifier.events.AbstractEvent;
import org.mazarineblue.eventnotifier.events.TestAutoConsumableEvent;
import org.mazarineblue.eventnotifier.events.TestEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EventTest {

    @Test
    public void matchesAnyAutoConsumable() {
        Predicate<Event> condition = Event.matchesAnyAutoConsumable();
        assertFalse(condition.test(new DummyEvent()));
        assertTrue(condition.test(new TestAutoConsumableEvent()));
    }

    @Test
    public void matchesNoneAutoConsumable() {
        Predicate<Event> condition = Event.matchesNoneAutoConsumable();
        assertTrue(condition.test(new DummyEvent()));
        assertFalse(condition.test(new TestAutoConsumableEvent()));
    }

    @Test
    public void matchesAnyConsumed() {
        TestEvent consumedEvent = new TestEvent();
        consumedEvent.setConsumed(true);
        Predicate<Event> condition = Event.matchesAnyConsumed();
        assertFalse(condition.test(new DummyEvent()));
        assertTrue(condition.test(consumedEvent));
    }

    @Test
    public void matchesNoneConsumed() {
        TestEvent consumedEvent = new TestEvent();
        consumedEvent.setConsumed(true);
        Predicate<Event> condition = Event.matchesNoneConsumed();
        assertTrue(condition.test(new DummyEvent()));
        assertFalse(condition.test(consumedEvent));
    }

    @Test
    public void matchesAny() {
        Predicate<Event> condition = Event.matchesAny(TestEvent.class);
        assertTrue(condition.test(new TestEvent()));
        assertFalse(condition.test(new DummyEvent()));
    }

    @Test
    public void matchesNone() {
        Predicate<Event> condition = Event.matchesNone(TestEvent.class);
        assertFalse(condition.test(new TestEvent()));
        assertTrue(condition.test(new DummyEvent()));
    }

    @Test
    public void clone_UnconsumedEvent() {
        Collection<TestEvent> src = asList(new TestEvent());
        Collection<TestEvent> dst = Event.clone(src);
        dst.stream().forEach(e -> assertFalse(e.isConsumed()));
        assertEquals(src, dst);
        assertNotSame(src, dst);
    }

    @Test
    public void clone_ConsumedEvent() {
        TestEvent e = new TestEvent();
        e.setConsumed(true);
        Collection<TestEvent> src = asList(e);
        Collection<TestEvent> dst = Event.clone(src);
        dst.stream().forEach(t -> assertFalse(t.isConsumed()));
        assertEquals(src, dst);
        assertNotSame(src, dst);
    }

    @Test
    public void test_AbstractEvent() {
        AbstractEvent event = new DummyEvent();
        assertEquals("", event.message());
        assertEquals("", event.responce());
    }
}
