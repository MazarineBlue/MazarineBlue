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

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.plugins.LibraryService.getLibraries;
import static org.mazarineblue.plugins.LibraryService.hasLibrary;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;
import org.mazarineblue.plugins.util.DummyLibrary;
import org.mazarineblue.plugins.util.ExceptionThrowingLibraryPluginStub;
import org.mazarineblue.plugins.util.LibraryPluginStub;
import org.mazarineblue.utilities.util.TestException;
import static org.mazarineblue.utilities.util.TestUtil.assertClasses;

/**
 * A {@code RunnerPlugin} is a {@code Plugin} that is able to create runners
 * dynamically.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryServiceTest {

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
    }

    @Test
    public void hasLibrary_NullNamespace() {
        assertFalse(hasLibrary(null));
    }

    @Test
    public void hasLibrary_EmptyNamespace() {
        assertFalse(hasLibrary(""));
    }

    @Test
    public void hasLibrary_UnsupportedNamespace() {
        assertFalse(hasLibrary("unsupported"));
    }

    @Test
    public void hasLibrary_SupportedNamespace() {
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("namespace"));
        assertTrue(hasLibrary("namespace"));
    }

    @Test(expected = LibraryPluginException.class)
    public void getLibrary_PluginThrowsException() {
        PluginLoader.getInstance().injectPlugin(new ExceptionThrowingLibraryPluginStub(() -> new TestException()));
        getLibraries("namespace");
    }

    @Test
    public void getLibrary_SupportedNamespace() {
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("namespace", new DummyLibrary("namespace")));
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("namespace", new DummyLibrary("namespace")));
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("foo", new DummyLibrary("bar")));
        assertClasses(getLibraries("namespace"), DummyLibrary.class, DummyLibrary.class);
    }
}
