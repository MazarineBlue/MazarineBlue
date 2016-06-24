/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.webdriver.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class SeleniumToolkit {

    public static boolean withinRange(Point expected, Point actual, Point delta) {
        if (withinRange(expected.x, actual.x, delta == null ? 0 : delta.x) == false)
            return false;
        return withinRange(expected.y, actual.y, delta == null ? 0 : delta.y);
    }

    public static boolean withinRange(Dimension expected, Dimension actual,
                                      Dimension delta) {
        if (withinRange(expected.width, actual.width,
                        delta == null ? 0 : delta.width) == false)
            return false;
        return withinRange(expected.height, actual.height,
                           delta == null ? 0 : delta.height);
    }

    public static boolean withinRange(int expected, int actual, int delta) {
        return expected - actual <= delta && expected - actual >= delta;
    }
}
