/*
 * Copyright (c) 2015 Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
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
package org.mazarineblue.keyworddriven.exceptions;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ConsumableException extends RuntimeException {
    private boolean consumed = false;

    public ConsumableException() {
    }

    public ConsumableException(String message) {
        super(message);
    }

    public ConsumableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsumableException(Throwable cause) {
        super(cause);
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed() {
        consumed = true;
    }
}
