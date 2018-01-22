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

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.pictures.compounders.PixelComperator;
import org.mazarineblue.pictures.compounders.comperators.FullPixelComperator;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FullPixelComperatorTest {

    private PixelComperator comperator;

    @Before
    public void setup() {
        comperator = new FullPixelComperator();
    }

    @After
    public void teardown() {
        comperator = null;
    }

    @Test
    public void isPixelEqual_DifferentAlphaValues_ReturnsFalse() {
        int left = PixelUtil.pixel(1, 2, 3, 4);
        int right = PixelUtil.pixel(2, 2, 3, 4);
        assertFalse(comperator.isPixelEqual(left, right));
    }

    @Test
    public void isPixelEqual_DifferentRedValues_ReturnsFalse() {
        int left = PixelUtil.pixel(1, 2, 3, 4);
        int right = PixelUtil.pixel(1, 3, 3, 4);
        assertFalse(comperator.isPixelEqual(left, right));
    }

    @Test
    public void isPixelEqual_DifferentGreenValues_ReturnsFalse() {
        int left = PixelUtil.pixel(1, 2, 3, 4);
        int right = PixelUtil.pixel(1, 2, 4, 4);
        assertFalse(comperator.isPixelEqual(left, right));
    }

    @Test
    public void isPixelEqual_DifferentBlueValues_ReturnsFalse() {
        int left = PixelUtil.pixel(1, 2, 3, 4);
        int right = PixelUtil.pixel(1, 2, 3, 5);
        assertFalse(comperator.isPixelEqual(left, right));
    }

    @Test
    public void isPixelEqual_IdenticalValues_ReturnsTrue() {
        int left = PixelUtil.pixel(1, 2, 3, 4);
        int right = PixelUtil.pixel(1, 2, 3, 4);
        assertTrue(comperator.isPixelEqual(left, right));
    }
}
