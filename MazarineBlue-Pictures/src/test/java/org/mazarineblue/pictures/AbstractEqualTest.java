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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.pictures.compounders.PixelComperator;
import org.mazarineblue.pictures.compounders.EqualCompounder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class AbstractEqualTest {

    static protected final int ALPHA = 255 << 24;
    static protected final int RED = 255 << 16;
    static protected final int GREEN = 255 << 8;
    static protected final int BLUE = 255;
    
    static protected final int ALPHA_RED = ALPHA + RED;
    static protected final int ALPHA_GREEN = ALPHA + GREEN;
    static protected final int ALPHA_BLUE = ALPHA + BLUE;
    
    static protected final int ALL = ALPHA + RED + GREEN + BLUE;

    static protected PixelComperator comperator;
    static protected Picture base, equal, alpha, red, green, blue;
    static protected EqualCompounder.Result equalResult, alphaResult, redResult, greenResult, blueResult;
    
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

    static protected Picture createPicture(Dimension d, int... rgb)
            throws Exception {
        return PictureTest.createPictureByImage(d, rgb);
    }
}
