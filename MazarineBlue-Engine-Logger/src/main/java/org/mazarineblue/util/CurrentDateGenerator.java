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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A {@code CurrentDateGenerator} creates the current date in a string format,
 * whenever it is requested.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CurrentDateGenerator
        implements DateFactory {

    private final SimpleDateFormat format;

    /**
     * Creates an instance that create timestamps in the format
     * "yyyy-MM-dd'T'HH:mm:ssX".
     *
     * @see SimpleDateFormat
     */
    public CurrentDateGenerator() {
        this("yyyy-MM-dd'T'HH:mm:ssX");
    }

    /**
     * Creates an instance that create timestamps in the specified format.
     *
     * @param format the format to use for the creation of timestamps.
     * @see SimpleDateFormat
     */
    public CurrentDateGenerator(String format) {
        this(new SimpleDateFormat(format));
    }

    /**
     * Creates an instance that create timestamps using the specified
     * {@link SimpleDateFormat}.
     *
     * @param format the format to use for the creation of timestamps.
     */
    public CurrentDateGenerator(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public String getCurrentDate() {
        return format.format(new Date());
    }

    @Override
    public int hashCode() {
        return 3 * 67
                + Objects.hashCode(this.format);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.format, ((CurrentDateGenerator) obj).format);
    }
}
