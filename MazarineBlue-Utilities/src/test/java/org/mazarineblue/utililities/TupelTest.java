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

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TupelTest {

    private Tupel<Integer> tupel;

    @Before
    public void setup() {
        tupel = new Tupel<>(1);
    }

    @After
    public void teardown() {
        tupel = null;
    }

    @Test
    public void toString_Tupel_ReturnsArrayView() {
        assertEquals("[1]", tupel.toString());
    }

    @Test
    public void size_Returns_Size() {
        assertEquals(1, tupel.size());
    }

    @Test
    public void get_FirstElement_ReturnsFirstElement() {
        assertEquals(new Integer(1), tupel.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_SecondElement_ThrowsException() {
        tupel.get(1);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_TupleNull_AreNotEqual() {
        assertFalse(tupel.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_TupelNonTupel_AreNotEqual() {
        assertFalse(tupel.equals(1));
    }

    @Test
    public void equals_TupelDifferentTypeTupel_AreNotEqual() {
        Tupel<Boolean> t = new Tupel<>(false);
        assertFalse(tupel.equals(t));
    }

    @Test
    public void equals_TupelDifferentSizeTupel_AreNotEqual() {
        Tupel<Integer> t = new Tupel<>(1, 1);
        assertFalse(tupel.equals(t));
    }

    @Test
    public void equals_TupelEquivalentTupel_AreEqual() {
        Tupel<Integer> t = new Tupel<>(1);
        assertTrue(tupel.equals(t));
    }

    @Test
    public void hashCode_TupelNonEquivalentTupel_AreNotEqual() {
        Tupel<Boolean> t = new Tupel<>(false);
        assertNotEquals(tupel.hashCode(), t.hashCode());
    }

    @Test
    public void hashCode_TupelEquivalentTupel_AreEqual() {
        Tupel<Integer> t = new Tupel<>(1);
        assertEquals(tupel.hashCode(), t.hashCode());
    }
}
