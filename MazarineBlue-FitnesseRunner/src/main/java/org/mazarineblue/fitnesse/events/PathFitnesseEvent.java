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
package org.mazarineblue.fitnesse.events;

import java.util.Objects;

/**
 * A {@code PathFitnesseEvent} is a {@code FitnesseEvent} that instructs a
 * {@link FitnesseSubscriber} to add an path to the environment path list.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PathFitnesseEvent
        extends FitnesseEvent {

    private final String path;

    /**
     * Constructs a {@code PathFitnesseEvent} as an instruction for
     * {@code FitnesseEvent} to add an path to the environment search list.
     *
     * @param path the path to add to the environment
     */
    public PathFitnesseEvent(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "path = " + path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return 3 * 71
                + Objects.hashCode(this.path);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && Objects.equals(this.path, ((PathFitnesseEvent) obj).path);
    }
}
