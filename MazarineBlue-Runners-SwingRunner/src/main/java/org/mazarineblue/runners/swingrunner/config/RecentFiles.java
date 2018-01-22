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
import java.io.ObjectInputStream;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import java.util.List;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.FeedService;
import org.mazarineblue.runners.swingrunner.archive.exceptions.UnableToWriteToFileException;

class RecentFiles {

    private static final long serialVersionUID = 1L;

    private final FileSystem fs;
    private final File location;
    private final int max;

    RecentFiles(FileSystem fs, File location, int max) {
        this.fs = fs;
        this.location = location;
        this.max = max;
    }

    public void write(File[] files) {
        write(asList(files));
    }

    private void write(List<File> list) {
        try {
            fs.mkfile(location, list);
        } catch (IOException ex) {
            throw new UnableToWriteToFileException(location, ex);
        }
    }

    File[] get()
            throws IOException {
        if (!fs.exists(location))
            return new File[0];
        File[] files = read(location);
        return removeDeadFiles(files);
    }

    private File[] read(File file)
            throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(fs.getInputStream(file))) {
            return readArray(input);
        }
    }

    private File[] readArray(ObjectInputStream input)
            throws IOException {
        int n = input.readInt();
        return fillArray(input, new File[n > max ? max : n]);
    }

    private File[] fillArray(ObjectInputStream input, File[] arr)
            throws IOException {
        int count = 0;
        for (; count < arr.length; ++count)
            try {
                arr[count] = (File) input.readObject();
            } catch (ClassNotFoundException ex) {
                --count;
            }
        return copyOf(arr, count);
    }

    private File[] removeDeadFiles(File[] files) {
        File[] output = new File[countFoundFiles(files)];
        return copyFoundFiles(files, output);
    }

    private int countFoundFiles(File[] files) {
        int n = 0;
        for (File f : files)
            if (shouldKeepFile(f))
                ++n;
        return n;
    }

    private File[] copyFoundFiles(File[] files, File[] output) {
        int j = 0;
        for (int i = 0; i < files.length; ++i)
            if (shouldKeepFile(files[i]))
                output[j++] = files[i];
        return output;
    }

    private boolean shouldKeepFile(File file) {
        try {
            return FeedService.canProcess(fs, file);
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
