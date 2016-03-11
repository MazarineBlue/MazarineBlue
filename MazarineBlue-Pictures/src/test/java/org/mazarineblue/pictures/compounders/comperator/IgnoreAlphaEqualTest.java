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
import static org.mazarineblue.pictures.compounders.comperator.AbstractEqualTest.base;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class IgnoreAlphaEqualTest
        extends AbstractEqualTest {

    @BeforeClass
    public static void setupClass()
            throws Exception {
        AbstractEqualTest.comperator = new IgnoreAlphaComperator();
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
