/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mazarineblue.pictures.PictureTest.createPictureByImage;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ComperatorMethodsTest {

    private static final int ALPHA = 255 << 24;
    private static final int RED = 255 << 16;
    private static final int GREEN = 255 << 8;
    private static final int BLUE = 255;

    @SuppressWarnings("PublicInnerClass")
    public abstract static class TestAbstractScreenshot {

        protected Dimension dimensionOverlap = new Dimension(2, 2);
        private Dimension dimensionLeft = new Dimension(2, 4);
        private Dimension dimensionRight = new Dimension(4, 2);
        private Dimension dimensionResult = new Dimension(4, 4);
        protected Picture left;
        protected Picture right;
        protected Picture check;

        @Before
        public void setup()
                throws IOException {
            dimensionLeft = getLeftDimension();
            dimensionRight = getRightDimension();
            dimensionOverlap = min(dimensionLeft, dimensionRight);
            dimensionResult = max(dimensionLeft, dimensionRight);
            left = createLeftPicture(dimensionLeft);
            right = createRightPicture(dimensionRight);
            check = ImageUtil.createPicture(createCheckImage(dimensionResult, dimensionOverlap));
        }

        @After
        public void teardown() {
            dimensionLeft = dimensionRight = dimensionOverlap = dimensionResult = null;
            left = right = check = null;
        }

        protected Dimension getLeftDimension() {
            return new Dimension(2, 4);
        }

        protected Dimension getRightDimension() {
            return new Dimension(4, 2);
        }

        private Dimension min(Dimension left, Dimension rigth) {
            int width = Math.min(left.width, rigth.width);
            int height = Math.min(left.height, rigth.height);
            return new Dimension(width, height);
        }

        private Dimension max(Dimension left, Dimension rigth) {
            int width = Math.max(left.width, rigth.width);
            int height = Math.max(left.height, rigth.height);
            return new Dimension(width, height);
        }

        protected Picture createLeftPicture(Dimension dimension)
                throws IOException {
            return createPictureByImage(dimension, RED);
        }

        protected Picture createRightPicture(Dimension dimension)
                throws IOException {
            return createPictureByImage(dimension, RED);
        }

        private BufferedImage createCheckImage(Dimension size, Dimension overlap) {
            BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_4BYTE_ABGR);
            for (int x = 0; x < dimensionResult.width; ++x)
                for (int y = 0; y < dimensionResult.height; ++y)
                    image.setRGB(x, y, getPixel(x, y));
            return image;
        }

        protected abstract int getPixel(int x, int y);
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestDiffScreenshot
            extends TestAbstractScreenshot {

        @Test
        public void test()
                throws Exception {
            assertEquals(check, left.diff(right));
        }

        @Override
        protected int getPixel(int x, int y) {
            if (x >= dimensionOverlap.width && y < dimensionOverlap.height)
                return RED;
            if (x < dimensionOverlap.width && y >= dimensionOverlap.height)
                return RED;
            return 0;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestSameScreenshot
            extends TestAbstractScreenshot {

        @Test
        public void test()
                throws Exception {
            assertEquals(check, left.same(right));
            assertEquals(check, right.same(left));
        }

        @Override
        protected int getPixel(int x, int y) {
            return x < dimensionOverlap.width && y < dimensionOverlap.height ? RED : 0;
        }
    }
}
