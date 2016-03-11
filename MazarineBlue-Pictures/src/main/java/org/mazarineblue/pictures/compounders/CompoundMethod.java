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
 * A {@code CompoundMethod} has the capability to compound two pixels.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface CompoundMethod {

    /**
     * This method compounds some value based on the specified pixels.
     *
     * @param leftPixel  the left pixel to compound.
     * @param rightPixel the right pixel to compound.
     * @return the compounded value of both pixels.
     */
    public int compoundBoth(int leftPixel, int rightPixel);

    /**
     * This method compounds some value based on the specified pixel. This
     * method should only be called when only the right pixel is unavailable.
     *
     * @param leftPixel the left pixel to compound.
     * @return the compounded value with only a leftPixel.
     */
    public int compoundLeft(int leftPixel);

    /**
     * This method compounds some value based on the specified pixel. This
     * method should only be called when only the left pixel is unavailable.
     *
     * @param rightPixel the right pixel to compound.
     * @return the compounded value with only a rightPixel.
     */
    public int compoundRigth(int rightPixel);

    /**
     * This method compounds some value without any pixels. This method should
     * only be called when there both pixels are unavailable.
     *
     * @return the value for when there are no pixels to compound.
     */
    public int compoundNeither();
}
