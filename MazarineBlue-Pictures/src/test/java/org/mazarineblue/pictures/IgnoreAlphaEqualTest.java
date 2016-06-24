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
import org.junit.BeforeClass;
import static org.mazarineblue.pictures.AbstractEqualTest.base;
import org.mazarineblue.pictures.compounders.comperator.IgnoreAlphaComperator;
import org.mazarineblue.pictures.compounders.EqualCompounder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class IgnoreAlphaEqualTest
        extends AbstractEqualTest {

    @BeforeClass
    static public void setupClass()
            throws Exception {
        AbstractEqualTest.comperator = new IgnoreAlphaComperator();
        Dimension dimension4x4 = new Dimension(4, 4);

        base = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
        equal = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
        alpha = createPicture(dimension4x4, 0, ALPHA_RED, ALPHA_GREEN, ALPHA_BLUE);
        red = createPicture(dimension4x4, ALPHA, ALPHA, ALPHA_GREEN, ALPHA_BLUE);
        green = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA, ALPHA_BLUE);
        blue = createPicture(dimension4x4, ALPHA, ALPHA_RED, ALPHA_GREEN, ALPHA);
        
        equalResult = EqualCompounder.Result.EQUAL;
        alphaResult = EqualCompounder.Result.EQUAL;
        redResult = EqualCompounder.Result.DIFF_CONTENT;
        greenResult = EqualCompounder.Result.DIFF_CONTENT;
        blueResult = EqualCompounder.Result.DIFF_CONTENT;
    }
}
