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
package org.mazarineblue.libraries.awaitility;

import static java.lang.Thread.sleep;
import org.awaitility.core.ConditionTimeoutException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.awaitility.exceptions.AwaitInstructionAlreadyCalledException;

public class AwaitilityLibraryTest
        extends AbstractExecutorTestHelper {

    @Before
    public void setup() {
        execute(new MemoryFeed(new AddLibraryEvent(new MyLibrary()),
                               new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.awaitility")));
    }

    @Test
    public void await_Onces() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await")));
        assertSuccess();
    }

    @Test(expected = AwaitInstructionAlreadyCalledException.class)
    public void await_Twice() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Await")));
        assertFailure();
    }

    @Test(timeout = 1000)
    public void until() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertSuccess();
    }

    @Ignore("Takes 10 seconds")
    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void until_UntilTimeout() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Until", "expression is false", "1 == 1")));
        assertFailure();
    }

    @Test(timeout = 1000)
    public void atMost() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await", "At most", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "test", "10")));
        assertSuccess();
    }

    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void atMost_UntilTimeout() {
        long start = System.currentTimeMillis();
        try {
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                                   new ExecuteInstructionLineEvent("At most", "500ms"),
                                   new ExecuteInstructionLineEvent("Until", "expression is false", "1 == 1")));
            assertFailure();
        } catch (ConditionTimeoutException ex) {
            long duration = System.currentTimeMillis() - start;
            assertEquals(300, duration, 300);
            throw ex;
        }
    }

    @Test(timeout = 1000)
    public void timeout() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await", "Timeout", "200ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertSuccess();
    }

    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void timeout_UntilTimeout() {
        long start = System.currentTimeMillis();
        try {
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                                   new ExecuteInstructionLineEvent("Timeout", "200ms"),
                                   new ExecuteInstructionLineEvent("Until", "expression is false", "1 == 1")));
            assertFailure();
        } catch (ConditionTimeoutException ex) {
            long duration = System.currentTimeMillis() - start;
            assertEquals(1200, duration, 1000);
            throw ex;
        }
    }

    @Test(timeout = 1000)
    public void atLeast() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await", "At least", "300ms",
                                                               "Until", "test", "300")));
        assertSuccess();
    }

    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void atLeast_UntilTimeout() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("At least", "300ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertFailure();
    }

    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void between_Sleep100ms() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Between", "300ms", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertSuccess();
    }

    @Test(timeout = 1000)
    public void between_Sleep300ms() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Between", "300ms", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "test", "300")));
        assertSuccess();
    }

    @Test(expected = ConditionTimeoutException.class, timeout = 1000)
    public void between_Sleep500ms() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Between", "300ms", "500ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is false", "1 == 1")));
        assertSuccess();
    }

    @Test(timeout = 1000)
    public void pollDelay() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Poll delay", "0ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertSuccess();
    }

    @Test(timeout = 1000)
    public void pollInterval() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Poll interval", "10ms"),
                               new ExecuteInstructionLineEvent("Until", "expression is true", "1 == 1")));
        assertSuccess();
    }

    @Test(timeout = 1000)
    public void forever() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Await"),
                               new ExecuteInstructionLineEvent("Timeout", "110ms"),
                               new ExecuteInstructionLineEvent("Forever"),
                               new ExecuteInstructionLineEvent("Until", "test", "200")));
        assertSuccess();
    }

    @SuppressWarnings("PublicInnerClass")
    public static class MyLibrary
            extends Library {

        MyLibrary() {
            super("test");
        }

        @Keyword("Test")
        @Parameters(min = 1)
        public boolean test(long millis)
                throws InterruptedException {
            sleep(millis);
            return true;
        }
    }
}
