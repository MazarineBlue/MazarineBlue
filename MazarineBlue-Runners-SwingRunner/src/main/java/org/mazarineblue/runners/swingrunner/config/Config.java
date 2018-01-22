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
package org.mazarineblue.runners.swingrunner.config;

import java.io.File;
import java.io.IOException;
import static java.lang.System.getProperty;
import org.mazarineblue.fs.FileSystem;

/**
 * An {@code Config} represents the configuration as stored on the file system.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Config {

    private static final int MAX_RECENT_FILES = 10;
    private static final File HOME = new File(getProperty("user.home"));
    private static final File RECENT_FILES_LOCATION = new File(HOME, ".MazarineBlue.recentFiles");
    private static final long serialVersionUID = 1L;

    private final FileSystem fs;
    private RecentFiles recentFiles;
    private boolean readOnly = false;

    /**
     * Load the configuration from the {@code FileSystem}.
     *
     * @param fs the file system to load the configuration from.
     */
    public Config(FileSystem fs) {
        this.fs = fs;
    }

    /**
     * Return the maximum amount of recent files that can be serialized to the
     * {@code FileSystem}.
     *
     * @return the maximum amount of recent files.
     */
    public static int maxRecentFiles() {
        return MAX_RECENT_FILES;
    }

    /**
     * Return location on the {@code FileSystem} where the recent files are
     * serialized to.
     *
     * @return the location of the recent files on the {@code FileSystem}.
     */
    public static File recentFilesLocation() {
        return RECENT_FILES_LOCATION;
    }

    public void writeToRecentFile(File[] files) {
        if (!readOnly)
            singleton().write(files);
    }

    public File getMostRecentDirectory() {
        try {
            File[] files = singleton().get();
            return files.length == 0 ? new File(".") : fs.getParent(files[0]);
        } catch (IOException ex) {
            return new File(".");
        }
    }

    public File[] getRecentFiles()
            throws IOException {
        return singleton().get();
    }

    private RecentFiles singleton() {
        if (recentFiles == null)
            recentFiles = new RecentFiles(fs, RECENT_FILES_LOCATION, MAX_RECENT_FILES);
        return recentFiles;
    }

    public void setReadOnly() {
        readOnly = true;
    }
}
