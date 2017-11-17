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
package org.mazarineblue.procedures;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.procedures.events.CallProcedureEvent;
import org.mazarineblue.procedures.events.StartRecordingProcedureEvent;
import org.mazarineblue.procedures.events.StopRecordingProcedureEvent;
import org.mazarineblue.procedures.events.TestIncrementEvent;
import org.mazarineblue.procedures.exceptions.ProcedureNameTakenException;
import org.mazarineblue.procedures.exceptions.ProcedureNotFoundException;
import org.mazarineblue.procedures.exceptions.StartRecordingProcedureFirstException;
import org.mazarineblue.procedures.exceptions.StopRecordingPreviousProcedureFirstException;
import org.mazarineblue.procedures.util.TestSubscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ProcedureLinkTest {

    @SuppressWarnings("PublicInnerClass")
    public class Main {

        private TestSubscriber testSubscriber;
        private MemoryFeed feed;
        private Interpreter interpreter;

        @Before
        public void setup() {
            testSubscriber = new TestSubscriber();
            feed = new MemoryFeed();
            interpreter = InterpreterFactory.createDefaultFactory().create();
            interpreter.addLink(new EventBusLink(TestEvent.class, null, testSubscriber));
            interpreter.addLink(new ProcedureLink(interpreter, ExceptionThrownEvent.class));
        }

        @After
        public void teardown() {
            feed = null;
            interpreter = null;
            testSubscriber = null;
        }

        @Test(expected = StopRecordingPreviousProcedureFirstException.class)
        public void publish_DoubleRecording_ThrowsException() {
            feed.add(new StartRecordingProcedureEvent("name"));
            feed.add(new StartRecordingProcedureEvent("name"));
            interpreter.execute(feed);
        }

        @Test(expected = ProcedureNameTakenException.class)
        public void publish_NameReuseWithRecordProcedure_ThrowsException() {
            feed.add(new StartRecordingProcedureEvent("name"));
            feed.add(new StopRecordingProcedureEvent());
            feed.add(new StartRecordingProcedureEvent("name"));
            interpreter.execute(feed);
        }

        @Test(expected = StartRecordingProcedureFirstException.class)
        public void publish_StopRecording_ThrowsException() {
            feed.add(new StopRecordingProcedureEvent());
            interpreter.execute(feed);
        }

        @Test(expected = ProcedureNotFoundException.class)
        public void publish_CallNonExistingProcedure_ThrowsException() {
            feed.add(new CallProcedureEvent("name"));
            interpreter.execute(feed);
        }

        @Test
        public void publish_StartAndStopRecordingAndCallProcedure_OK() {
            feed.add(new StartRecordingProcedureEvent("nested"));
            feed.add(new TestIncrementEvent());
            feed.add(new StopRecordingProcedureEvent());

            feed.add(new StartRecordingProcedureEvent("name"));
            feed.add(new CallProcedureEvent("nested"));
            feed.add(new StopRecordingProcedureEvent());

            feed.add(new CallProcedureEvent("nested"));
            feed.add(new CallProcedureEvent("name"));
            interpreter.execute(feed);
            assertEquals(2, testSubscriber.count());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode {

        private ProcedureLink a;
        private Interpreter interpreter;

        @Before
        public void setup() {
            interpreter = InterpreterFactory.createDefaultFactory().create();
            a = new ProcedureLink(interpreter);
        }

        @After
        public void teardown() {
            a = null;
        }

        @Test
        @SuppressWarnings("ObjectEqualsNull")
        public void equals_Null() {
            assertFalse(a.equals(null));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_DifferentClass() {
            assertFalse(a.equals(""));
        }

        @Test
        public void hashCode_DifferentInterpreter() {
            ProcedureLink b = new ProcedureLink(InterpreterFactory.createDefaultFactory().create());
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentInterpreter() {
            ProcedureLink b = new ProcedureLink(InterpreterFactory.createDefaultFactory().create());
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_DifferentIgnoreTypes() {
            ProcedureLink b = new ProcedureLink(interpreter, Event.class);
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentIgnoreTypes() {
            ProcedureLink b = new ProcedureLink(interpreter, Event.class);
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_DifferentCurrentRecording() {
            ProcedureLink b = new ProcedureLink(interpreter);
            b.eventHandler(new StartRecordingProcedureEvent("foo"));
            b.eventHandler(new TestEvent());
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentCurrentRecording() {
            ProcedureLink b = new ProcedureLink(interpreter);
            b.eventHandler(new StartRecordingProcedureEvent("foo"));
            b.eventHandler(new TestEvent());
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_DifferentRegistrations() {
            ProcedureLink b = new ProcedureLink(interpreter);
            b.eventHandler(new StartRecordingProcedureEvent("foo"));
            b.eventHandler(new TestEvent());
            b.eventHandler(new StopRecordingProcedureEvent());
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentRegistrations() {
            ProcedureLink b = new ProcedureLink(interpreter);
            b.eventHandler(new StartRecordingProcedureEvent("foo"));
            b.eventHandler(new TestEvent());
            b.eventHandler(new StopRecordingProcedureEvent());
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_IdenticalContent() {
            ProcedureLink b = new ProcedureLink(interpreter);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_IdenticalContent() {
            ProcedureLink b = new ProcedureLink(interpreter);
            assertEquals(a, b);
        }
    }
}
