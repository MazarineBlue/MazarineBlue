/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaultFeedExecutorIT {

    private Executor executor;

    @Before
    public void setup() {
        MemoryFileSystem fs = new MemoryFileSystem();
        FeedExecutorOutputSpy output = new FeedExecutorOutputSpy();
        ExecutorFactory factory = TestFeedExecutorFactory.newInstance(fs, output);
        executor = factory.create();
    }

    @After
    public void teardown() {
        executor = null;
    }

    @Test
    public void variableStore() {
        GetVariableEvent e = new GetVariableEvent("key");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "key", "value"),
                                        e));
        assertEquals("value", e.getValue());
    }

    @Test
    public void parser() {
        ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
        executor.setPublisherListener(spy);
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Set", "key", "xx ${variable} xx");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "variable", "value"),
                                        e));
        spy.startEvents().assertClasses(ExecuteInstructionLineEvent.class, ExecuteInstructionLineEvent.class);
        assertEquals("line={.set, key, xx value xx}", e.message());
    }
}
