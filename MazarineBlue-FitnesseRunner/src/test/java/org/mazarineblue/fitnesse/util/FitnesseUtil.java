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
package org.mazarineblue.fitnesse.util;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FitnesseUtil {

    public static long getMemoryFootprint() {
        getMemoryFootprintHelper();
        return getMemoryFootprintHelper();
    }

    public static double getMemoryFootprintDiff(long before, long unit) {
        long diff = getMemoryFootprintHelper() - before;
        return Math.round(diff * 100 / unit) / 100d;
    }

    // <editor-fold defaultstate="true" desc="Memory footprint helper methods">
    private static long getMemoryFootprintHelper() {
        System.gc();
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    // </editor-fold>

    private FitnesseUtil() {
    }
}
