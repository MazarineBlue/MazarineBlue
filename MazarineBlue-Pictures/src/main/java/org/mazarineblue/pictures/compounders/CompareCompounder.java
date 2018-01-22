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
import org.mazarineblue.utilities.exceptions.NeverThrownException;

/**
 * An {@code CompareCompounder} is a {@code Compounder} that compares two
 * {@link Picture pictures}, using a {@link PixelComperator} and report if they
 * the two {@code pictures} are deemed identical or not.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CompareCompounder
        extends Compounder<CompareCompounder.Result> {

    private Result result;
    private boolean stop = false;

    private final PixelComperator pixelComperator;

    /**
     * The result of {@link #compound()}.
     */
    @SuppressWarnings("PublicInnerClass")
    public enum Result {

        /**
         * Indicates both pictures are equal.
         */
        EQUAL,
        /**
         * Indicates both pictures have different width.
         */
        DIFF_WIDTH,
        /**
         * Indicates both pictures have different height.
         */
        DIFF_HEIGHT,
        /**
         * Indicates both pictures have different width and height.
         */
        DIFF_WIDTH_AND_HEIGHT,
        /**
         * Indicates both pictures have different width and height.
         */
        DIFF_CONTENT,
        /**
         * Indicates some failure comparing both pictures.
         */
        FAIL,
    }

    /**
     * Constructs an {@link Compounder compounder} capable of testing the
     * equality of the two specified pictures using the specified
     * {@code PixelComperator}.
     *
     * @param left            the first picture.
     * @param right           the second picture.
     * @param pixelComperator the comperator to use in the equality test.
     */
    public CompareCompounder(Picture left, Picture right, PixelComperator pixelComperator) {
        super(left, right);
        this.pixelComperator = pixelComperator;
        result = Result.EQUAL;
    }

    @Override
    protected void compoundDimension() {
        compareWidthOrHeight(left.getWidth(), right.getWidth(), Result.DIFF_WIDTH);
        compareWidthOrHeight(left.getHeight(), right.getHeight(), Result.DIFF_HEIGHT);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for compoundDimension()">
    private void compareWidthOrHeight(int expected, int actual, Result type) {
        switch (result) {
            case EQUAL:
                if (expected != actual)
                    result = type;
                break;
            case DIFF_WIDTH:
            case DIFF_HEIGHT:
                if (expected != actual)
                    result = Result.DIFF_WIDTH_AND_HEIGHT;
                break;
            default:
                throw new UnsupportedOperationException("Type: " + type + " is unsupported.");
        }
    }
    // </editor-fold>

    @Override
    protected boolean shouldCompoundContent() {
        return isWidthEqual() && isHeightEqual();
    }

    private boolean isWidthEqual() {
        return left.getWidth() == right.getWidth();
    }

    private boolean isHeightEqual() {
        return left.getHeight() == right.getHeight();
    }

    @Override
    protected void compoundNone(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new NeverThrownException();
    }

    @Override
    protected void compoundRight(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new NeverThrownException();
    }

    @Override
    protected void compoundLeft(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new NeverThrownException();
    }

    @Override
    protected void compoundUsingBothPixels(int x, int y) {
        int expectedPixel = left.getPixel(x, y);
        int actualPixel = right.getPixel(x, y);
        if (pixelComperator.isPixelEqual(expectedPixel, actualPixel))
            return;
        result = Result.DIFF_CONTENT;
        stop = true;
    }

    @Override
    protected boolean stopComputing() {
        return stop;
    }

    @Override
    public Result getResult() {
        return result;
    }
}
