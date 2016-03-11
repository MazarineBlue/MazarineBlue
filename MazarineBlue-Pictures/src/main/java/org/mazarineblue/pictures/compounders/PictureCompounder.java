/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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

import org.mazarineblue.pictures.Picture;

/**
 * An {@code PictureCompounder} is a {@code Compounder} that compound two
 * {@link Picture pictures} using the specified {@link CompoundMethod}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PictureCompounder
        extends Compounder<Picture> {

    private final CompoundMethod method;
    private final Picture result;

    /**
     * Constructs an {@link Compounder compounder} capable of creating a
     * {@code Picture picture}, based on the two specified {@code pictures},
     * using the specified {@code CompoundMethod}.
     *
     * testing the
     * equality of the two specified pictures using the specified
     * {@code PixelComperator}.
     *
     * @param left   the first picture.
     * @param right  the second picture.
     * @param method the method used to compound both images.
     */
    public PictureCompounder(Picture left, Picture right, CompoundMethod method) {
        super(left, right);
        this.method = method;
        result = new Picture(width, height);
    }

    @Override
    protected void compoundDimension() {
        // There's no need to compound the dimension.
    }

    @Override
    protected boolean shouldCompoundContent() {
        return true;
    }

    @Override
    protected void compoundNone(int x, int y) {
        int computedPixel = method.compoundNeither();
        result.setPixel(x, y, computedPixel);
    }

    @Override
    protected void compoundRight(int x, int y) {
        int rightPixel = right.getPixel(x, y);
        int computedPixel = method.compoundRigth(rightPixel);
        result.setPixel(x, y, computedPixel);
    }

    @Override
    protected void compoundLeft(int x, int y) {
        int leftPixel = left.getPixel(x, y);
        int computedPixel = method.compoundLeft(leftPixel);
        result.setPixel(x, y, computedPixel);
    }

    @Override
    protected void compoundUsingBothPixels(int x, int y) {
        int exectedPixel = left.getPixel(x, y);
        int actualPixel = right.getPixel(x, y);
        int computedPixel = method.compoundBoth(exectedPixel, actualPixel);
        result.setPixel(x, y, computedPixel);
    }

    @Override
    protected boolean stopComputing() {
        return false;
    }

    @Override
    public Picture getResult() {
        return result;
    }
}
