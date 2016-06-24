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
public class ScreenshotsCompounder
        extends Compounder {

    static private final int ALPHA = 0;
    private final CompoundMethod method;
    private final Raster result;

    public ScreenshotsCompounder(Raster left, Raster rigth,
                                 CompoundMethod method)
            throws IOException {
        super(left, rigth);
        this.method = method;
        result = new Raster(width, height);
    }

    @Override
    protected void compareWidth() {
    }

    @Override
    protected void compareHeight() {
    }

    @Override
    protected boolean shouldComputeContent() {
        return true;
    }

    @Override
    protected void computeNone(int x, int y) {
        int computedPixel = method.computeNeither();
        result.setRGB(x, y, computedPixel);
    }

    @Override
    protected void computeRight(int x, int y) {
        int rightPixel = right.getRGB(x, y);
        int computedPixel = method.computeRigth(rightPixel);
        result.setRGB(x, y, computedPixel);
    }

    @Override
    protected void computeLeft(int x, int y) {
        int leftPixel = left.getRGB(x, y);
        int computedPixel = method.computeLeft(leftPixel);
        result.setRGB(x, y, computedPixel);
    }

    @Override
    protected void computeBoth(int x, int y) {
        int exectedPixel = left.getRGB(x, y);
        int actualPixel = right.getRGB(x, y);
        int computedPixel = method.compute(exectedPixel, actualPixel);
        result.setRGB(x, y, computedPixel);
    }

    @Override
    protected boolean stopComputingContent() {
        return false;
    }

    public Raster getComperation() {
        return result;
    }
}
