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
package org.mazarineblue.libraries.test;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.util.ResultCollectorTestListener;

public class RuntimeLibraryInstructionsTest
        extends AbstractExecutorTestHelper {
    private ResultCollectorTestListener results;

    @Before
    public void setup() {
        results = new ResultCollectorTestListener();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                               new SetTestListenerEvent(results)));
    }

    @After
    public void teardown() {
        // For testing purposes, there is no need for an implemantion.
    }

    @Test
    public void checkEqual_FooAndFoo() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Check equals", "Foo", "Foo"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(1, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
    }

    @Test
    public void checkEqual_FooAndOof() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Check equals", "Foo", "Oof"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(1, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
    }

    @Test
    public void checkNotEqual_FooAndFoo() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Check not equals", "Foo", "Foo"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(1, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
    }

    @Test
    public void checkNotEqual_FooAndOof() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Check not equals", "Foo", "Oof"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(1, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
    }

    @Test
    public void fail() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Fail", "This is the error message"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(1, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
    }
}
