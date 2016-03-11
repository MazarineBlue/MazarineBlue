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
package org.mazarineblue.pictures.compounders.comperator;

import java.awt.Dimension;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.pictures.PictureTest;
import org.mazarineblue.pictures.compounders.CompareCompounder;
import org.mazarineblue.pictures.compounders.PixelComperator;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractEqualTest {

    protected static final int ALPHA = 255 << 24;
    protected static final int RED = 255 << 16;
    protected static final int GREEN = 255 << 8;
    protected static final int BLUE = 255;

    protected static final int ALPHA_RED = ALPHA + RED;
    protected static final int ALPHA_GREEN = ALPHA + GREEN;
    protected static final int ALPHA_BLUE = ALPHA + BLUE;

    protected static final int ALL = ALPHA + RED + GREEN + BLUE;

    protected static PixelComperator comperator;
    protected static Picture base, equal, alpha, red, green, blue;
    protected static CompareCompounder.Result equalResult, alphaResult, redResult, greenResult, blueResult;

    @AfterClass
    public static void teardownClass() {
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
    public void testAlpha()
            throws Exception {
        assertEquals(alphaResult, alpha.compare(base, comperator));
    }

    @Test
    public void testRed()
            throws Exception {
        assertEquals(redResult, red.compare(base, comperator));
    }

    @Test
    public void testGreen()
            throws Exception {
        assertEquals(greenResult, green.compare(base, comperator));
    }

    @Test
    public void testBlue()
            throws Exception {
        assertEquals(blueResult, blue.compare(base, comperator));
    }

    protected static Picture createPicture(Dimension d, int... rgb)
            throws Exception {
        return PictureTest.createPictureByImage(d, rgb);
    }
}
