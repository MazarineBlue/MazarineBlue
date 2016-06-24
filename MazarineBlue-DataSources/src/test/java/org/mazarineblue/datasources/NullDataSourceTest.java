/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.datasources;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_SET;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class NullDataSourceTest {

    private NullDataSource source;

    @Before
    public void setup() {
        source = new NullDataSource("Null");
    }

    @Test
    public void getSourceIdentifier_ReturnsNull() {
        assertEquals(asList("Null"), source.getSourceIdentifiers());
    }

    @Test
    public void getIndex() {
        int index = source.getIndex("foo");
        assertEquals(-1, index);
    }

    @Test
    public void getDataColumn_ReturnsNull() {
        assertEquals(null, source.getData("foo"));
    }

    @Test
    public void getDataIndex_ReturnsNull() {
        assertEquals(null, source.getData(1));
    }

    @Test
    public void setDataColumn_ReturnsTrue() {
        assertEquals(true, source.setData("foo", ""));
    }

    @Test
    public void setDataIndex_ReturnsTrue() {
        assertEquals(true, source.setData(1, ""));
    }

    @Test
    public void getColumn_ReturnsEmptyArray() {
        assertEquals(EMPTY_SET, source.getColumns());
    }

    @Test
    public void hasNext() {
        assertEquals(true, source.hasNext());
    }

    @Test
    public void next() {
        source.next();
        assertEquals(true, source.isInitilized());
    }

    @Test
    public void reset() {
        source.reset();
        assertEquals(false, source.isInitilized());
    }

    @Test
    public void getLineIdentifier_WithoutCallingNext_ThrowsException() {
        assertEquals(asList("Null:-1"), source.getLineIdentifiers());
    }

    @Test
    public void getLineIdentifier_AfterCallingNext_ReturnsEmptyIdentifier() {
        source.next();
        assertEquals(asList("Null:0"), source.getLineIdentifiers());
    }
}
