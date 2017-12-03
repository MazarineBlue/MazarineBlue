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
package org.mazarineblue.libraries.fixtures;

import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLoaderLibraryPluginTest {

    private FixtureLoaderLibraryPlugin plugin;

    @Before
    public void setup() {
        plugin = new FixtureLoaderLibraryPlugin();
    }

    @After
    public void teardown() {
        plugin = null;
    }
    
    @Test
    public void canHandle_UnknownNamespace() {
        assertFalse(plugin.canHandle("unkown"));
    }

    @Test
    public void canHandle_KnownNamespace() {
        assertTrue(plugin.canHandle("org.mazarineblue.libraries.fixtures"));
    }

    @Test
    public void getLibraries_UnkownNamespace() {
        assertTrue(plugin.getLibraries("unkown").isEmpty());
    }

    @Test
    public void getLibraries_KownNamespace() {
        List<Library> lib = plugin.getLibraries("org.mazarineblue.libraries.fixtures");
        assertFalse(lib.isEmpty());
    }
}
