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

import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTeardownEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.listeners.TestListenerList;
import org.mazarineblue.libraries.test.util.OutputCollectorTestListener;
import org.mazarineblue.libraries.test.util.ResultCollectorTestListener;
import static org.mazarineblue.libraries.test.util.TestUtilility.RESOURCE_POSTFIX;
import static org.mazarineblue.libraries.test.util.TestUtilility.assertExecutorListenerCalls;
import static org.mazarineblue.libraries.test.util.TestUtilility.getResourcePrefix;

public class SetupAndTeardownInstructionsTest
        extends AbstractExecutorTestHelper {

    private static final String RESOURCE_PREFIX = getResourcePrefix(SetupAndTeardownInstructionsTest.class.getPackage(),
                                                                    new Object(){}.getClass().getEnclosingClass().getSimpleName());
    private TestListener output;
    private ResultCollectorTestListener results;

    @Before
    public void setup() {
        output = new OutputCollectorTestListener(matchesAny(AddLibraryEvent.class, ExecuteSetupEvent.class,
                                                            ExecuteTestEvent.class, ExecuteTeardownEvent.class,
                                                            ExecuteInstructionLineEvent.class,
                                                            ExceptionThrownEvent.class));
        results = new ResultCollectorTestListener();
        TestListenerList listener = new TestListenerList();
        listener.addListener(output);
        listener.addListener(results);
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                               new SetTestListenerEvent(listener)));
    }

    @After
    public void teardown() {
        output = null;
    }

    @Test
    public void globalSetup_Fail()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(3, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void globalSetup_Exception()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(3, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void globalTeardown_Fail()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(3, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void globalTeardown_Excepetion()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(3, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void suiteSetup_Fail()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(2, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void suiteSetup_Exception()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(2, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void suiteTeardown_Fail()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(2, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void suiteTeardown_Exception()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(2, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void test_Fail()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }

    @Test
    public void test_Exception()
            throws IOException {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
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

                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
        assertSuccess();
        assertEquals(3, results.getTestCount());
        assertEquals(1, results.getFailedTestCount());
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        assertExecutorListenerCalls(RESOURCE_PREFIX + name + RESOURCE_POSTFIX, output);
    }
}
