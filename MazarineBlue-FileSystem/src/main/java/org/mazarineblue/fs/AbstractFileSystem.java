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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.reflect.Array.newInstance;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.List;
import static org.mazarineblue.utililities.Streams.copy;

/**
 * An {@code AbstractFileSystem} is a {@code FileSystem} that implements methods using other more basis methods.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractFileSystem
        implements FileSystem {

    @Override
    public void mkfile(File file, String content)
            throws IOException {
        byte[] bytes = content.getBytes(UTF_8);
        InputStream input = new ByteArrayInputStream(bytes);
        mkfile(file, input);
    }

    @Override
    public void mkfile(File file, Object... arr)
            throws IOException {
        mkfile(file, asList(arr));
    }

    protected void mkdir(OutputStream output, Collection<?> collection)
            throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(output)) {
            stream.writeInt(collection.size());
            for (Object obj : collection)
                stream.writeObject(obj);
        }
    }

    @Override
    public <T> List<T> getList(File file)
            throws IOException {
        return getListHelper(file);
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        return getListHelper(file).toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        try {
            List<T> list = getListHelper(file);
            T[] arr = (T[]) newInstance(type.getComponentType(), list.size());
            return getListHelper(file).toArray(arr);
        } catch (ArrayStoreException ex) {
            throw new IOException("File content is not assinable to type: " + type, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getListHelper(File file)
            throws IOException {
        List<T> list = new ArrayList<>(16);
        try (ObjectInputStream input = new ObjectInputStream(getInputStream(file))) {
            int n = input.readInt();
            for (int i = 0; i < n; ++i)
                list.add((T) input.readObject());
            return list;
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String getContent(File file)
            throws IOException {
        InputStream input = getInputStream(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream(input.available());
        copy(input, output);
        return new String(output.toByteArray(), UTF_8);
    }

    @Override
    public File getParent(File file) {
        return file.getParentFile();
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass();
    }
}
