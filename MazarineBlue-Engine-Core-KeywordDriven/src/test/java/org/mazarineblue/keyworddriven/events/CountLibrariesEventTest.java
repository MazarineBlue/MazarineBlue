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
package org.mazarineblue.keyworddriven.events;

import static java.lang.Math.random;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CountLibrariesEventTest {

    @Test
    public void responce() {
        int n = (int) round(random());
        List<Library> libraries = createLibraries(n);
        CountLibrariesEvent e = new CountLibrariesEvent();
        e.addToCount(libraries);
        assertEquals(n, e.getCount());
        assertEquals("count=" + n, e.responce());
    }

    @Test
    public void equals_Null() {
        CountLibrariesEvent a = new CountLibrariesEvent();
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClasses() {
        CountLibrariesEvent a = new CountLibrariesEvent();
        assertNotEquals(a, "");
    }

    @Test
    public void hashCode_DifferentCounts() {
        CountLibrariesEvent a = new CountLibrariesEvent();
        CountLibrariesEvent b = new CountLibrariesEvent();
        a.addToCount(createLibraries(1));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentCounts() {
        CountLibrariesEvent a = new CountLibrariesEvent();
        CountLibrariesEvent b = new CountLibrariesEvent();
        a.addToCount(createLibraries(1));
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentMatchers() {
        CountLibrariesEvent a = new CountLibrariesEvent(t -> true);
        CountLibrariesEvent b = new CountLibrariesEvent(t -> false);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentMatchers() {
        CountLibrariesEvent a = new CountLibrariesEvent(t -> true);
        CountLibrariesEvent b = new CountLibrariesEvent(t -> false);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_TwoEqualEvents() {
        int n = (int) round(random());
        CountLibrariesEvent a = new CountLibrariesEvent();
        CountLibrariesEvent b = new CountLibrariesEvent();
        a.addToCount(createLibraries(n));
        b.addToCount(createLibraries(n));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_TwoEqualEvents() {
        int n = (int) round(random());
        CountLibrariesEvent a = new CountLibrariesEvent();
        CountLibrariesEvent b = new CountLibrariesEvent();
        a.addToCount(createLibraries(n));
        b.addToCount(createLibraries(n));
        assertEquals(a, b);
    }

    private List<Library> createLibraries(int n) {
        List<Library> libraries = new ArrayList<>(n);
        for (int i = 0; i < n; ++i)
            libraries.add(new TestLibrary(""));
        return libraries;
    }

    private static class TestLibrary
            extends Library {

        private TestLibrary(String namespace) {
            super(namespace);
        }
    }
}
