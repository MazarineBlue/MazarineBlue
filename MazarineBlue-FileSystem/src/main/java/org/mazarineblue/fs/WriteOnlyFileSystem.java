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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An {@code WriteOnlyFileSystem} is a {@code AdapterFileSystem} that acts as if all file are unreadable.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WriteOnlyFileSystem
        extends AdapterFileSystem {

    /**
     * Creates a {@code WriteOnlyFileSystem}.
     *
     * @param adaptee the other file system to adapt the implementation from.
     */
    public WriteOnlyFileSystem(FileSystem adaptee) {
        super(adaptee);
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        throw new IOException();
    }

    @Override
    public String getContent(File file)
            throws IOException {
        throw new IOException();
    }

    @Override
    public <T> List<T> getList(File file)
            throws IOException {
        throw new IOException();
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        throw new IOException();
    }

    @Override
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        throw new IOException();
    }

    @Override
    public boolean isReadable(File file) {
        return false;
    }
}
