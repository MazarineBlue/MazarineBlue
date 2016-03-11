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
package org.mazarineblue.pictures.compounders;

/**
 * A {@code PixelComparator} test the equality of two pixels.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@FunctionalInterface
public interface PixelComperator {

    /**
     * Test if two specified pixels are equal.
     *
     * @param leftPixel  one pixel to use in the test.
     * @param rightPixel one pixel to use in the test.
     * @return {@code true} if both pixels are equivalent.
     */
    public abstract boolean isPixelEqual(int leftPixel, int rightPixel);
}
