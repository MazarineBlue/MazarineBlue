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

import java.io.File;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;

public class UnableToReadFileExceptionTest {

    private static FileUnreadableException EX;
    private static Object DIFFERENT_CLASSES;
    private static FileUnreadableException DIFFERENT_FILES;
    private static FileUnreadableException IDENTICAL_FILES;

    @BeforeClass
    public static void setup() {
        EX = new FileUnreadableException(new File("foo"), new Exception());
        DIFFERENT_CLASSES = "";
        DIFFERENT_FILES = new FileUnreadableException(new File("oof"), new Exception());
        IDENTICAL_FILES = new FileUnreadableException(new File("foo"), new Exception());
    }

    @AfterClass
    public static void teardown() {
        EX = DIFFERENT_FILES = IDENTICAL_FILES = null;
        DIFFERENT_CLASSES = null;
    }

    @Test
    public void hash_IdenticalFiles_GiveDifferentHashCodes() {
        assertNotEquals(DIFFERENT_FILES.hashCode(), EX.hashCode());
    }

    @Test
    public void hash_IdenticalFiles_GiveTheSameHashCodes() {
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
    public void equals_DifferentFiles_ReturnFalse() {
        assertFalse(EX.equals(DIFFERENT_FILES));
    }

    @Test
    public void equals_IdenticalFiles_ReturnTrue() {
        assertTrue(EX.equals(IDENTICAL_FILES));
    }
}
