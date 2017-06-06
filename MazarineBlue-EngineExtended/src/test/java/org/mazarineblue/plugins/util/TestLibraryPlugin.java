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

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.plugins.LibraryPlugin;

public class TestLibraryPlugin
        implements LibraryPlugin {

    @Override
    public boolean canHandle(String namespace) {
        return true;
    }

    @Override
    public List<Library> getLibraries(String namespace) {
        List<Library> list = new ArrayList<>(1);
        list.add(new TestLibrary(namespace));
        return list;
    }
}