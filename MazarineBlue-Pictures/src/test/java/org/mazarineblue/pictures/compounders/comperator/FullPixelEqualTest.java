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
import org.junit.BeforeClass;
import org.mazarineblue.pictures.compounders.CompareCompounder;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FullPixelEqualTest
        extends AbstractEqualTest {

    @BeforeClass
    public static void setupClass()
            throws Exception {
        AbstractEqualTest.comperator = new FullPixelComperator();
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
