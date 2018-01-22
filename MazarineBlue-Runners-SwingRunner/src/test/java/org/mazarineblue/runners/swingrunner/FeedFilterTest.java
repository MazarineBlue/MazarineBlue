/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.swingrunner;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.HiddenFileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.SocketFileSystem;
import org.mazarineblue.fs.WriteOnlyFileSystem;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedFilterTest {

    private File file;
    private FileSystem fs;
    private FeedFilter filter;

    @Before
    public void setup() {
        file = new File("foo");
        fs = new MemoryFileSystem();
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        file = null;
        fs = null;
        filter = null;
    }

    @Test
    public void test() {
        filter = new FeedFilter(fs);
        assertEquals("Feeds", filter.getDescription());
    }

    @Test
    public void accepts_Null_False() {
        filter = new FeedFilter(fs);
        assertFalse(filter.accept(null));
    }

    @Test
    public void accepts_UnreadableDirectory_True()
            throws IOException {
        fs.mkdir(file);
        filter = new FeedFilter(new WriteOnlyFileSystem(fs));
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_UnreadableFileTrue()
            throws IOException {
        fs.mkfile(file);
        filter = new FeedFilter(new WriteOnlyFileSystem(fs));
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_HiddenDirectory_False()
            throws IOException {
        fs.mkfile(file);
        filter = new FeedFilter(new HiddenFileSystem(fs));
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_HiddenFile_False()
            throws IOException {
        fs.mkdir(file);
        filter = new FeedFilter(new HiddenFileSystem(fs));
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_Directory_True()
            throws IOException {
        fs.mkdir(file);
        filter = new FeedFilter(fs);
        assertTrue(filter.accept(file));
    }

    @Test
    public void accepts_Socket_False()
            throws IOException {
        fs.mkfile(file);
        filter = new FeedFilter(new SocketFileSystem(fs));
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_UnsupportedFile_False()
            throws IOException {
        fs.mkfile(file);
        filter = new FeedFilter(fs);
        assertFalse(filter.accept(file));
    }

    @Test
    public void accepts_SupportedFile_False()
            throws IOException {
        fs.mkfile(file);
        filter = new FeedFilter(fs);
        PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin());
        assertTrue(filter.accept(file));
    }
}
