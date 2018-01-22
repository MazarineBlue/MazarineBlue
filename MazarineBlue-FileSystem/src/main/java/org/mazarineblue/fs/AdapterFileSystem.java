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
import java.util.Collection;
import java.util.List;

/**
 * An {@code AdapterFileSystem} is a {@code FileSystem} that depend on another file system for its implementation.
 *
 * This class is used by other classes that alters other file systems in some ways and act the same in other ways.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AdapterFileSystem
        implements FileSystem {

    private final FileSystem adaptee;

    /**
     * Creates an {@code AdapterFileSystem}.
     *
     * @param adaptee the other file system to adapt the implementation from.
     */
    public AdapterFileSystem(FileSystem adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }

    @Override
    public void mkdir(File file)
            throws IOException {
        adaptee.mkdir(file);
    }

    @Override
    public void mkfile(File file, String content)
            throws IOException {
        adaptee.mkfile(file, content);
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        adaptee.mkfile(file, input);
    }

    @Override
    public void mkfile(File file, Object... arr)
            throws IOException {
        adaptee.mkfile(file, arr);
    }

    @Override
    public void mkfile(File file, Collection<?> collection)
            throws IOException {
        adaptee.mkfile(file, collection);
    }

    @Override
    public void delete(File file)
            throws IOException {
        adaptee.delete(file);
    }

    @Override
    public void deleteAll()
            throws IOException {
        adaptee.deleteAll();
    }

    @Override
    public File getParent(File file) {
        return adaptee.getParent(file);
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        return adaptee.getInputStream(file);
    }

    @Override
    public String getContent(File file)
            throws IOException {
        return adaptee.getContent(file);
    }

    @Override
    public <T> List<T> getList(File file)
            throws IOException {
        return adaptee.getList(file);
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        return adaptee.getArray(file);
    }

    @Override
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        return adaptee.getArray(file, type);
    }

    @Override
    public File[] listChilderen(File dir) {
        return adaptee.listChilderen(dir);
    }

    @Override
    public boolean exists(File file) {
        return adaptee.exists(file);
    }

    @Override
    public boolean isDirectory(File file) {
        return adaptee.isDirectory(file);
    }

    @Override
    public boolean isFile(File file) {
        return adaptee.isFile(file);
    }

    @Override
    public boolean isHidden(File file) {
        return adaptee.isHidden(file);
    }

    @Override
    public boolean isReadable(File file) {
        return adaptee.isReadable(file);
    }
}
