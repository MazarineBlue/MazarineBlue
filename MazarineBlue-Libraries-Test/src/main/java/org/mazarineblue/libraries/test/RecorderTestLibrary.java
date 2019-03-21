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

import static java.lang.String.format;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import static org.mazarineblue.keyworddriven.events.InstructionLineEvent.matchesAnyKeywords;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.libraries.test.model.TestSystem;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.subscribers.RecorderSubscriber;

public class RecorderTestLibrary
        extends AbstractTestLibrary {

    private static final Predicate<Event> stopRecordingCondition = matchesAnyKeywords("Setup", "Teardown", "Suite", "Test", "End test set");

    private final TestSystem system;
    private final Suite globalSuite;
    private Suite activeSuite;
    private Test activeTest;
    private RecorderSubscriber setup;
    private RecorderSubscriber test;
    private RecorderSubscriber teardown;

    public RecorderTestLibrary(TestSystem system) {
        this.system = system;
        globalSuite = activeSuite = system.getGlobalSuite();
    }

    @Keyword("Maximum jobs")
    @Parameters(min = 1)
    public void maxThreads(int maximumJobs) {
        system.setMaximumJobs(maximumJobs);
    }

    @Keyword("Suite")
    public void suite(String suite) {
        endRecording();
        changeActiveSuite(suite);
    }

    private void changeActiveSuite(String suite) {
        Suite s = Suite.newInstance(globalSuite, suite);
        if (system.contains(s))
            activeSuite = system.getSuite(s.getKey());
        else
            system.add(activeSuite = s);
    }

    @PassInvoker
    @Keyword("End test set")
    public void testSystem(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
        endRecording();
    }

    private void endRecording() {
        endSetupRecording();
        endTestRecording();
        endTeardownRecording();
    }

    @PassInvoker
    @Keyword("Setup")
    public void setup(Invoker invoker) {
        endRecording();
        startSetupRecording(invoker);
    }

    private void endSetupRecording() {
        if (setup == null)
            return;
        activeSuite.setSetup(setup.getRecording());
        setup = null;
    }

    private void startSetupRecording(Invoker invoker) {
        if (activeSuite.containsSetup())
            throw new IllegalArgumentException(format("Setup already defined for suite '%s'", activeSuite.name()));
        setup = new RecorderSubscriber(invoker, stopRecordingCondition);
        invoker.processor().addLink(setup);
    }

    @PassInvoker
    @Keyword("Teardown")
    public void teardown(Invoker invoker) {
        endRecording();
        startTeardownRecording(invoker);
    }

    private void endTeardownRecording() {
        if (teardown == null)
            return;
        activeSuite.setTeardown(teardown.getRecording());
        teardown = null;
    }

    private void startTeardownRecording(Invoker invoker) {
        if (activeSuite.containsTeardown())
            throw new IllegalArgumentException(format("Teardown already defined for suite '%s'", activeSuite.name()));
        teardown = new RecorderSubscriber(invoker, stopRecordingCondition);
        invoker.processor().addLink(teardown);
    }

    @PassInvoker
    @Keyword("Test")
    @Parameters(min = 1)
    public void test(Invoker invoker, String name) {
        endRecording();
        startTestRecording(invoker, name);
    }

    private void endTestRecording() {
        if (activeTest == null)
            return;
        activeTest.setEvents(test.getRecording());
        system.add(activeTest);
        activeTest = null;
    }

    private void startTestRecording(Invoker invoker, String name) {
        Test t = Test.newInstance(activeSuite, name);
        if (system.contains(t))
            throw new IllegalArgumentException(format("Test '%s' already defined", name));
        activeTest = t;
        test = new RecorderSubscriber(invoker, stopRecordingCondition);
        invoker.processor().addLink(test);
    }
}
