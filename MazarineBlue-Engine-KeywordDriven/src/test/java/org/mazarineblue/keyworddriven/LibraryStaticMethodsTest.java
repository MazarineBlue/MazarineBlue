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
package org.mazarineblue.keyworddriven;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.keyworddriven.Library.matchesAny;
import static org.mazarineblue.keyworddriven.Library.matchesAnyNamespace;
import static org.mazarineblue.keyworddriven.Library.matchesNone;
import static org.mazarineblue.keyworddriven.Library.matchesNoneNamespace;
import org.mazarineblue.keyworddriven.util.libraries.DummyLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;

public class LibraryStaticMethodsTest {

    @Test
    public void matchesAny_SameLibrary() {
        assertTrue(matchesAny(DummyLibrary.class)
                .test(new DummyLibrary("")));
    }

    @Test
    public void matchesAny_GenericLibrary() {
        assertTrue(matchesAny(Library.class)
                .test(new DummyLibrary("")));
    }

    @Test
    public void matchesAny_DifferentLibrary() {
        assertFalse(matchesAny(TestLibrary1.class)
                .test(new DummyLibrary("")));
    }

    @Test
    public void matchesNone_DifferentLibrary() {
        assertTrue(matchesNone(TestLibrary1.class)
                .test(new DummyLibrary("")));
    }

    @Test
    public void matchesNone_SameLibrary() {
        assertFalse(matchesNone(DummyLibrary.class)
                .test(new DummyLibrary("")));
    }

    @Test
    public void matchesAnyNamespace_SameNamespace() {
        assertTrue(matchesAnyNamespace("foo")
                .test(new DummyLibrary("foo")));
    }

    @Test
    public void matchesAnyNamespace_DifferentNamespace() {
        assertFalse(matchesAnyNamespace("bar")
                .test(new DummyLibrary("foo")));
    }

    @Test
    public void matchesNoneNamespace_DifferentNamespace() {
        assertTrue(matchesNoneNamespace("bar")
                .test(new DummyLibrary("foo")));
    }

    @Test
    public void matchesNoneNamespace_SameNamespace() {
        assertFalse(matchesNoneNamespace("foo")
                .test(new DummyLibrary("foo")));
    }
}
