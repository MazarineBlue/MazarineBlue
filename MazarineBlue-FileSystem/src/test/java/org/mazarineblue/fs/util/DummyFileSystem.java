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
package org.mazarineblue.fs.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import org.mazarineblue.fs.FileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DummyFileSystem
        implements FileSystem {

    public DummyFileSystem() {
    }

    @Override
    public void mkdir(File dir)
            throws IOException {
    }

    @Override
    public void mkfile(File file, String content)
            throws IOException {
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
    }

    @Override
    public void mkfile(File file, Object... arr)
            throws IOException {
    }

    @Override
    public void mkfile(File file, Collection<?> col)
            throws IOException {
    }

    @Override
    public void delete(File file)
            throws IOException {
    }

    @Override
    public void deleteAll()
            throws IOException {
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        return new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public String getContent(File file)
            throws IOException {
        return "";
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        return new Object[0];
    }

    @Override
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> List<T> getList(File file)
            throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File[] listChilderen(File dir) {
        return new File[0];
    }

    @Override
    public boolean exists(File file) {
        return false;
    }

    @Override
    public boolean isDirectory(File file) {
        return false;
    }

    @Override
    public boolean isFile(File file) {
        return false;
    }

    @Override
    public boolean isHidden(File file) {
        return false;
    }

    @Override
    public boolean isReadable(File file) {
        return false;
    }

    @Override
    public File getParent(File file) {
        return new File(".");
    }
}
