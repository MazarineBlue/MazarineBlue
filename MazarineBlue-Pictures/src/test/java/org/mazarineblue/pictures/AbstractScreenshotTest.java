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
package org.mazarineblue.pictures;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.junit.Before;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class AbstractScreenshotTest {

    static protected final int ALPHA = 255 << 24;
    static protected final int RED = 255 << 16;
    static protected final int GREEN = 255 << 8;
    static protected final int BLUE = 255;

    protected Dimension dimensionOverlap = new Dimension(2, 2);
    private Dimension dimensionLeft = new Dimension(2, 4);
    private Dimension dimensionRight = new Dimension(4, 2);
    private Dimension dimensionResult = new Dimension(4, 4);
    protected Picture left, right, check;

    static protected Picture createPicture(Dimension d, int... rgb)
            throws Exception {
        return PictureTest.createPictureByImage(d, rgb);
    }

    @Before
    public void setup()
            throws Exception {
        dimensionOverlap = new Dimension(2, 2);
        dimensionLeft = new Dimension(2, 4);
        dimensionRight = new Dimension(4, 2);
        dimensionResult = new Dimension(4, 4);

        left = createPicture(dimensionLeft, RED);
        right = createPicture(dimensionRight, RED);
        check = new BufferedImagePicture(createCheckImage(dimensionResult, dimensionOverlap));
    }

    private BufferedImage createCheckImage(Dimension size, Dimension overlap) {
        BufferedImage image = new BufferedImage(4, 4,
                                                BufferedImage.TYPE_4BYTE_ABGR);
        for (int x = 0; x < dimensionResult.width; ++x)
            for (int y = 0; y < dimensionResult.height; ++y)
                image.setRGB(x, y, getPixel(x, y));
        return image;
    }

    protected abstract int getPixel(int x, int y);
}
