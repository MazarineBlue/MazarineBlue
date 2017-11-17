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
package org.mazarineblue.mbt.model;

class EventImpl
        implements Event {

    private final String title;
    private final Class<?> type;

    EventImpl(String title, Class<?> type) {
        this.title = title;
        this.type = type;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public int compareTo(Event other) {
        int c = title.compareTo(other.getTitle());
        if (c != 0)
            c = type.getCanonicalName().compareTo(other.getType().getCanonicalName());
        return c;
    }
}
