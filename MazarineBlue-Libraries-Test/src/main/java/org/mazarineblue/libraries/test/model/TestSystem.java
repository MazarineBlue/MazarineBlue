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
package org.mazarineblue.libraries.test.model;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;

/**
 * A {@code TestSystem} contains a whole set of tests and contains the
 * functionality to run them.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface TestSystem {

    /**
     * Creates a default {@code TestSystem} that uses the specified invoker to run
     * the tests.
     *
     * @param invoker used to run the tests.
     * @return an instance of a {@code TestSystem}.
     */
    public static TestSystem newInstance(Invoker invoker) {
        return new DefaultTestSytem(invoker);
    }

    /**
     * Sets a {@code  TestListener} to report to when running the tests.
     *
     * @param listener the report sink.
     */
    public void setTestListener(TestListener listener);

    /**
     * Fetches the global suite, which is used when no suite is declared.
     *
     * @return the global suite.
     */
    public Suite getGlobalSuite();

    /**
     * Indicated if the specified suite is in this set.

     * @param suite the test to look for.
     * @return {@code true} if the specified test is in this set.
     */
    public boolean contains(Suite suite);

    /**
     * Fetches the suite with the specified key.
     *
     * @param key the key of suite fetched.
     * @return the requested suite.
     */
    public Suite getSuite(Key key);

    /**
     * Adds the specified suite to this set.
     *
     * @param suite the suite to add.
     */
    public void add(Suite suite);

    /**
     * Indicated if the specified test is in this set.
     *
     * @param test the test to look for.
     * @return {@code true} if the specified test is in this set.
     */
    public boolean contains(Test test);

    /**
     * Adds the specified test to this set.
     *
     * @param test the test to add.
     */
    public void add(Test test);

    /**
     * Runs all tests.
     */
    public void run();

    /**
     * Specifies that maximum amount of jobs that may have running at one time.
     *
     * @param maximumJobs the maximum amount of tests allowed to run at one time.
     */
    public void setMaximumJobs(int maximumJobs);
}
