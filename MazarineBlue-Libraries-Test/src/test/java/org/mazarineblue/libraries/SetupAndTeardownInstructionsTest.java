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

import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import static org.mazarineblue.libraries.MainTestLibraryTest.RESOURCE_POSTFIX;
import static org.mazarineblue.libraries.MainTestLibraryTest.assertExecutorListenerCalls;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTeardownEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.events.GetTestResultEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.util.TestListenerSpy;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

public class SetupAndTeardownInstructionsTest {

    private static final String RESOURCE_PREFIX = "org/mazarineblue/libraries/"
            + new Object(){}.getClass().getEnclosingClass().getSimpleName() + "/";

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutor executor;
    private TestListener listener;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        FeedExecutorFactory factory = TestFeedExecutorFactory.newInstance(fs, output);
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(ExceptionThrownEvent.class));
        executor = factory.create();
        listener = new TestListenerSpy(Event.matches(AddLibraryEvent.class, ExecuteSetupEvent.class,
                                                     ExecuteTestEvent.class, ExecuteTeardownEvent.class,
                                                     ExecuteInstructionLineEvent.class));
    }

    @After
    public void teardown()
            throws Exception {
        fs = null;
        output = null;
        executor = null;
        listener = null;
    }

    @Test
    public void globalSetup_Fail()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Fail", "Setup: Global"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(3, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void globalSetup_Exception()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Unkown keyword", "Setup: Global"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(3, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void globalTeardown_Fail()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Fail", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(3, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void globalTeardown_Excepetion()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Unkown keyword", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(3, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }
    
    @Test
    public void suiteSetup_Fail()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Fail", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(2, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void suiteSetup_Exception()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Unkown keyword", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(2, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void suiteTeardown_Fail()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Fail", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(2, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void suiteTeardown_Exception()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Unkown keyword", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(2, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void test_Fail()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Fail", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(1, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }

    @Test
    public void test_Exception()
            throws IOException {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new SetTestListenerEvent(listener),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Setup"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Teardown"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Test", "Test A1"),
                new ExecuteInstructionLineEvent("Unkown keyword", "Teardown: Global"),

                new ExecuteInstructionLineEvent("Test", "Test A2"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("Suite", "Suite B"),
                new ExecuteInstructionLineEvent("Test", "Test B1"),
                new ExecuteInstructionLineEvent("Check equals", "Lorem Ipsum", "Lorem Ipsum"),

                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(3, e.getTests());
        assertEquals(1, e.getFailedTests());
        assertFalse(executor.containsErrors());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, listener);
    }
}
