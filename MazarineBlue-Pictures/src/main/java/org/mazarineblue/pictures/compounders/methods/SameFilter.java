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
package org.mazarineblue.pictures.compounders.methods;

import org.mazarineblue.pictures.compounders.CompoundMethod;
import org.mazarineblue.pictures.compounders.PixelComperator;

/**
 * A {@code SameFilter} is a {@link CompoundMethod} that filters out pixels
 * that are identical.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SameFilter
        implements CompoundMethod {

    private static final int TRANSPARENT_PIXEL = 0;

    protected final PixelComperator comparator;

    /**
     * Constructs a {@code SameFilter} that uses the specified
     * {@link PixelComparator} to determine which pixels are identical an which
     * doesn't.
     *
     * @param comparator the pixel comparator.
     */
    public SameFilter(PixelComperator comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compoundBoth(int leftPixel, int rightPixel) {
        boolean same = comparator.isPixelEqual(leftPixel, rightPixel);
        return same ? leftPixel : TRANSPARENT_PIXEL;
    }

    @Override
    public int compoundLeft(int leftPixel) {
        return TRANSPARENT_PIXEL;
    }

    @Override
    public int compoundRigth(int rightPixel) {
        return TRANSPARENT_PIXEL;
    }

    @Override
    public int compoundNeither() {
        return TRANSPARENT_PIXEL;
    }
}
