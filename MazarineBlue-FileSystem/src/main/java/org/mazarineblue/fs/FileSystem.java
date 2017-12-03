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
 * A {@code FileSystem} is an interface between the file system and the application. This allows for easily
 * interchangeable connections.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface FileSystem {

    /**
     * Creates a directory.
     *
     * @param dir the directory to be created.
     * @throws IOException when the directory couldn't be created.
     */
    public void mkdir(File dir)
            throws IOException;

    /**
     * Creates a file with content.
     *
     * @param file    the file to be created.
     * @param content the content that file must have.
     * @throws IOException when the file couldn't be created.
     */
    public void mkfile(File file, String content)
            throws IOException;

    /**
     * Creates a file with content from a input stream.
     *
     * @param file  the file to be created.
     * @param input the file content.
     * @throws IOException when the file couldn't be created.
     */
    public void mkfile(File file, InputStream input)
            throws IOException;

    /**
     * Creates a file containing a number of objects.
     *
     * @param file the file to be created.
     * @param arr  the file content.
     * @throws IOException when the file couldn't be created.
     */
    public void mkfile(File file, Object... arr)
            throws IOException;

    /**
     * Creates a file containing a number of objects.
     *
     * @param file the file to be created.
     * @param col  an object holding a number of objects.
     * @throws IOException when the file couldn't be created.
     */
    public void mkfile(File file, Collection<?> col)
            throws IOException;

    /**
     * Deletes the file.
     *
     * @param file the delete
     * @throws IOException when the file couldn't be deleted.
     */
    public void delete(File file)
            throws IOException;

    /**
     * Delete all files (optional operation).
     *
     * @throws IOException when some files couldn't be deleted.
     */
    public void deleteAll()
            throws IOException;

    /**
     * Obtains bytes from a file as a input stream.
     *
     * @param file the file to fetch the bytes from.
     * @return the bytes as an input stream.
     *
     * @throws IOException when the file couldn't be read.
     */
    public InputStream getInputStream(File file)
            throws IOException;

    /**
     * Obtains the content from a file.
     *
     * @param file the file to fetch the bytes from.
     * @return the content of the file.
     *
     * @throws IOException when the file couldn't be read.
     */
    public String getContent(File file)
            throws IOException;

    /**
     * Obtains an array of objects that where serialized.
     *
     * @param file te file to fetch the objects from.
     * @return an arry of the objects serialized to the file.
     *
     * @throws IOException when the file couldn't be read.
     */
    public Object[] getArray(File file)
            throws IOException;

    /**
     * Obtains an array of objects that ware serialized.
     *
     * @param <T>  the type to use in the array.
     * @param file the file to fetch the objects from.
     * @param type the type of objects to expect and return.
     * @return an array of the objects serialized to the file.
     *
     * @throws IOException when the file couldn't be read.
     */
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException;

    /**
     * Returns the serialized objects as a list.
     *
     * @param <T>  the type to store in the list.
     * @param file the file to fetch the objects from.
     * @return an list with the objects serialized to the file.
     *
     * @throws IOException when the file couldn't be read.
     */
    public <T> List<T> getList(File file)
            throws IOException;

    /**
     * List the files within the specified directory.
     *
     * @param dir the directory containing the files.
     * @return the files that live within the directory.
     */
    public File[] listChilderen(File dir);

    /**
     * Test whether the specified file exists.
     *
     * @param file the file to look for.
     * @return true if the file exists.
     */
    public boolean exists(File file);

    /**
     * Test whether the specified file is a directory.
     *
     * @param file the file to test.
     * @return true if the file is a directory.
     */
    public boolean isDirectory(File file);

    /**
     * Test whether the specified file is a file.
     *
     * @param file the file to test.
     * @return true if the file is a file.
     */
    public boolean isFile(File file);

    /**
     * Test whether the specified file is hidden.
     *
     * @param file the file to test.
     * @return true if the file is hidden.
     */
    public boolean isHidden(File file);

    /**
     * Test whether the specified file is readable.
     *
     * @param file the file to test.
     * @return true if the file is a readable.
     */
    public boolean isReadable(File file);

    /**
     * Gets the parent of the specified file.
     *
     * @param file the file to test.
     * @return the parent of the specified file.
     */
    public File getParent(File file);
}
