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

import org.awaitility.core.ConditionTimeoutException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.util.ResultCollectorTestListener;

public class MainTestLibraryTest
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
        results = null;
    }

    @Test
    public void testSystem_Empty() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        results.throwFirstException();
        assertEquals(0, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
    }

    @Test(timeout = 2000, expected = ConditionTimeoutException.class)
    public void jobs_TwoJobsWaitingOnEachOther_OneThread() {
        runJobs(1);
        assertSuccess();
        results.throwFirstException();
        assertEquals(2, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
    }

    @Test(timeout = 1000)
    public void jobs() {
        runJobs(2);
        assertSuccess();
        results.throwFirstException();
        assertEquals(2, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
    }

    private void runJobs(int maximumJobs) {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set global", "key", "value"),
                               new ExecuteInstructionLineEvent("Set global", "first", "xxx"),
                               new ExecuteInstructionLineEvent("Set global", "second", "xxx"),

                               new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Maximum jobs", maximumJobs),

                               new ExecuteInstructionLineEvent("Setup"),
                               new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.awaitility"),

                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Set global", "first", "value"),
                               new ExecuteInstructionLineEvent("Await", "Timeout", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "Expression is true", "second == key"),

                               new ExecuteInstructionLineEvent("Test", "Test 2"),
                               new ExecuteInstructionLineEvent("Set global", "second", "value"),
                               new ExecuteInstructionLineEvent("Await", "Timeout", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "Expression is true", "first == key"),
                               new ExecuteInstructionLineEvent("End test set"),

                               new ExecuteInstructionLineEvent("Run tests")));
    }
}
