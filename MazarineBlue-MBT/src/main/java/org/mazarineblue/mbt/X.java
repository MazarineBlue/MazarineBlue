/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.mbt;

import java.util.Collection;
import java.util.Iterator;

// http://model-based-testing.de/data/weissleder_phd_thesis.pdf

public class X {

    public static <T extends Comparable<T>> int compateTo(Collection<T> left, Collection<T> right) {
        if (left == null)
            return right == null ? 0 : -1;
        else if (right == null)
            return 1;
        else {
            int c = left.size() - right.size();
            return c == 0 ? deepCompareTo(left, right) : c;
        }
    }

    private static <T extends Comparable<T>> int deepCompareTo(Collection<T> left, Collection<T> right) {
        int c;
        Iterator<T> it = left.iterator();
        Iterator<T> it2 = right.iterator();
        while (it.hasNext())
            if ((c = it.next().compareTo(it2.next())) != 0)
                return c;
        return 0;
    }

    @SuppressWarnings("null")
    public static <T> String toString(Collection<T> c, String open, String dilimeter, String close, String empty) {
        if (c.isEmpty())
            return empty;
        StringBuilder builder = null;
        for (T t : c)
            builder = builder == null
                    ? new StringBuilder(32 * c.size()).append(open).append(t)
                    : builder.append(dilimeter).append(t);
        return builder.append(close).toString();
    }
}
