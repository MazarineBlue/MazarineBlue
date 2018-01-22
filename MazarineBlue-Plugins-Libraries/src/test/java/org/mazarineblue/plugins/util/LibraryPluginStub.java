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
package org.mazarineblue.plugins.util;

import static java.util.Arrays.asList;
import java.util.List;
import java.util.function.Supplier;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.plugins.LibraryPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryPluginStub
        implements LibraryPlugin {

    private final String namespace;
    private final Supplier<Library[]> supplier;

    public LibraryPluginStub(String namespace, Library... libraries) {
        this(namespace, () -> libraries);
    }

    public LibraryPluginStub(String namespace, Supplier<Library[]> supplier) {
        this.namespace = namespace;
        this.supplier = supplier;
    }

    @Override
    public boolean canHandle(String namespace) {
        return this.namespace.equals(namespace);
    }

    @Override
    public List<Library> getLibraries(String namespace) {
        return asList(supplier.get());
    }
}
