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
import java.util.ArrayList;
import static java.util.Arrays.asList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.runners.swingrunner.config.Config;
import static org.mazarineblue.runners.swingrunner.config.Config.recentFilesLocation;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ConfigTest {

    private FileSystem fs;
    private Config config;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        config = new Config(fs);

    }

    @After
    public void teardown() {
        fs = null;
        config = null;
    }

    @Test
    public void getMostRecentDirectory_FreshConfig()
            throws IOException {
        assertEquals(".", config.getMostRecentDirectory().getPath());
    }

    @Test
    public void getMostRecentDirectory_EmptyConfig()
            throws IOException {
        fs.mkfile(recentFilesLocation(), new ArrayList<>(0));
        assertEquals(".", config.getMostRecentDirectory().getPath());
    }

    @Test
    public void getMostRecentDirectory_FilledConfig()
            throws IOException {
        fs.mkdir(new File("dir"));
        fs.mkfile(new File("dir", "file.txt"), "");
        fs.mkfile(recentFilesLocation(), asList(new File("dir", "file.txt")));
        assertEquals("dir", config.getMostRecentDirectory().getPath());
    }

    @Test
    public void test() {
        File[] files = new File[1];
        files[0] = new File("dir", "file");
        config.writeToRecentFile(files);
        assertTrue(fs.isFile(recentFilesLocation()));
    }
}
