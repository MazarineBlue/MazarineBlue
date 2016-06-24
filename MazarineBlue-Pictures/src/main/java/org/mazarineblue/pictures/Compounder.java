/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.pictures;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Compounder {

    protected final Raster left, right;
    protected final int width;
    protected final int height;

    protected Compounder(Raster left, Raster right) {
        this.left = left;
        this.right = right;
        width = left.width > right.width ? left.width : right.width;
        height = left.height > right.height ? left.height : right.height;
    }
    
    public void computeScreenshot() {
        compareWidth();
        compareHeight();
        if (shouldComputeContent())
            computeContent();
    }

    protected abstract void compareWidth();

    protected abstract void compareHeight();
    
    protected abstract boolean shouldComputeContent();

    private void computeContent() {
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x) {
                compute(x, y);
                if (stopComputingContent())
                    return;
            }
    }

    private void compute(int x, int y) {
        if (areBothBeyondBoundries(x, y))
            computeNone(x, y);
        else if (isLeftBeyondTheBoundry(x, y))
            computeRight(x, y);
        else if (isRightBeyondTheBoundry(x, y))
            computeLeft(x, y);
        else
            computeBoth(x, y);
    }

    private boolean areBothBeyondBoundries(int x, int y) {
        boolean left = isLeftBeyondTheBoundry(x, y);
        boolean right = isRightBeyondTheBoundry(x, y);
        return left && right;
    }

    private boolean isLeftBeyondTheBoundry(int x, int y) {
        return x >= left.width || y >= left.height;
    }

    private boolean isRightBeyondTheBoundry(int x, int y) {
        return x >= right.width || y >= right.height;
    }

    protected abstract void computeNone(int x, int y);

    protected abstract void computeRight(int x, int y);

    protected abstract void computeLeft(int x, int y);

    protected abstract void computeBoth(int x, int y);

    protected abstract boolean stopComputingContent();
}
