/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.plugins.util.DummyLibrary;

public class AbstractLibrayPluginTest {

    private DummyLibrary library;
    private LibraryPlugin libraryPlugin;

    @Before
    public void setup() {
        libraryPlugin = new AbstractLibraryPlugin("foo") {
            @Override
            protected List<Library> doLibraries() {
                return asList(library);
            }
        };
    }

    @Test
    public void canHandle_Null() {
        assertFalse(libraryPlugin.canHandle(null));
    }

    @Test
    public void canHandle_DifferentNamespace() {
        assertFalse(libraryPlugin.canHandle("bar"));
    }

    @Test
    public void canHandle_SameNamespace() {
        assertTrue(libraryPlugin.canHandle("foo"));
    }

    @Test
    public void getLibraries_Null() {
        assertEquals(emptyList(), libraryPlugin.getLibraries(null));
    }

    @Test
    public void getLibraries_DifferentNamespace() {
        assertEquals(emptyList(), libraryPlugin.getLibraries("bar"));
    }

    @Test
    public void getLibraries_SameNamespace() {
        assertEquals(asList(library), libraryPlugin.getLibraries("foo"));
    }
}
