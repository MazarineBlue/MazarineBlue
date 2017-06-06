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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.mazarineblue.utililities.exceptions.UnclonableObjectException;
import org.mazarineblue.utililities.exceptions.WrapperException;
import org.mazarineblue.utililities.util.ImmutableArray;
import org.mazarineblue.utililities.util.ImmutableChildAndFieldAndParent;
import org.mazarineblue.utililities.util.ImmutableChildAndMutableParent;
import org.mazarineblue.utililities.util.ImmutableObjectWithImmutableField;
import org.mazarineblue.utililities.util.ImmutableObjectWithMutableField;
import org.mazarineblue.utililities.util.MutableArray;
import org.mazarineblue.utililities.util.MutableObject;

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
    public void deepcopy_MutableObjectWithPrimative() {
        MutableObject x = new MutableObject();
        MutableObject y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertNotSame(x, y);
    }

    @Test
    public void deepcopy_ImmutableObjectWithMutableField() {
        ImmutableObjectWithMutableField x = new ImmutableObjectWithMutableField();
        ImmutableObjectWithMutableField y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertNotSame(x, y);
    }

    @Test
    public void deepcopy_ImmutableChildObjectWithMutableParent() {
        ImmutableChildAndMutableParent x = new ImmutableChildAndMutableParent();
        ImmutableChildAndMutableParent y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertNotSame(x, y);
    }

    @Test
    public void deepcopy_ImmutableObject() {
        ImmutableObjectWithImmutableField x = new ImmutableChildAndFieldAndParent();
        ImmutableObjectWithImmutableField y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertSame(x, y);
    }

    @Test
    public void deepcopy_MutableArray() {
        MutableArray x = new MutableArray();
        MutableArray y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertNotSame(x, y);
    }

    @Test
    public void deepcopy_ImmutableArray() {
        ImmutableArray x = new ImmutableArray();
        ImmutableArray y = new ObjectsUtil<>(x).deepcopy().get();
        assertEquals(x, y);
        assertSame(x, y);
    }

    @Test
    public void assertEquals_Null_Null() {
        assertNull(new ObjectsUtil<>(null).assertEquals(null).get());
    }

    @Test(expected = WrapperException.class)
    public void assertEquals_Null_String() {
        new ObjectsUtil<>(null).assertEquals("");
    }

    @Test(expected = WrapperException.class)
    public void assertEquals_String_Null() {
        new ObjectsUtil<>("").assertEquals(null);
    }

    @Test(expected = WrapperException.class)
    public void assertEquals_DifferentClasses() {
        new ObjectsUtil<>(new Object()).assertEquals("");
    }

    @Test(expected = WrapperException.class)
    public void assertEquals_DifferentContent() {
        new ObjectsUtil<>("abc").assertEquals("");
    }

    @Test
    public void assertEquals_String_String() {
        assertEquals("abc", new ObjectsUtil<>("abc").assertEquals("abc").get());
    }

    @Test(expected = WrapperException.class)
    public void rainyIntegration() {
        SerializableObjectWrapper expected = new SerializableObjectWrapper("abc");
        new ObjectsUtil<>(expected).deepcopy().assertEquals(new SerializableObjectWrapper("def"));
    }

    @Test
    public void happyIntegration() {
        SerializableObjectWrapper expected = new SerializableObjectWrapper("abc");
        assertEquals(expected, new ObjectsUtil<>(expected).deepcopy().assertEquals(expected).get());
    }
}
