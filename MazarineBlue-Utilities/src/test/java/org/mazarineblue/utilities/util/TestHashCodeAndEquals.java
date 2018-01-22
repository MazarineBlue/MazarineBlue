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
package org.mazarineblue.utilities.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public abstract class TestHashCodeAndEquals<T> {

    @Test
    public void hashCode_DifferentObjects() {
        int a = getObject().hashCode();
        int b = getDifferentObject().hashCode();
        if (isHashCodeImplemented(a, b))
            assertNotEquals(getMessage(), a, b);
    }

    public static boolean isHashCodeImplemented(int a, int b) {
        return a != 0 || b != 0;
    }

    @Test
    public void hashCode_IdenticalObjects() {
        int a = getObject().hashCode();
        int b = getIdenticalObject().hashCode();
        assertEquals(getMessage(), a, b);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        Object a = getObject();
        assertFalse(getMessage(), a.equals(null));
    }

    @Test
    public void equals_DifferentClasses() {
        Object a = getObject();
        assertFalse(getMessage(), a.equals(""));
    }

    @Test
    public void equals_DifferentObjects() {
        Object a = getObject();
        Object b = getDifferentObject();
        assertFalse(getMessage(), a.equals(b));
    }

    @Test
    public void equals_IdenticalObjects() {
        Object a = getObject();
        Object b = getIdenticalObject();
        assertTrue(getMessage(), a.equals(b));
    }

    @Test
    public void equals_SameObject() {
        Object a = getObject();
        assertTrue(getMessage(), a.equals(a));
    }

    protected T getIdenticalObject() {
        return getObject();
    }

    protected String getMessage() {
        return getClass().getName();
    }

    protected abstract T getObject();

    protected abstract T getDifferentObject();
}
