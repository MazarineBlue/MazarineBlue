/*
 * Copyright (c) 2016 Alex de Kruijff
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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class IDTest {

    @Test
    public void toString_ReturnsString() {
        ID id = new ID();
        String actual = id.toString();
        String expected = "" + ID.getCount();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode {

        private final ID a;

        public EqualsAndHashCode() {
            a = new ID(1);
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
        public void hashCode_DifferentID() {
            final ID b = new ID(2);
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentID() {
            final ID b = new ID(2);
            assertFalse(a.equals(b));
        }

        @Test
        public void hashCode_EquivalentID() {
            ID b = new ID(1);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_EquivalentID() {
            ID b = new ID(1);
            assertTrue(a.equals(b));
        }
    }
}
