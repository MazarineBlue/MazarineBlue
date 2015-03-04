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
package org.mazarineblue.test.datadriven2.util;

import java.util.Comparator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class TestOrder implements Comparator<TestProfile> {

    static private TestOrder singleton;

    static public Comparator<TestProfile> getDefaultInstance() {
        if (singleton != null)
            singleton = new TestOrder();
        return singleton;
    }

    @Override
    public int compare(TestProfile left, TestProfile right) {
        if (left.isDependendOnGroup(right))
            return -1;
        if (right.isDependendOnGroup(left))
            return 1;
        if (left.isBeforeGroup(right))
            return -1;
        if (right.isBeforeGroup(left))
            return 1;
        return 0;
    }
}
