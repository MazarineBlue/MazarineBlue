/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.pictures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RasterTest
        extends TestHashCodeAndEquals<Object> {

    @Test
    public void toString_ReturnsWidthAndHeight() {
        Raster raster = new Raster(3, 4);
        assertEquals("width=3, height=4", raster.toString());
    }

    @Override
    protected Object getObject() {
        return new Raster(4, 4);
    }

    @Override
    protected Object getDifferentObject() {
        return new Raster(4, 3);
    }

    @Test
    public void hashCode_DifferentHeigth_ReturnFalse() {
        int a = getObject().hashCode();
        int b = new Raster(3, 4).hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentContent_ReturnFalse() {
        int a = getObject().hashCode();
        Raster b = new Raster(4, 4);
        b.setRGB(0, 0, 127);
        assertNotEquals(a, b.hashCode());
    }

    @Test
    public void equals_DifferentHeigth_ReturnFalse() {
        Object a = getObject();
        Raster b = new Raster(3, 4);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_DifferentContent_ReturnFalse() {
        Object a = getObject();
        Raster b = new Raster(4, 4);
        b.setRGB(0, 0, 127);
        assertFalse(a.equals(b));
    }
}
