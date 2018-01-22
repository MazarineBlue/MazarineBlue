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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.pictures.compounders.TestCompareCompounder;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

public class CompareCompounderTest {

    private TestCompareCompounder compareCompounder;

    @Before
    public void setup() {
        Picture left = new Picture(3, 4);
        Picture right = new Picture(3, 4);
        compareCompounder = new TestCompareCompounder(left, right, (leftPixel, rightPixel) -> true);
    }

    @After
    public void teardown() {
        compareCompounder = null;
    }

    @Test(expected = NeverThrownException.class)
    public void compoundNone() {
        compareCompounder.compoundNone(0, 0);
    }

    @Test(expected = NeverThrownException.class)
    public void compoundRight() {
        compareCompounder.compoundRight(0, 0);
    }

    @Test(expected = NeverThrownException.class)
    public void compoundLeft() {
        compareCompounder.compoundLeft(0, 0);
    }
}
