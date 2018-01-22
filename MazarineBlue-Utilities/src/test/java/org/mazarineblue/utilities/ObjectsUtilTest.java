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
package org.mazarineblue.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.mazarineblue.utilities.exceptions.NotEqualException;
import org.mazarineblue.utilities.exceptions.UnclonableObjectException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ObjectsUtilTest {

    private static class LeakingButAutoCleaningLoop
            implements AutoCloseable {

        private LeakingButAutoCleaningLoop loop;

        @SuppressWarnings("LeakingThisInConstructor")
        private LeakingButAutoCleaningLoop() {
            loop = this;
        }

        @Override
        public void close() {
            loop = null;
        }
    }

    @Test
    public void clone_SerializableClonable() {
        SerializableClonable x = new TestSerializableClonable();
        Object y = ObjectsUtil.clone(x);
        assertEquals(x, y);
    }

    @Test(expected = UnclonableObjectException.class)
    public void deepcopy_Object() {
        Object x = new Object();
        Object y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertSame(x, y);
    }

    @Test
    public void deepcopy_String() {
        String x = "abc";
        String y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertSame(x, y);
    }

    @Test
    public void deepcopy_Integer() {
        Integer x = 1;
        Integer y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertSame(x, y);
    }

    @Test
    public void assertEquals_Null_Null() {
        assertNull(new ObjectsUtil<>(null).assertEquals(null).get());
    }

    @Test(expected = NotEqualException.class)
    public void assertEquals_Null_String() {
        new ObjectsUtil<>(null).assertEquals("");
    }

    @Test(expected = NotEqualException.class)
    public void assertEquals_String_Null() {
        new ObjectsUtil<>("").assertEquals(null);
    }

    @Test(expected = NotEqualException.class)
    public void assertEquals_DifferentClasses() {
        new ObjectsUtil<>(new Object()).assertEquals("");
    }

    @Test(expected = NotEqualException.class)
    public void assertEquals_DifferentContent() {
        new ObjectsUtil<>("abc").assertEquals("");
    }

    @Test
    public void assertEquals_String_String() {
        assertEquals("abc", new ObjectsUtil<>("abc").assertEquals("abc").get());
    }
}
