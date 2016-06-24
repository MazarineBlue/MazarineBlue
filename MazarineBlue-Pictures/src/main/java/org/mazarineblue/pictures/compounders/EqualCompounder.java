/*
 * Copyright (c) 2012-2014 Alex de Kruijff
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

import org.mazarineblue.pictures.Compounder;
import java.io.IOException;
import org.mazarineblue.pictures.Raster;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class EqualCompounder
        extends Compounder {

    private Result result;
    private boolean stop = false;

    private final PixelComperator pixelComperator;

    static public enum Result {

        EQUAL, FAIL, DIFF_WIDTH, DIFF_HEIGHT,
        DIFF_WIDTH_AND_HEIGHT, DIFF_CONTENT,
    }

    public EqualCompounder(Raster left, Raster right,
                           PixelComperator pixelComperator)
            throws IOException {
        super(left, right);
        this.pixelComperator = pixelComperator;
        result = Result.EQUAL;
    }

    @Override
    protected void compareWidth() {
        compareWidthOrHeight(left.getWidth(), right.getWidth(), Result.DIFF_WIDTH);
    }

    @Override
    protected void compareHeight() {
        compareWidthOrHeight(left.getHeight(), right.getHeight(), Result.DIFF_HEIGHT);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for compareWidth() and compareHeight()">
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
        }
    }
    // </editor-fold>

    @Override
    protected boolean shouldComputeContent() {
        return isWidthEqual() && isHeightEqual();
    }

    private boolean isWidthEqual() {
        return left.getWidth() == right.getWidth();
    }

    private boolean isHeightEqual() {
        return left.getHeight() == right.getHeight();
    }

    @Override
    protected void computeNone(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void computeRight(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void computeLeft(int x, int y) {
        /* This method is never called, because we only check the content if the
         * width and height of both screenshots are equal.
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    protected void computeBoth(int x, int y) {
        int expectedPixel = left.getRGB(x, y);
        int actualPixel = right.getRGB(x, y);
        if (pixelComperator.isPixelEqual(expectedPixel, actualPixel))
            return;
        result = Result.DIFF_CONTENT;
        stop = true;
    }

    @Override
    protected boolean stopComputingContent() {
        return stop;
    }

    public Result getComperation() {
        return result;
    }
}
