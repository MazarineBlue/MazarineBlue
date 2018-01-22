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
package org.mazarineblue.pictures;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.utilities.util.TestUtilityClass;

public class PixelUtilTest
        extends TestUtilityClass {

    public PixelUtilTest() {
        super(PixelUtil.class);
    }

    @Test
    public void pixel() {
        int pixel = PixelUtil.pixel(1, 2, 3, 4);
        assertEquals(1, PixelUtil.getAlpha(pixel));
        assertEquals(2, PixelUtil.getRed(pixel));
        assertEquals(3, PixelUtil.getGreen(pixel));
        assertEquals(4, PixelUtil.getBlue(pixel));
    }

    @Test
    public void set() {
        int pixel = PixelUtil.setAlpha(0, 1);
        pixel = PixelUtil.setRed(pixel, 2);
        pixel = PixelUtil.setGreen(pixel, 3);
        pixel = PixelUtil.setBlue(pixel, 4);
        pixel = PixelUtil.setAlpha(pixel, 5);
        assertEquals(5, PixelUtil.getAlpha(pixel));
        assertEquals(2, PixelUtil.getRed(pixel));
        assertEquals(3, PixelUtil.getGreen(pixel));
        assertEquals(4, PixelUtil.getBlue(pixel));
    }
}
