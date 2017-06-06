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
package org.mazarineblue.swingrunner.util;

import java.util.logging.Level;

/**
 * A {@code ExceptionReporter} has the capability to report exceptions.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@FunctionalInterface
public interface ExceptionReporter {

    /**
     * Reports a message, with associated Throwable information.
     *
     * @param level  One of the message level identifiers, e.g., SEVERE.
     * @param msg    The string message (or a key in the message catalog).
     * @param thrown Throwable associated with log message.
     */
    public void log(Level level, String msg, Throwable thrown);
}
