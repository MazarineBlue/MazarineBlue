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
package org.mazarineblue.runners.swingrunner.screens.main;

import org.mazarineblue.runners.swingrunner.screens.about.GraphicalMessage;

/**
 * A {@code GraphicalText} contains information about and text and where to
 * draw it.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class GraphicalMessageImpl
        implements GraphicalMessage {

    private final String message;
    private final int x;
    private final int y;

    /**
     * Constructs a {@code GraphicalText} with a specified message to draw at
     * the specified location.
     *
     * @param message the message to draw.
     * @param x       the x position to draw the message at.
     * @param y       the y position to draw the message at.
     */
    GraphicalMessageImpl(String message, int x, int y) {
        this.message = message;
        this.x = x;
        this.y = y;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
