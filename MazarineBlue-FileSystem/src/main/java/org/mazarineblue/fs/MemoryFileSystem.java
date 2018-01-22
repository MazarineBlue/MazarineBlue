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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.mazarineblue.utililities.Streams.copy;

/**
 * An {@code MemoryFileSystem} is a {@code FileSystem} that is fully in memory.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MemoryFileSystem
        extends AbstractFileSystem {

    private final Map<String, Payload> files = new HashMap<>(16);
    private final DirectoryPayload directoryMarker = new DirectoryPayload();

    @Override
    public String toString() {
        return "size = " + files.size();
    }

    @Override
    public void mkdir(File file)
            throws IOException {
        if (files.containsKey(file.getPath()))
            throw new IOException("Directory already exists: " + file);
        files.put(file.getPath(), directoryMarker);
    }

    @Override
    public void mkfile(File file, String content) {
        byte[] bytes = content.getBytes(UTF_8);
        files.put(file.getPath(), new FilePayload(bytes));
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        files.put(file.getPath(), new FilePayload(output.toByteArray()));
    }

    @Override
    public void mkfile(File file, Collection<?> collection)
            throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        mkdir(data, collection);
        files.put(file.getPath(), new FilePayload(data.toByteArray()));
    }

    @Override
    public void deleteAll() {
        files.clear();
    }

    @Override
    public void delete(File file)
            throws IOException {
        if (!files.containsKey(file.getPath()))
            throw new IOException("File does not exists: " + file);
        if (isDirectoryHelper(file) && listChilderen(file).length != 0)
            throw new IOException("Directory not empty: " + file);
        files.remove(file.getParent());
    }

    @Override
    public boolean isDirectory(File file) {
        return isDirectoryHelper(file);
    }

    private boolean isDirectoryHelper(File file) {
        Payload payload = files.get(file.getPath());
        return payload != null && payload instanceof DirectoryPayload;
    }

    @Override
    public File[] listChilderen(File dir) {
        return listChilderenHelper(dir);
    }

    private File[] listChilderenHelper(File dir) {
        List<File> list = findFiles(dir.getPath());
        list.sort(null);
        return list.toArray(new File[list.size()]);
    }

    private List<File> findFiles(String base) {
        List<File> list = new ArrayList<>(16);
        files.keySet().parallelStream()
                .filter(path -> isPathParentOf(base, path))
                .forEach(path -> list.add(new File(path)));
        return list;
    }

    private boolean isPathParentOf(String base, String path) {
        return !base.equals(path) && path.startsWith(base);
    }


    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        String path = file.getPath();
        if (files.containsKey(path))
            return getInputStream(path);
        throw new FileNotFoundException(path);
    }

    private ByteArrayInputStream getInputStream(String path)
            throws IOException {
        if (!files.containsKey(path))
            throw new FileNotFoundException("File is not found: " + path);
        byte[] arr = files.get(path).getData();
        return new ByteArrayInputStream(arr);
    }

    @Override
    public boolean exists(File file) {
        return files.containsKey(file.getPath());
    }

    @Override
    public boolean isFile(File file) {
        Payload payload = files.get(file.getPath());
        return payload != null && payload instanceof FilePayload;
    }

    @Override
    public boolean isHidden(File file) {
        return false;
    }

    @Override
    public boolean isReadable(File file) {
        return files.get(file.getPath()) != null;
    }

    @FunctionalInterface
    private static interface Payload {

        byte[] getData()
                throws IOException;
    }

    private static class DirectoryPayload
            implements Payload {

        @Override
        public String toString() {
            return "directory";
        }

        @Override
        public byte[] getData()
                throws IOException {
            throw new IOException("Can not read from directory");
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return getClass() == obj.getClass();
        }
    }

    private static class FilePayload
            implements Payload {

        private final byte[] data;

        private FilePayload(byte[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "size = " + data.length;
        }

        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        @Override
        public byte[] getData() {
            return data;
        }

        @Override
        public int hashCode() {
            return 7 * 79
                    + Arrays.hashCode(this.data);
        }

        @Override
        public boolean equals(Object obj) {
            return getClass() == obj.getClass()
                    && Arrays.equals(this.data, ((FilePayload) obj).data);
        }
    }

    @Override
    public int hashCode() {
        return files.entrySet().stream()
                .map(Entry::hashCode)
                .reduce(7, (hash, value) -> 83 * hash + value);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && equals((MemoryFileSystem) obj);
    }

    private boolean equals(final MemoryFileSystem other) {
        return files.keySet().stream()
                .allMatch(keyword -> other.files.containsKey(keyword) && files.get(keyword).equals(other.files.get(
                        keyword)));
    }
}
