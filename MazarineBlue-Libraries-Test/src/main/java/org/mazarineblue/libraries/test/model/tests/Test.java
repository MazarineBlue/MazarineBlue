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
package org.mazarineblue.libraries.test.model.tests;

import java.util.Collection;
import java.util.Comparator;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.Key;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;

/**
 * A {@code Test} is a collection of instructions, which start at a given
 * state, then transition to another state and finally validate that the
 * result matches our expectation.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Test {

    /**
     * Creates a instance of a default implementation of a {@code Suite}.
     *
     * @param name the name of the suite.
     * @return the created instance.
     */
    public static Test newInstance(Suite suite, String name) {
        return new DefaultTest(suite, name);
    }

    public default Key getKey() {
        return this instanceof Key ? (Key) this : new TestKey(suite(), name());
    }

    /**
     * Returns a {@link Comperator} that can be used to sort tests in natural order.
     *
     * @return a {@code Comperator}.
     */
    public static Comparator<Test> naturalOrder() {
        return (first, second) -> {
            int result = first.suite().name().compareTo(second.suite().name());
            return result != 0 ? result : first.name().compareTo(second.name());
        };
    }

    public Suite suite();

    public String name();

    /**
     * Returns all the suites that this test is in.
     *
     * @return all (grand) parent suites.
     */
    public Collection<Suite> getSuites();

    /**
     * Defines the test steps for this test.
     *
     * @param testEvents the test instructions.
     */
    public void setEvents(Collection<Event> testEvents);

    /**
     * Returns the test steps for this test.
     *
     * @return the test instructions.
     */
    public Collection<Event> getEvents();

    public void setTestListener(TestListener listener);

    public TestResult result();
}
