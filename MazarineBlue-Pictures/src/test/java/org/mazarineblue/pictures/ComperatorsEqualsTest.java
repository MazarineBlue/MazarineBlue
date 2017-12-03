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

import java.awt.Dimension;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.pictures.compounders.CompareCompounder;
import org.mazarineblue.pictures.compounders.PixelComperator;
import org.mazarineblue.pictures.compounders.comperators.FullPixelComperator;
import org.mazarineblue.pictures.compounders.comperators.IgnoreAlphaComperator;

public class ComperatorsEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public abstract class AbstractEqualTest {

        protected static final int ALPHA = 255 << 24;
        protected static final int RED = 255 << 16;
        protected static final int GREEN = 255 << 8;
        protected static final int BLUE = 255;

        protected static final int ALPHA_RED = ALPHA + RED;
        protected static final int ALPHA_GREEN = ALPHA + GREEN;
        protected static final int ALPHA_BLUE = ALPHA + BLUE;

        protected static final int ALL = ALPHA + RED + GREEN + BLUE;

        protected PixelComperator comperator;
        protected Picture base, equal, alpha, red, green, blue;
        protected CompareCompounder.Result equalResult, alphaResult, redResult, greenResult, blueResult;

        @After
        public void teardown() {
            comperator = null;
            base = equal = alpha = red = green = blue = null;
            equalResult = alphaResult = redResult = greenResult = blueResult = null;
        }

        @Test
        public void testEqual()
                throws Exception {
            assertEquals(equalResult, equal.compare(base, comperator));
        }

        @Test
        public void testAlpha() {
            assertEquals(alphaResult, alpha.compare(base, comperator));
        }

        @Test
        public void testRed() {
            assertEquals(redResult, red.compare(base, comperator));
        }

        @Test
        public void testGreen() {
            assertEquals(greenResult, green.compare(base, comperator));
        }

        @Test
        public void testBlue() {
            assertEquals(blueResult, blue.compare(base, comperator));
        }

        protected Picture createPicture(Dimension d, int... rgb)
                throws IOException {
            return PictureTest.createPictureByImage(d, rgb);
        }
    }

    public class FullPixelEqualTest
            extends AbstractEqualTest {

        @Before
        public void setup()
                throws Exception {
            comperator = new FullPixelComperator();
            Dimension dimension4x4 = new Dimension(4, 4);

            base = createPicture(dimension4x4, ALPHA, RED, GREEN, BLUE);
            equal = createPicture(dimension4x4, ALPHA, RED, GREEN, BLUE);
            alpha = createPicture(dimension4x4, ALPHA + RED, RED, GREEN, BLUE);
            red = createPicture(dimension4x4, ALPHA, ALPHA + RED, GREEN, BLUE);
            green = createPicture(dimension4x4, ALPHA, RED, GREEN + BLUE, BLUE);
            blue = createPicture(dimension4x4, ALPHA, RED, GREEN, GREEN + BLUE);

            equalResult = CompareCompounder.Result.EQUAL;
            alphaResult = CompareCompounder.Result.DIFF_CONTENT;
            redResult = CompareCompounder.Result.DIFF_CONTENT;
            greenResult = CompareCompounder.Result.DIFF_CONTENT;
            blueResult = CompareCompounder.Result.DIFF_CONTENT;
        }
    }

    public class IgnoreAlphaEqualTest
            extends AbstractEqualTest {

        @Before
        public void setupClass()
                throws Exception {
            comperator = new IgnoreAlphaComperator();
            Dimension dimension4x4 = new Dimension(4, 4);

            base = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
            equal = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
            alpha = createPicture(dimension4x4, 0, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
            red = createPicture(dimension4x4, ALPHA, ALPHA, ALPHA_GREEN, ALPHA_BLUE);
            green = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA, ALPHA_BLUE);
            blue = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA);

            equalResult = CompareCompounder.Result.EQUAL;
            alphaResult = CompareCompounder.Result.EQUAL;
            redResult = CompareCompounder.Result.DIFF_CONTENT;
            greenResult = CompareCompounder.Result.DIFF_CONTENT;
            blueResult = CompareCompounder.Result.DIFF_CONTENT;
        }
    }
}
