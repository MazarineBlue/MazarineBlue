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
package org.mazarineblue.libraries.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.libraries.test.model.Status.FAIL;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.tests.Test;

public class ResultCollectorTestListener
        implements TestListener {

    private final Set<Test> runningTests = new TreeSet<>(Test.naturalOrder());
    private final List<Test> completedTests = new ArrayList<>();

    public long getTestCount() {
        return completedTests.stream().count();
    }

    public long getFailedTestCount() {
        return completedTests.stream().filter(matching(FAIL)).count();
    }

    private Predicate<Test> matching(org.mazarineblue.libraries.test.model.Status status) {
        return t -> t.result().getEvenStatuses().stream()
                .anyMatch(s -> s.isStatus(status));
    }

    @Override
    public void testStarted(Test test) {
        runningTests.add(test);
    }

    @Override
    public void testEnded(Test test) {
        runningTests.remove(test);
        completedTests.add(test);
    }

    @Override
    public void startPublishingEvent(Test test, Event event) {
        // This class only collects the tests.
    }

    @Override
    public void error(Test tests, String message) {
        // This class only collects the tests.
    }

    @Override
    public void exceptionThrown(Test test, RuntimeException ex) {
        // This class only collects the tests.
    }

    @Override
    public void endPublishedEvent(Test test, Event event) {
        // This class only collects the tests.
    }
}
