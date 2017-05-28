/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.fitnesse.engineplugin;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

public class FixtureLibraryTest {

    private Object caller;
    private FixtureLibrary a;

    @Before
    public void setup() {
        caller = new Object();
        a = new FixtureLibrary("foo", caller);
    }

    @After
    public void teardown() {
        a = null;
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(a.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(a.equals(""));
    }

    @Test
    public void hashCode_DifferentNamespace() {
        FixtureLibrary b = new FixtureLibrary("oof", caller);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentNamespace() {
        FixtureLibrary b = new FixtureLibrary("oof", caller);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentCaller() {
        FixtureLibrary b = new FixtureLibrary("foo", new Object());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentCaller() {
        FixtureLibrary b = new FixtureLibrary("foo", new Object());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalCaller() {
        FixtureLibrary b = new FixtureLibrary("foo", caller);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalCaller() {
        FixtureLibrary b = new FixtureLibrary("foo", caller);
        assertEquals(a, b);
    }
}
