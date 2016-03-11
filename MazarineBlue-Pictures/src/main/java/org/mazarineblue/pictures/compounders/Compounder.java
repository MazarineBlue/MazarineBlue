/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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

import static java.lang.Math.max;
import org.mazarineblue.pictures.Picture;

/**
 * A {@code Compounder} is a object that combines two pictures.
 * <p>
 * The abstract base class contains the main algorithm and provide hooks that
 * have to be implemented by concrete classes.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of object to return.
 */
public abstract class Compounder<T> {

    protected final Picture left;
    protected final Picture right;
    protected final int width;
    protected final int height;

    protected Compounder(Picture left, Picture right) {
        this.left = left;
        this.right = right;
        width = max(left.getWidth(), right.getWidth());
        height = max(left.getHeight(), right.getHeight());
    }

    /**
     * Compounds both pictures.
     *
     * @return the result, as specified by the concrete class.
     */
    public T compound() {
        compoundDimension();
        if (shouldCompoundContent())
            compoundContent();
        return getResult();
    }

    /**
     * Computes both pictures dimensions.
     */
    protected abstract void compoundDimension();

    /**
     * Test if the content should be compounded.
     *
     * @return {@code true} if the content should compounded.
     */
    protected abstract boolean shouldCompoundContent();

    private void compoundContent() {
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x) {
                Compounder.this.compound(x, y);
                if (stopComputing())
                    return;
            }
    }

    private void compound(int x, int y) {
        if (areBothBeyondBoundries(x, y))
            compoundNone(x, y);
        else if (isLeftBeyondTheBoundry(x, y))
            compoundRight(x, y);
        else if (isRightBeyondTheBoundry(x, y))
            compoundLeft(x, y);
        else
            compoundUsingBothPixels(x, y);
    }

    private boolean areBothBeyondBoundries(int x, int y) {
        return isLeftBeyondTheBoundry(x, y) && isRightBeyondTheBoundry(x, y);
    }

    private boolean isLeftBeyondTheBoundry(int x, int y) {
        return x >= left.getWidth() || y >= left.getHeight();
    }

    private boolean isRightBeyondTheBoundry(int x, int y) {
        return x >= right.getWidth() || y >= right.getHeight();
    }

    /**
     * Compound using non of pixels.
     *
     * @param x the x coordinate of the pixel.
     * @param y the y coordinate of the pixel.
     */
    protected abstract void compoundNone(int x, int y);

    /**
     * Compound using only the pixel from the left picture.
     *
     * @param x the x coordinate of the pixel.
     * @param y the y coordinate of the pixel.
     */
    protected abstract void compoundLeft(int x, int y);

    /**
     * Compound using only the pixel from the right picture.
     *
     * @param x the x coordinate of the pixel.
     * @param y the y coordinate of the pixel.
     */
    protected abstract void compoundRight(int x, int y);

    /**
     * Compound using both pixels at the specified locations.
     *
     * @param x the x coordinate of the pixel.
     * @param y the y coordinate of the pixel.
     */
    protected abstract void compoundUsingBothPixels(int x, int y);

    /**
     * Test if the computing process should stop.
     *
     * @return {@code true} if computing should stop.
     */
    protected abstract boolean stopComputing();

    /**
     * Returns the result of the compounding.
     *
     * @return the result of the compounding.
     */
    protected abstract T getResult();
}
