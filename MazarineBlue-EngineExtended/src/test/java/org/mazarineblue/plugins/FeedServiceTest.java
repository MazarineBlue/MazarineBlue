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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.feeds.DummyFeed;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.util.CorruptFileSystem;
import org.mazarineblue.fs.util.DummyFileSystem;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.plugins.util.TestFeedPlugin;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FeedServiceTest {

    private MemoryFileSystem fs;
    private InputStream input;
    private static File FILE_OK;
    private static File FILE_NOT_SUPPORTED;
    private static File FILE_NOT_ON_DISK;

    @BeforeClass
    public static void setupClass() {
        FILE_OK = new File("demo.txt");
        FILE_NOT_SUPPORTED = new File("demo.xlsx");
        FILE_NOT_ON_DISK = new File("demo.xxx");
    }

    @AfterClass
    public static void teardownClass() {
        FILE_OK = null;
        FILE_NOT_SUPPORTED = null;
        FILE_NOT_ON_DISK = null;
    }

    @Before
    public void setup()
            throws IOException {
        fs = new MemoryFileSystem();
        input = new ByteArrayInputStream(new byte[0]);
        fs.mkfile(FILE_OK, "| Keyword | Argument |");
        fs.mkfile(FILE_NOT_SUPPORTED, input);
    }

    @After
    public void teardown() {
        input = null;
        PluginLoader.getInstance().reload();
    }

    @Test(expected = FileUnreadableException.class)
    public void canProcess_CorruptFileSystem_ThrowsException() {
        FeedService.canProcess(new CorruptFileSystem(new DummyFileSystem()), FILE_NOT_SUPPORTED);
    }

    @Test
    public void canProcess_MissingFile_ReturnsFalse() {
        assertFalse(FeedService.canProcess(fs, FILE_NOT_SUPPORTED));
    }

    @Test
    public void canProcess_ContainingFile_ReturnsTrue() {
        PluginLoader.getInstance().injectPlugin(new TestFeedPlugin(new DummyFeed(), "main"));
        assertTrue(FeedService.canProcess(fs, FILE_NOT_SUPPORTED));
    }

    @Test(expected = FileNotFoundException.class)
    public void readSheetNames_FileNotOnDisk_ThrowsException() {
        FeedService.readSheetNames(fs, FILE_NOT_ON_DISK);
    }

    @Test(expected = FileNotSupportedException.class)
    public void readSheetNames_FileNotSupported_ThrowsException() {
        FeedService.readSheetNames(fs, FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void readSheetNames_CorruptedFileSystem_ThrowsException() {
        FeedService.readSheetNames(new CorruptFileSystem(new DummyFileSystem()), FILE_OK);
    }

    @Test
    public void readSheetNames() {
        PluginLoader.getInstance().injectPlugin(new TestFeedPlugin(new DummyFeed(), "main"));
        assertArrayEquals(new String[]{"main"}, FeedService.readSheetNames(fs, FILE_NOT_SUPPORTED));
    }

    @Test(expected = FileNotFoundException.class)
    public void createFeed_FileNotOnDisk_ReturnsFalse() {
        FeedService.createFeed(fs, FILE_NOT_ON_DISK);
    }

    @Test(expected = FileNotSupportedException.class)
    public void createFeed_FileNotSupported_ThrowsException() {
        FeedService.createFeed(fs, FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed_CorruptFileSystem_ReturnsFalse() {
        FeedService.createFeed(new CorruptFileSystem(new DummyFileSystem()), FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed_FileWithoutSheets_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new TestFeedPlugin(new DummyFeed()));
        FeedService.createFeed(fs, FILE_NOT_SUPPORTED);
    }

    @Test
    public void createFeed()
            throws IOException {
        Feed expected = new DummyFeed();
        PluginLoader.getInstance().injectPlugin(new TestFeedPlugin(expected, "main"));
        Feed feed = FeedService.createFeed(fs, FILE_NOT_SUPPORTED);
        assertEquals(expected, feed);
    }
}
