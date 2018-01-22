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
package org.mazarineblue.libraries.test.model.suites;

import java.util.Collection;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.Key;

/**
 * A {@code Suite} is group of tests and contains two collections of
 * instructions, one which must be executed before running a test and which
 * must be run after a test is run.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Suite {

    /**
     * Creates a instance of a default implementation of a {@code Suite}.
     *
     * @param parent the parent suite of this suite or {@code null}.
     * @param name the name of the suite.
     * @return the created instance.
     */
    public static Suite newInstance(Suite parent, String name) {
        return new DefaultSuite(parent, name);
    }

    public default Key getKey() {
        return this instanceof Key ? (Key) this : new SuiteKey(name());
    }

    public String name();

    /**
     * Returns the parent suite of this suite.
     *
     * @return {@code null} if this suite has no parent.
     */
    public Suite parent();

    /**
     * Indicates if this suite contains a collection of instructions, which
     * need to run before a {@link Test} is run.
     *
     * @return {@code true} is this suite contains a setup.
     */
    public boolean containsSetup();

    /**
     * Set a collection of instructions as setup instructions for this suite,
     * so that they are run before a {@link Test} is run.
     *
     * @param c the setup instruction to set
     */
    public void setSetup(Collection<Event> c);

    /**
     * Fetches the setup instructions, which need to rune before a {@link Test}
     * is run.
     *
     * @return a collection of setup instructions.
     */
    public Collection<Event> getSetup();

    /**
     * Indicates if this suite contains a collection of instructions, which
     * need to run after a {@link Test} has run.
     *
     * @return {@code true} is this suite contains a setup.
     */
    public boolean containsTeardown();

    /**
     * Set a collection of instructions as teardown instructions for this
     * suite, so that they are run after a {@link Test} have run.
     *
     * @param c the setup instruction to set
     */
    public void setTeardown(Collection<Event> c);

    /**
     * Fetches the teardown instructions, which need to rune before a
     * {@link Test} is run.
     *
     * @return a collection of teardown instructions.
     */
    public Collection<Event> getTeardown();
}
