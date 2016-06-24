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
package org.mazarineblue.pictures.compounders;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public interface PixelComperator {

    static final int ALPHA = 24;
    static final int RED = 16;
    static final int GREEN = 8;
    static final int BLUE = 0;

    public abstract boolean isPixelEqual(int leftPixel, int rightPixel);

    static boolean isPixelPartEqual(int leftPixel, int rightPixel, int part) {
        int a = getPixelPart(leftPixel, part);
        int b = getPixelPart(rightPixel, part);
        return getPixelPart(leftPixel, part) == getPixelPart(rightPixel, part);
    }

    static int getPixelPart(int argb, int part) {
        return (argb >> part) & 0xFF;
    }
}
