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
package org.mazarineblue.libraries;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;
import org.mazarineblue.plugins.util.RuntimeExceptionThrowingLibraryPlugin;
import org.mazarineblue.plugins.util.TestLibraryPlugin;
import static org.junit.Assert.assertEquals;

public class DefaultLibraryTest {

    private DefaultLibrary library;
    private TestInvokerSpy spy;

    @Before
    public void setup() {
        library = new DefaultLibrary();
        spy = new TestInvokerSpy();
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        library = null;
        spy = null;
    }

    @Test
    public void namespace() {
        assertEquals("org.mazarineblue", library.getNamespace());

    }

    @Test(expected = LibraryNotFoundException.class)
    public void importLibrary_NoRegisterdPlugins_ThrowsException() {
        library.importLibrary(new TestInvokerSpy(), "foo");
    }

    @Test
    public void importLibrary_RegisterdPlugin_CausesAddLibraryEvent() {
        PluginLoader.getInstance().injectPlugin(new TestLibraryPlugin());
        library.importLibrary(spy, "foo");

        List<Event> expected = new ArrayList<>(1);
        expected.add(new AddLibraryEvent(new TestLibrary("foo")));
        assertEquals(expected, spy.getEvents());
    }

    @Test(expected = LibraryPluginException.class)
    public void importLibrary_RegisterdPlugin_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new RuntimeExceptionThrowingLibraryPlugin());
        library.importLibrary(spy, "foo");

        List<Event> expected = new ArrayList<>(1);
        expected.add(new AddLibraryEvent(new TestLibrary("foo")));
        assertEquals(expected, spy.getEvents());
    }
}
