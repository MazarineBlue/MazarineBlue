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
package org.mazarineblue.plugins;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;

public class LibraryPluginExceptionTest {

    private static LibraryPluginException EX;
    private static Object DIFFERENT_CLASSES;
    private static LibraryPluginException DIFFERENT_FILES;
    private static LibraryPluginException IDENTICAL_FILES;

    @BeforeClass
    public static void setup() {
        EX = new LibraryPluginException("foo");
        DIFFERENT_CLASSES = "";
        DIFFERENT_FILES = new LibraryPluginException("oof");
        IDENTICAL_FILES = new LibraryPluginException("foo");
    }

    @AfterClass
    public static void teardown() {
        EX = DIFFERENT_FILES = IDENTICAL_FILES = null;
        DIFFERENT_CLASSES = null;
    }

    @Test
    public void hash_IdenticalNamespaces_GiveDifferentHashCodes() {
        assertNotEquals(DIFFERENT_FILES.hashCode(), EX.hashCode());
    }

    @Test
    public void hash_IdenticalNamespaces_GiveTheSameHashCodes() {
        assertEquals(IDENTICAL_FILES.hashCode(), EX.hashCode());
    }

    @Test
    public void equals_Null_ReturnFalse() {
        assertFalse(EX.equals(null));
    }

    @Test
    public void equals_DifferentClasses_ReturnFalse() {
        assertFalse(EX.equals(DIFFERENT_CLASSES));
    }

    @Test
    public void equals_DifferentNamespaces_ReturnFalse() {
        assertFalse(EX.equals(DIFFERENT_FILES));
    }

    @Test
    public void equals_IdenticalNamespaces_ReturnTrue() {
        assertTrue(EX.equals(IDENTICAL_FILES));
    }
}
