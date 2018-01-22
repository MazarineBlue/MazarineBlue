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
package org.mazarineblue.plugins;

/**
 * A {@code Runner} is an unit that can process the arguments.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Runner {

    /**
     * Setups, with the specified arguments, and starts the {@code Runner}.
     *
     * @param args the arguments to load
     */
    default void execute(String... args) {
        if (args != null && args.length != 0)
            setArguments(args);
        start();
    }

    /**
     * Setups the {@code Runner} to work with the specified arguments.
     *
     * @param args the arguments to load
     */
    public void setArguments(String... args);

    /**
     * Start the {@code Runner}, which (usually) processes the specified arguments.
     */
    public void start();
}
