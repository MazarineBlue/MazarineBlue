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
public interface CompoundMethod {

    /**
     * This method computes some value based on the specified pixels.
     */
    int compute(int leftPixel, int rightPixel);

    /**
     * This method computes some value based on the specified pixel. This 
     * method should only be called when only the right pixel is unavailable.
     */
    int computeLeft(int leftPixel);

    /**
     * This method computes some value based on the specified pixel. This 
     * method should only be called when only the left pixel is unavailable.
     */
    int computeRigth(int rightPixel);

    /**
     * This method computes some value without any pixels. This method should
     * only be called when there both pixels are unavailable.
     */
    public int computeNeither();
}
