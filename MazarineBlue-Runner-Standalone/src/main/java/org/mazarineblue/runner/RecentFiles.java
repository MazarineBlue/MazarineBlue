/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class RecentFiles {

    private final File recentFilesLocation;
    private final int maxFiles;

    public RecentFiles() {
        this(10);
    }

    public RecentFiles(int max) {
        this(max, new File(new File(System.getProperty("user.home")),
                           ".Mazarine.recentFiles"));
    }

    public RecentFiles(File recentFilesLocation) {
        this(10, recentFilesLocation);
    }

    public RecentFiles(int maxFiles, File recentFilesLocation) {
        this.maxFiles = maxFiles;
        this.recentFilesLocation = recentFilesLocation;
    }

    public void storeAdditionFile(File file)
            throws FileNotFoundException, IOException {
        RecentFiles recentFiles = new RecentFiles();
        Collection<File> list = new ArrayList();
        list.addAll(recentFiles.readRecentFiles());
        recentFiles.storeRecentFiles(list);
    }

    public int getMaxRecentFiles() {
        return maxFiles;
    }

    public final Collection<File> readRecentFiles()
            throws FileNotFoundException, IOException {
        Collection<File> files = new ArrayList();
        try (FileInputStream stream = new FileInputStream(recentFilesLocation);
             ObjectInputStream input = new ObjectInputStream(stream)) {
            FileLock lock = stream.getChannel().tryLock(0L, Long.MAX_VALUE, true);
            process(files, input);
            if (lock != null)
                lock.release();
        }
        return files;
    }

    private void process(Collection<File> files, ObjectInputStream input) {
        for (int i = 0; i < maxFiles; ++i)
            try {
                files.add((File) input.readObject());
            } catch (ClassNotFoundException | IOException ex) {
            }
    }

    public final void storeRecentFiles(Collection<File> collection)
            throws FileNotFoundException, IOException {
        try (FileOutputStream stream = new FileOutputStream(recentFilesLocation);
             ObjectOutputStream output = new ObjectOutputStream(stream)) {
            FileLock lock = stream.getChannel().lock();
            process(collection, output);
            lock.release();
        }
    }

    private void process(Collection<File> files, ObjectOutputStream output) {
        int index = 0;
        Map<File, File> seen = new HashMap();
        for (File file : files)
            try {
                if (seen.containsKey(file))
                    continue;
                seen.put(file, file);
                output.writeObject(file);
                ++index;
                if (index > maxFiles)
                    break;
            } catch (IOException ex) {
            }
    }
}
