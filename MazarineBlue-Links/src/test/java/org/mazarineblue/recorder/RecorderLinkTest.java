/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.recorder;

import java.util.List;
import java.util.function.Supplier;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.function.Condition;
import org.mazarineblue.recorder.events.StopRecordingEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderLinkTest {

    private MemoryFeed feed;
    private Interpreter interpreter;
    private Condition<Event> stopCondition;
    private RecorderLink link;

    @Before
    public void setup() {
        feed = new MemoryFeed();
        interpreter = InterpreterFactory.createDefaultFactory().create();
        stopCondition = e -> StopRecordingEvent.class.isAssignableFrom(e.getClass());
        link = new RecorderLink(interpreter, stopCondition);
        interpreter.addLink(link);
    }

    @After
    public void teardown() {
        feed = null;
        interpreter = null;
        link = null;
    }

    @Test
    public void initial() {
        List<? extends Event> recording = link.getRecording();
        assertEquals(1, interpreter.countLinks());
        assertEquals(0, recording.size());
    }

    @Test
    public void recoding() {
        Event[] events = createEvents(2, () -> new TestEvent());
        feed.add(events);
        feed.add(new StopRecordingEvent());

        interpreter.execute(feed);
        consumeEvents(events);
        List<? extends Event> recording = link.getRecording();

        assertEquals(0, interpreter.countLinks());
        assertEquals(events.length, recording.size());
        for (int i = 0; i < events.length; ++i) {
            assertTrue(events[i].isConsumed());
            assertFalse(recording.get(i).isConsumed());
            assertEquals(events[i], recording.get(i));
        }
    }

    private Event[] createEvents(int amount, Supplier<Event> supplier) {
        Event[] events = new Event[amount];
        for (int i = 0; i < events.length; ++i)
            events[i] = supplier.get();
        return events;
    }

    private void consumeEvents(Event[] events) {
        for (int i = 0; i < events.length; ++i)
            events[i].setConsumed(true);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(link.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(link.equals(""));
    }

    @Test
    public void hashCode_DifferentInterpreter() {
        Interpreter i = InterpreterFactory.createDefaultFactory().create();
        RecorderLink other = new RecorderLink(i, stopCondition);
        assertNotEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_DifferentInterpreter() {
        Interpreter i = InterpreterFactory.createDefaultFactory().create();
        RecorderLink other = new RecorderLink(i, stopCondition);
        assertNotEquals(link, other);
    }

    @Test
    public void hashCode_DifferentStopCondition() {
        Condition<Event> stop = e -> StopRecordingEvent.class.isAssignableFrom(e.getClass());
        RecorderLink other = new RecorderLink(interpreter, stop);
        assertNotEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_DifferentStopCondition() {
        Condition<Event> stop = e -> StopRecordingEvent.class.isAssignableFrom(e.getClass());
        RecorderLink other = new RecorderLink(interpreter, stop);
        assertNotEquals(link, other);
    }

    @Test
    public void hashCode_DifferentRecording() {
        RecorderLink other = new RecorderLink(interpreter, stopCondition);
        other.eventHandler(new TestEvent());
        assertNotEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_DifferentRecording() {
        RecorderLink other = new RecorderLink(interpreter, stopCondition);
        other.eventHandler(new TestEvent());
        assertNotEquals(link, other);
    }

    @Test
    public void hashCode_IdenticalContent() {
        RecorderLink other = new RecorderLink(interpreter, stopCondition);
        assertEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        RecorderLink other = new RecorderLink(interpreter, stopCondition);
        assertEquals(link, other);
    }
}
