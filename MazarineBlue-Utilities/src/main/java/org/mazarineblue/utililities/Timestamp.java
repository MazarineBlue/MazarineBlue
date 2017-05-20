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
package org.mazarineblue.utililities;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A {@code Timestamp} provides a mechanisme for formatting dates.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Timestamp
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Format format;

    /**
     * Constructs a {@code Timestamp} that formates dates using the format:
     * {@link SimpleDateFormat "yyyy-MM-dd HH:mm:ss X"}.
     */
    public Timestamp() {
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss X");
    }

    /**
     * Constructs a {@code Timestamp} that formates dates using the specified
     * format: {@link SimpleDateFormat}.
     *
     * @param format
     */
    public Timestamp(String format) {
        this.format = new SimpleDateFormat(format);
    }

    /**
     * Return the timestamp for the current date (now).
     *
     * @return the timestamp for the current date (now).
     */
    public String getTimestamp() {
        return format.format(new Date());
    }

    /**
     * Return the timestamp for the specified date.
     *
     * @param date the date to return the timestamp for.
     * @return the timestamp for the specified date.
     */
    public String getTimestamp(Date date) {
        return format.format(date);
    }

    @Override
    public int hashCode() {
        return 581 + Objects.hashCode(this.format);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && Objects.equals(this.format, ((Timestamp) obj).format);
    }
}
