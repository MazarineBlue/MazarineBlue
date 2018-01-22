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

import java.util.List;
import static java.util.stream.Collectors.toList;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;

public class LibraryService {

    public static boolean hasLibrary(String namespace) {
        return PluginLoader.getInstance().getPlugins(LibraryPlugin.class).stream()
                .anyMatch(lib -> lib.canHandle(namespace));
    }

    public static List<Library> getLibraries(String namespace) {
        return PluginLoader.getInstance().getPlugins(LibraryPlugin.class).stream()
                .filter(lib -> lib.canHandle(namespace))
                .flatMap(plugin -> getLibraries(plugin, namespace).stream())
                .collect(toList());
    }

    private static List<Library> getLibraries(LibraryPlugin plugin, String namespace) {
        try {
            return plugin.getLibraries(namespace);
        } catch (RuntimeException ex) {
            throw new LibraryPluginException(namespace, ex);
        }
    }

    private LibraryService() {
    }
}
