/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.engine.nestedinstructions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

public class NestedInstructionSubscriberTest {

    private ProcessorListenerSpy spy;
    private Processor processor;

    @Before
    public void setup() {
        spy = new ProcessorListenerSpy();
        processor = Processor.newInstance();
        processor.addLink(new TestSubsciber());
        processor.addLink(new NestedInstructionExecutor());
        processor.setChainModifierListener(spy);
        processor.setFeedExecutorListener(spy);
        processor.setPublisherListener(spy);
    }

    @After
    public void teardown() {
        spy = null;
        processor = null;
    }

    @Test
    public void test_ValidateInstructionLineEvent_WithoutNestedInstruction() {
        processor.execute(new MemoryFeed(new ValidateInstructionLineEvent("first", "second", "thirth")));
        spy.startEvents().assertObjects(new ValidateInstructionLineEvent("first", "second", "thirth"));
    }

    @Test
    public void test_ValidateInstructionLineEvent_WithNestedInstruction() {
        processor.execute(new MemoryFeed(new ValidateInstructionLineEvent("first", "=second", "thirth")));
        spy.startEvents().assertObjects(new ValidateInstructionLineEvent("first", "=second", "thirth"),
                                        new ValidateInstructionLineEvent("second", "thirth"));
    }

    @Test
    public void test_ExecuteInstructionLineEvent_WithoutNestedInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("first", "second", "thirth")));
        spy.startEvents().assertObjects(new ExecuteInstructionLineEvent("first", "second", "thirth").setResult("result"));
    }

    @Test
    public void test_ExecuteInstructionLineEvent_WithNestedInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("first", "=second", "thirth")));
        spy.startEvents().assertObjects(new ExecuteInstructionLineEvent("first", "result").setResult("result"),
                                        new ExecuteInstructionLineEvent("second", "thirth").setResult("result"));
    }

    public static class TestSubsciber
            extends ReflectionSubscriber<Event> {

        @EventHandler
        public void eventHandler(ValidateInstructionLineEvent e) {
            e.setConsumed(true);
        }

        @EventHandler
        public void eventHandler(ExecuteInstructionLineEvent e) {
            e.setResult("result");
            e.setConsumed(true);
        }
    }

    private static class TestLibrary {

        public TestLibrary() {
        }
    }
}
