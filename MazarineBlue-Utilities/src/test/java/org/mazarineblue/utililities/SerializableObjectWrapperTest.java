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
package org.mazarineblue.utililities;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.utililities.util.SerializeTestUtil;

public class SerializableObjectWrapperTest {

    private SerializableObjectWrapper obj;

    @Before
    public void setup() {
        obj = new SerializableObjectWrapper(1);
    }

    @After
    public void teardown() {
        obj = null;
    }

    @Test
    public void toString_Integer() {
        assertEquals("1", obj.toString());
    }

    @Test
    public void getObject_Integer() {
        assertEquals(1, obj.getObject());
    }

    @Test
    public void getWrappedClass_Integer() {
        assertEquals(Integer.class, obj.getWrappedClass());
    }

    @Test
    public void serialization()
            throws Exception {
        assertEquals(obj, SerializeTestUtil.copy(obj));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(obj.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(obj.equals(""));
    }

    @Test
    public void hashCode_DifferentContent() {
        SerializableObjectWrapper b = new SerializableObjectWrapper(2);
        assertNotEquals(obj.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentContent() {
        assertFalse(obj.equals(new SerializableObjectWrapper(2)));
    }

    @Test
    public void hashCode_IdenticalContent() {
        SerializableObjectWrapper b = new SerializableObjectWrapper(1);
        assertEquals(obj.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        assertTrue(obj.equals(new SerializableObjectWrapper(1)));
    }
}
