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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.random;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.setAll;
import static java.util.Arrays.sort;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractFileSystemTest
        extends TestHashCodeAndEquals<FileSystem> {

    private FileSystem fs;
    private File base;

    @Before
    public void setup() {
        fs = getFileSystem();
        base = new File("./target/");
    }

    protected FileSystem getDifferentFileSystem() {
        return new MemoryFileSystem();
    }

    protected FileSystem getIdenticalFileSystem() {
        return getFileSystem();
    }

    protected abstract FileSystem getFileSystem();

    @After
    public void teardown() {
        fs = null;
        base = null;
    }

    @Test(expected = IOException.class)
    public void initial()
            throws IOException {
        File file = createFileName(base);
        assertFalse(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertFalse(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertFalse(fs.isReadable(file));
        fs.getInputStream(file);
    }

    @Test
    public void list()
            throws IOException {
        File dir = createFileName(base);
        File[] expected = new File[]{new File(dir, "file1"), new File(dir, "file2")};
        fs.mkdir(dir);
        fs.mkdir(createFileName(base));
        fs.mkfile(expected[0]);
        fs.mkfile(expected[1]);
        File[] actual = fs.listChilderen(dir);
        sort(actual, (left, right) -> left.getAbsolutePath().compareTo(right.getAbsolutePath()));
        assertArrayEquals(expected, actual);
        assertEquals(dir, fs.getParent(expected[0]));
    }

    @Test(expected = IOException.class)
    public void mkdir()
            throws IOException {
        File file = createFileName(base);
        fs.mkdir(file);
        File[] childeren = fs.listChilderen(base);
        assertNotEquals(-1, binarySearch(childeren, file));
        assertTrue(fs.exists(file));
        assertTrue(fs.isDirectory(file));
        assertFalse(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        fs.getInputStream(file);
    }

    @Test(expected = IOException.class)
    public void mkdir_CreateSameFileTwice()
            throws IOException {
        File f = createFileName(base);
        fs.mkdir(f);
        fs.mkdir(f);
    }

    @Test(expected = NullPointerException.class)
    public void mkfile_InputStream_NullFile()
            throws IOException {
        InputStream input = new ByteArrayInputStream(createData(1024));
        fs.mkfile(null, input);
    }

    @Test(expected = NullPointerException.class)
    public void mkfile_InputStream_NullInputStream()
            throws IOException {
        File file = createFileName(base);
        fs.mkfile(file, (InputStream) null);
    }

    @Test
    public void mkfile_InputStream()
            throws IOException {
        File file = createFileName(base);
        byte[] expected = createData(1024);
        InputStream input = new ByteArrayInputStream(expected);
        fs.mkfile(file, input);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
    }

    @Test
    public void mkfile_InputStream_CreateSameFileTwice()
            throws IOException {
        File file = createFileName(base);
        byte[] expected = createData(1024);
        fs.mkfile(file, new ByteArrayInputStream(expected));
        fs.mkfile(file, new ByteArrayInputStream(expected));
    }

    @Test
    public void mkfile_String()
            throws IOException {
        File file = createFileName(base);
        String expected = "foo foo";
        fs.mkfile(file, expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        assertEquals(expected, fs.getContent(file));
    }

    @Test
    public void mkfile_String_CreateSameFileTwice()
            throws IOException {
        File file = createFileName(base);
        String expected = "foo foo";
        fs.mkfile(file, expected);
        fs.mkfile(file, expected);
    }

    @Test(expected = NullPointerException.class)
    public void mkfile_List_NullFile()
            throws IOException {
        fs.mkfile(null, asList(new String[]{"abc", "def"}));
    }

    @Test(expected = NullPointerException.class)
    public void mkfile_List_NullList()
            throws IOException {
        File file = createFileName(base);
        fs.mkfile(file, (Collection<?>) null);
    }

    @Test
    public void mkfile_List()
            throws IOException {
        File file = createFileName(base);
        List<String> expected = asList(new String[]{"abc", "def"});
        fs.mkfile(file, expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        assertEquals(expected, fs.getList(file));
    }

    @Test
    public void mkfile_List_CreateSameFileTwice()
            throws IOException {
        File file = createFileName(base);
        List<String> expected = asList(new String[]{"abc", "def"});
        fs.mkfile(file, expected);
        fs.mkfile(file, expected);
    }

    @Test
    public void mkfile_ObjectArray()
            throws IOException {
        File file = createFileName(base);
        Object[] expected = new Object[]{"abc", "def"};
        fs.mkfile(file, expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        assertArrayEquals(expected, fs.getArray(file));
    }

    @Test
    public void mkfile_ObjectArray_CreateSameFileTwice()
            throws IOException {
        File file = createFileName(base);
        Object[] expected = new Object[]{"abc", "def"};
        fs.mkfile(file, expected);
        fs.mkfile(file, expected);
    }

    @Test(expected = IOException.class)
    public void mkfile_StringArray_Content()
            throws IOException {
        File file = createFileName(base);
        Object[] expected = new Object[]{"abc", "def", 1};
        fs.mkfile(file, expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        String[] actual = fs.getArray(file, String[].class);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void mkfile_StringArray_WrongType()
            throws IOException {
        File file = createFileName(base);
        String[] expected = new String[]{"abc", "def"};
        fs.mkfile(file, (Object[]) expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        Object[] actual = fs.getArray(file, Integer[].class);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void mkfile_StringArray_Happy()
            throws IOException {
        File file = createFileName(base);
        String[] expected = new String[]{"abc", "def"};
        fs.mkfile(file, (Object[]) expected);
        assertTrue(fs.exists(file));
        assertFalse(fs.isDirectory(file));
        assertTrue(fs.isFile(file));
        assertFalse(fs.isHidden(file));
        assertTrue(fs.isReadable(file));
        assertArrayEquals(expected, fs.getArray(file, String[].class));
    }

    @Test(expected = IOException.class)
    public void delete_FileNotFound()
            throws IOException {
        File file = createFileName(base);
        fs.delete(file);
    }

    @Test(expected = IOException.class)
    public void delete_DirectoryNotEmpty()
            throws IOException {
        File file = createFileName(base);
        fs.mkdir(file);
        fs.mkfile(new File(file, "foo"), "foo");
        fs.delete(file);
    }

    @Test
    public void delete_FileFound()
            throws IOException {
        File file = createFileName(base);
        fs.mkfile(file, "oof");
        fs.delete(file);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteAll()
            throws IOException {
        fs.deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void getInputStream_Null()
            throws IOException {
        fs.getInputStream(null);
    }

    @Test(expected = IOException.class)
    public void getInputStream_UnregistedFile()
            throws IOException {
        fs.getInputStream(createFileName(base));
    }

    @Test
    public void getInputStream_Happy()
            throws IOException {
        File file = createFileName(base);
        byte[] expected = createData(1024);
        fs.mkfile(file, new ByteArrayInputStream(expected));
        InputStream input = fs.getInputStream(file);
        assertArrayEquals(expected, convertToBytes(fs.getInputStream(file)));
    }

    private File createFileName(File base) {
        File[] childeren = fs.listChilderen(base);
        int count = (childeren == null) ? 0 : countMatches(childeren);
        return new File(base, "test-" + count);
    }

    private int countMatches(File[] childeren) {
        int count = -1;
        for (File c : childeren)
            if (c.getPath().contains("test-"))
                ++count;
        return count;
    }

    private static byte[] createData(int size) {
        byte[] arr = new byte[size];
        for (int i = 0; i < size; ++i)
            arr[i] = (byte) random();
        return arr;
    }

    private byte[] convertToBytes(InputStream input)
            throws IOException {
        return new Buffer(1024).read(input);
    }

    private static class Buffer {

        int len = 0;
        byte[] buf;

        Buffer(int capacity) {
            buf = new byte[capacity];
        }

        @SuppressWarnings({"AssignmentToMethodParameter", "NestedAssignment"})
        private byte[] read(InputStream input)
                throws IOException {
            List<Byte> list = new ArrayList<>(buf.length);
            copy(input, list);
            return convert(list);
        }

        private void copy(InputStream input, List<Byte> list)
                throws IOException {
            while (!eof())
                copySegment(input, list);
        }

        private boolean eof() {
            return len < 0;
        }

        private int copySegment(InputStream input, List<Byte> list)
                throws IOException {
            len = input.read(buf);
            for (int i = 0; i < len; ++i)
                list.add(buf[i]);
            return len;
        }

        private static byte[] convert(List<Byte> list) {
            return convert(list.toArray(new Byte[list.size()]));
        }

        private static byte[] convert(Byte[] src) {
            byte[] dst = new byte[src.length];
            setAll(src, i -> dst[i]);
            return dst;
        }
    }

    @Override
    protected FileSystem getIdenticalObject() {
        return getIdenticalFileSystem();
    }

    @Override
    protected FileSystem getObject() {
        return fs;
    }

    @Override
    protected FileSystem getDifferentObject() {
        return getDifferentFileSystem();
    }
}
