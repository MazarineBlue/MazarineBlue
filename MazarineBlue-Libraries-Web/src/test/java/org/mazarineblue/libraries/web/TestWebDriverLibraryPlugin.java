/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web;

import org.mazarineblue.libraries.web.BrowserLibrary;
import static java.util.Arrays.asList;
import java.util.List;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.plugins.AbstractLibraryPlugin;
import org.mazarineblue.plugins.LibraryPlugin;
import org.openide.util.lookup.ServiceProvider;

/**
 * This class overwrites the functionality of the production version of
 * {@code EmailLibraryPlugin}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@ServiceProvider(service = LibraryPlugin.class)
public class TestWebDriverLibraryPlugin
        extends AbstractLibraryPlugin {

    static final String NAMESPACE = "test.mazarineblue.libraries.web";

    public TestWebDriverLibraryPlugin() {
        super(NAMESPACE);
    }

    @Override
    protected List<Library> doLibraries() {
        return asList(createLibrary());
    }

    private Library createLibrary() {
        return new BrowserLibrary();
    }
}
