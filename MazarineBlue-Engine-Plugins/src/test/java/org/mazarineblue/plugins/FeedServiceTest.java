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
import static org.mazarineblue.plugins.FeedService.canProcess;
import static org.mazarineblue.plugins.FeedService.createFeed;
import static org.mazarineblue.plugins.FeedService.getFirstSheetName;
import static org.mazarineblue.plugins.FeedService.getSheetNames;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.plugins.util.ExceptionFeedPlugin;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedServiceTest {

    private static File FILE_ON_DISK;
    private static File FILE_NOT_SUPPORTED;
    private static File FILE_NOT_ON_DISK;

    private MemoryFileSystem fs;
    private InputStream input;

    @BeforeClass
    public static void setupClass() {
        FILE_ON_DISK = new File("demo.txt");
        FILE_NOT_SUPPORTED = new File("demo.xlsx");
        FILE_NOT_ON_DISK = new File("demo.xxx");
    }

    @AfterClass
    public static void teardownClass() {
        FILE_ON_DISK = null;
        FILE_NOT_SUPPORTED = null;
        FILE_NOT_ON_DISK = null;
    }

    @Before
    public void setup()
            throws IOException {
        fs = new MemoryFileSystem();
        input = new ByteArrayInputStream(new byte[0]);
        fs.mkfile(FILE_ON_DISK, "| Keyword | Argument |");
        fs.mkfile(FILE_NOT_SUPPORTED, input);
    }

    @After
    public void teardown() {
        input = null;
        PluginLoader.getInstance().reload();
    }

    @Test(expected = FileUnreadableException.class)
    public void canProcess_CorruptFileSystem_ThrowsException() {
        canProcess(new CorruptFileSystem(new DummyFileSystem()), FILE_NOT_SUPPORTED);
    }

    @Test
    public void canProcess_MissingFile_ReturnsFalse() {
        assertFalse(canProcess(fs, FILE_NOT_SUPPORTED));
    }

    @Test
    public void canProcess_ContainingFile_ReturnsTrue() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("main"));
        assertTrue(canProcess(fs, FILE_NOT_SUPPORTED));
    }

    @Test(expected = FileNotFoundException.class)
    public void getFirstSheetName_FileNotOnDisk_ThrowsException() {
        getFirstSheetName(fs, FILE_NOT_ON_DISK);
    }

    @Test(expected = FileNotSupportedException.class)
    public void getFirstSheetName_FileNotSupported_ThrowsException() {
        getFirstSheetName(fs, FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void getFirstSheetName_CorruptedFileSystem_ThrowsException() {
        getFirstSheetName(new CorruptFileSystem(new DummyFileSystem()), FILE_ON_DISK);
    }

    @Test(expected = SheetNotFoundException.class)
    public void getFirstSheetName_FileHasNoSheets_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin());
        getFirstSheetName(fs, FILE_ON_DISK);
    }

    @Test
    public void getFirstSheetName_FileHasSheets_ReturnFirstSheet() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("A", "B", "C"));
        assertEquals("A", getFirstSheetName(fs, FILE_ON_DISK));
    }

    @Test(expected = FileNotFoundException.class)
    public void getSheetNames_FileNotOnDisk_ThrowsException() {
        getSheetNames(fs, FILE_NOT_ON_DISK);
    }

    @Test(expected = FileNotSupportedException.class)
    public void getSheetNames_FileNotSupported_ThrowsException() {
        getSheetNames(fs, FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void getSheetNames_CorruptedFileSystem_ThrowsException() {
        getSheetNames(new CorruptFileSystem(new DummyFileSystem()), FILE_ON_DISK);
    }

    @Test
    public void getSheetNames_FileHasNoSheets_ReturnsEmptyString() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin());
        assertArrayEquals(new String[0], getSheetNames(fs, FILE_ON_DISK));
    }

    @Test
    public void getSheetNames_FileHasSheets_ReturnSheets() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("A", "B", "C"));
        assertArrayEquals(new String[]{"A", "B", "C"}, getSheetNames(fs, FILE_NOT_SUPPORTED));
    }

    @Test(expected = FileNotFoundException.class)
    public void createFeed2_FileNotOnDisk_ReturnsFalse() {
        createFeed(fs, FILE_NOT_ON_DISK);
    }

    @Test(expected = FileNotSupportedException.class)
    public void createFeed2_FileNotSupported_ThrowsException() {
        createFeed(fs, FILE_NOT_SUPPORTED);
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed2_CorruptFileSystem_ReturnsFalse() {
        createFeed(new CorruptFileSystem(new DummyFileSystem()), FILE_NOT_SUPPORTED);
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed2_FileWithoutSheets_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin());
        createFeed(fs, FILE_ON_DISK);
    }

    @Test
    public void createFeed2_FileWithSheets_ReturnsFeed() {
        Feed feed = new DummyFeed();
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("A", feed).addSheet("B, C"));
        assertEquals(feed, createFeed(fs, FILE_ON_DISK));
    }

    @Test(expected = FileNotFoundException.class)
    public void createFeed3_FileNotOnDisk_ReturnsFalse() {
        createFeed(fs, FILE_NOT_ON_DISK, "A");
    }

    @Test(expected = FileNotSupportedException.class)
    public void createFeed3_FileNotSupported_ThrowsException() {
        createFeed(fs, FILE_NOT_SUPPORTED, "A");
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed3_CorruptFileSystem_ReturnsFalse() {
        createFeed(new CorruptFileSystem(new DummyFileSystem()), FILE_NOT_SUPPORTED, "A");
    }

    @Test(expected = FileUnreadableException.class)
    public void createFeed3_FeedPluginThrowsExceptionOnCreation_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new ExceptionFeedPlugin("A"));
        createFeed(fs, FILE_ON_DISK, "A");
    }

    @Test(expected = SheetNotFoundException.class)
    public void createFeed3_FileWithoutSheets_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin());
        createFeed(fs, FILE_ON_DISK, "A");
    }

    @Test
    public void createFeed3_FileWithSheets_ReturnsFeed() {
        Feed feed = new DummyFeed();
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("A").addSheet("B", feed).addSheet("C"));
        assertEquals(feed, createFeed(fs, FILE_ON_DISK, "B"));
    }

    @Test
    public void createFeed_WitSheetSpecified()
            throws IOException {
        Feed expected = new DummyFeed();
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("main", expected));
        Feed feed = createFeed(fs, FILE_NOT_SUPPORTED, "main");
        assertEquals(expected, feed);
    }
}
