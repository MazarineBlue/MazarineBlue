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
package org.mazarineblue.libraries;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.GetTestResultEvent;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

public class RuntimeLibraryInstructionsTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutor executor;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        FeedExecutorFactory factory = TestFeedExecutorFactory.newInstance(fs, output);
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(ExceptionThrownEvent.class));
        executor = factory.create();
    }

    @After
    public void teardown() {
        fs = null;
        output = null;
        executor = null;
    }

    @Test
    public void checkEqual_FooAndFoo() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Check equals", "Foo", "Foo"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(1, e.getTests());
        assertEquals(0, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }

    @Test
    public void checkEqual_FooAndOof() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Check equals", "Foo", "Oof"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(1, e.getTests());
        assertEquals(1, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }

    @Test
    public void checkNotEqual_FooAndFoo() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Check not equals", "Foo", "Foo"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(1, e.getTests());
        assertEquals(1, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }

    @Test
    public void checkNotEqual_FooAndOof() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Check not equals", "Foo", "Oof"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(1, e.getTests());
        assertEquals(0, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }

    @Test
    public void fail() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Fail", "This is the error message"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(1, e.getTests());
        assertEquals(1, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }
}
