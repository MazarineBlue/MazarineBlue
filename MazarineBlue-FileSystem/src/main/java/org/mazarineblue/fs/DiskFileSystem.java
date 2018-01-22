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
package org.mazarineblue.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import static org.mazarineblue.utililities.Streams.copy;

/**
 * An {@code AbstractFileSystem} is a {@code FileSystem} that works on the hard disk.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DiskFileSystem
        extends AbstractFileSystem {

    @Override
    public void mkdir(File file)
            throws IOException {
        if (!file.mkdir())
            throw new IOException("Unable to create directory: " + file);
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        try (FileOutputStream output = new FileOutputStream(file)) {
            copy(input, output);
        }
    }

    @Override
    public void mkfile(File file, Collection<?> collection)
            throws IOException {
        mkdir(new FileOutputStream(file), collection);
    }

    @Override
    public void delete(File file)
            throws IOException {
        Files.delete(file.toPath());
    }

    @Override
    public void deleteAll()
            throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public File getParent(File file) {
        return file.getParentFile();
    }

    @Override
    public File[] listChilderen(File dir) {
        return dir.listFiles();
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    @Override
    public boolean isFile(File file) {
        return file.isFile();
    }

    @Override
    public boolean isHidden(File file) {
        return file.isHidden();
    }

    @Override
    public boolean isReadable(File file) {
        return file.canRead();
    }
}
