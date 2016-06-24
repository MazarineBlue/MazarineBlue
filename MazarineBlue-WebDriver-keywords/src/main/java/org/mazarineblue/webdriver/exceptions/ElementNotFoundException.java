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
package org.mazarineblue.webdriver.exceptions;

import org.mazarineblue.keyworddriven.exceptions.ConsumableException;
import org.mazarineblue.webdriver.Offset;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ElementNotFoundException
        extends ConsumableException {

    public ElementNotFoundException(String name, Throwable cause) {
        super("Name: " + name, cause);
    }

    public ElementNotFoundException(String from, String to, Throwable cause) {
        super("From: " + from + " To: " + to, cause);
    }

    public ElementNotFoundException(String from, Offset to, Throwable cause) {
        super("From: " + from + " To: " + to, cause);
    }
}
