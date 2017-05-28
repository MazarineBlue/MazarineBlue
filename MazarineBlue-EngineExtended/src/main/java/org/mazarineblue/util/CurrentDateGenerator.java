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
package org.mazarineblue.util;

import java.util.Calendar;

/**
 * A {@code CurrentDateGenerator} creates the current date in a string format,
 * whenever it is requested.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class CurrentDateGenerator
        implements DateFactory {

    private final String format;

    CurrentDateGenerator() {
        format = "%04d-%02d-%02dT%02d:%02d:%02d %s";
    }

    @Override
    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return String.format(format,
                             calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                             Calendar.DAY_OF_MONTH),
                             calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                             calendar.get(Calendar.ZONE_OFFSET));
    }
}
