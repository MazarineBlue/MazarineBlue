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
package org.mazarineblue.plugins;

import java.util.List;
import org.mazarineblue.keyworddriven.Library;

/**
 * {@code LibraryPlugin} provide support for loading libraries.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface LibraryPlugin
        extends Plugin {

    /**
     * Tests if the specified libraries can be processed by any of the libraries
     * plugins.
     *
     * @param namespace the namespace of the libraries to load
     * @return {@code true} if the specified library can be fetched by one of
     *         libraries plugins
     */
    public boolean canHandle(String namespace);

    /**
     * Read the specified stream to create one or more Libraries.
     *
     * @param namespace the location of library
     * @return the Library created from the specified input stream.
     */
    public List<Library> getLibraries(String namespace);
}
