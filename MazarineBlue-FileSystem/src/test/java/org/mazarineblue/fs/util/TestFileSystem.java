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
package org.mazarineblue.fs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.copyOf;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import org.mazarineblue.fs.FileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestFileSystem
        implements FileSystem {

    private final List<File> files = new ArrayList<>(4);
    private File parentFile;
    private File[] childeren;
    private boolean exists, directory, file, hidden, readable;
    private InputStream input;
    private String content;
    private Object[] arr;
    private List<?> list;
    private Collection<?> collection;

    public List<File> getFiles() {
        return unmodifiableList(files);
    }

    public File getParentFile() {
        return parentFile;
    }

    public void setParentFile(File parent) {
        this.parentFile = parent;
    }

    public void setChilderen(File... childeren) {
        this.childeren = copyOf(childeren, childeren.length);
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object[] getArray() {
        return copyOf(arr, arr.length);
    }

    public void setArray(Object[] arr) {
        this.arr = copyOf(arr, arr.length);
    }

    public List<?> getList() {
        return unmodifiableList(list);
    }

    public void setList(List<?> list) {
        this.list = unmodifiableList(list);
    }

    public Collection<?> getCollection() {
        return unmodifiableCollection(collection);
    }

    public void setIterator(Collection<?> collection) {
        this.collection = new ArrayList<>(collection);
    }

    @Override
    public void mkdir(File file)
            throws IOException {
        files.add(file);
    }

    @Override
    public void mkfile(File file, String content)
            throws IOException {
        files.add(file);
        this.content = content;
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        files.add(file);
        this.input = input;
    }

    @Override
    public void mkfile(File file, Object... arr)
            throws IOException {
        files.add(file);
        this.arr = Arrays.copyOf(arr, arr.length);
    }

    @Override
    public void mkfile(File file, Collection<?> collection)
            throws IOException {
        files.add(file);
        this.collection = new ArrayList<>(collection);
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        files.add(file);
        return input;
    }

    @Override
    public String getContent(File file)
            throws IOException {
        files.add(file);
        return content;
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        files.add(file);
        return copyOf(arr, arr.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        files.add(file);
        return copyOf((T[]) arr, arr.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(File file)
            throws IOException {
        files.add(file);
        return unmodifiableList((List<T>) list);
    }

    @Override
    public File[] listChilderen(File dir) {
        files.add(dir);
        return copyOf(childeren, childeren.length);
    }

    @Override
    public boolean exists(File file) {
        files.add(file);
        return exists;
    }

    @Override
    public boolean isDirectory(File file) {
        files.add(file);
        return directory;
    }

    @Override
    public boolean isFile(File file) {
        files.add(file);
        return this.file;
    }

    @Override
    public boolean isHidden(File file) {
        files.add(file);
        return hidden;
    }

    @Override
    public boolean isReadable(File file) {
        files.add(file);
        return readable;
    }

    @Override
    public File getParent(File file) {
        files.add(file);
        return parentFile;
    }
}
