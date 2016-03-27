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
package org.mazarineblue.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class IDTest {

    @Test
    public void toString_ReturnsString() {
        ID id = new ID();
        String actual = id.toString();
        String expected = "" + ID.count();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_Matches() {
        ID id = new ID();
        assertEquals(355 + ID.count(), id.hashCode());
    }

    @Test
    public void equals_Different_DoesntMatch() {
        assertEquals(false, new ID(1).equals(new ID(2)));
    }

    @Test
    public void equals_Equivalent_DoesntMatch() {
        assertEquals(true, new ID(1).equals(new ID(1)));
    }
}
