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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import java.util.Collection;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.mazarineblue.fs.FileSystem;

public class AdapterFileSystemSpy
        implements FileSystem {

    private String calledMethod;
    private Object argument1, argument2;
    private String content;
    private Object[] array;
    private List<Object> list;
    private File[] childeren;
    private boolean isDirectory;
    private boolean isFile;
    private boolean isHidden;
    private boolean isReadable;
    private File parent;
    private InputStream input;

    public String getCalledMethod() {
        return calledMethod;
    }

    public Object getArgument1() {
        return argument1;
    }

    public Object getArgument2() {
        return argument2;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setArray(Object... array) {
        this.array = array;
    }

    public void setList(List<? extends Object> list) {
        this.list = new ArrayList<>(list);
    }

    public void setFiles(File... files) {
        this.childeren = files;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public void setIsFile(boolean flag) {
        this.isFile = flag;
    }

    public void setIsHidden(boolean flag) {
        this.isHidden = flag;
    }

    public void setIsReadable(boolean flag) {
        this.isReadable = flag;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    @Override
    public void mkdir(File dir)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = dir;
        argument2 = null;
    }

    @Override
    public void mkfile(File file, String content)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = content;
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = input;
    }

    @Override
    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void mkfile(File file, Object... arr)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = arr;
    }

    @Override
    public void mkfile(File file, Collection<?> col)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = col;
    }

    @Override
    public void delete(File file)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
    }

    @Override
    public void deleteAll()
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = argument2 = null;
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return input;
    }

    @Override
    public String getContent(File file)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return content;
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return copyOf(array, array.length);
    }

    @Override
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return copyOf((T[]) array, array.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(File file)
            throws IOException {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return list.stream().map(t -> (T) t).collect(toList());
    }

    @Override
    public File[] listChilderen(File dir) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = dir;
        argument2 = null;
        return childeren;
    }

    @Override
    public boolean exists(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return stream(childeren).anyMatch(f -> f.equals(file));
    }

    @Override
    public boolean isDirectory(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return isDirectory;
    }

    @Override
    public boolean isFile(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return isFile;
    }

    @Override
    public boolean isHidden(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return isHidden;
    }

    @Override
    public boolean isReadable(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return isReadable;
    }

    @Override
    public File getParent(File file) {
        calledMethod = new Object(){}.getClass().getEnclosingMethod().getName();
        argument1 = file;
        argument2 = null;
        return parent;
    }
}
