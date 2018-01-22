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

import java.io.File;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.util.CorruptFileSystem;
import static org.mazarineblue.plugins.FeedService.canProcess;
import static org.mazarineblue.plugins.FeedService.createFeed;
import static org.mazarineblue.plugins.FeedService.getFirstSheetName;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;
import org.mazarineblue.plugins.util.FeedPluginStub;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedServiceTest {

    private FileSystem fs;
    private File fileExists, fileNotFound;

    @Before
    public void setup()
            throws IOException {
        fs = new MemoryFileSystem();
        fileExists = new File("foo.txt");
        fileNotFound = new File("Bar.txt");
        fs.mkfile(fileExists, "");
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        fs = null;
        fileExists = null;
    }

    @Test(expected = FileUnreadableException.class)
    public void canProcess_CorruptFileSystem() {
        assertFalse(canProcess(new CorruptFileSystem(), fileExists));
    }

    @Test(expected = FileNotFoundException.class)
    public void canProcess_FileNotOnFileSystem() {
        assertFalse(canProcess(fs, fileNotFound));
    }

    @Test
    public void canProcess_False() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(false));
        assertFalse(canProcess(fs, fileExists));
    }

    @Test
    public void canProcess_True() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true));
        assertTrue(canProcess(fs, fileExists));
    }

    @Test(expected = FileUnreadableException.class)
    public void getFirstSheetName_CorruptFileSystem() {
        getFirstSheetName(new CorruptFileSystem(), fileExists);
    }

    @Test(expected = FileNotFoundException.class)
    public void getFirstSheetName_FileNotOnFileSystem() {
        getFirstSheetName(fs, fileNotFound);
    }

    @Test(expected = FileNotSupportedException.class)
    public void getFirstSheetName_CanNotProcessFile() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(false));
        getFirstSheetName(fs, fileExists);
    }

    @Test(expected = SheetNotFoundException.class)
    public void getFirstSheetName_ZeroSheetNames() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true));
        getFirstSheetName(fs, fileExists);
    }

    @Test
    public void getFirstSheetName_MultipleSheetNames() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, "foo", "bar"));
        assertEquals("foo", getFirstSheetName(fs, fileExists));
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed_CorruptFileSystem() {
        createFeed(new CorruptFileSystem(), fileExists);
    }

    @Test(expected = FileNotFoundException.class)
    public void createFeed_FileNotOnFileSystem() {
        createFeed(fs, fileNotFound);
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed_ZeroSheetNames() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, new MemoryFeed()));
        createFeed(fs, fileExists);
    }

    @Test(expected = FileNotSupportedException.class)
    public void createFeed_CanNotProcessFile() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(false, new MemoryFeed(), "foo", "bar"));
        createFeed(fs, fileExists).getClass();
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed_ZeroSheetNames_SelectUnavailableSheet() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, new MemoryFeed()));
        createFeed(fs, fileExists, "xxx");
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed_MultipleSheetNames_SelectNullSheet() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, new MemoryFeed(), "foo", "bar"));
        assertTrue(MemoryFeed.class.isAssignableFrom(createFeed(fs, fileExists, null).getClass()));
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed_MultipleSheetNames_SelectUnavailableSheet() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, new MemoryFeed(), "foo", "bar"));
        assertTrue(MemoryFeed.class.isAssignableFrom(createFeed(fs, fileExists, "xxx").getClass()));
    }

    @Test
    public void createFeed_MultipleSheetNames() {
        PluginLoader.getInstance().injectPlugin(new FeedPluginStub(true, new MemoryFeed(), "foo", "bar"));
        assertTrue(MemoryFeed.class.isAssignableFrom(createFeed(fs, fileExists).getClass()));
    }
}
