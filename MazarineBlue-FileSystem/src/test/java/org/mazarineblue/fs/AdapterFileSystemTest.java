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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.util.AdapterFileSystemSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AdapterFileSystemTest {

    private File file;
    private List<File> files;
    private AdapterFileSystemSpy spy;
    private AdapterFileSystem fs;

    @Before
    public void setup() {
        file = new File("foo");
        files = new ArrayList<>(1);
        files.add(file);
        spy = new AdapterFileSystemSpy();
        fs = createFileSystem(spy);
    }

    @After
    public void teardown() {
    }

    protected AdapterFileSystem createFileSystem(AdapterFileSystemSpy spy) {
        return new AdapterFileSystem(spy);
    }

    public AdapterFileSystemSpy getSpy() {
        return spy;
    }

    public AdapterFileSystem getFs() {
        return fs;
    }

    @Test
    public void mkdir()
            throws IOException {
        fs.mkdir(file);
        assertEquals("mkdir", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void mkfile_Content()
            throws IOException {
        fs.mkfile(file, "content");
        assertEquals("mkfile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
        assertEquals("content", spy.getArgument2());
    }

    @Test
    public void mkdir_InputStream()
            throws IOException {
        InputStream input = new BufferedInputStream(null);
        fs.mkfile(file, input);
        assertEquals("mkfile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
        assertEquals(input, spy.getArgument2());
    }

    @Test
    public void mkfile_Array()
            throws IOException {
        Object[] arr = new Object[]{"abc", "def"};
        fs.mkfile(file, arr);
        assertEquals("mkfile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
        assertEquals(arr, spy.getArgument2());
    }

    @Test
    public void mkfile_Collection()
            throws IOException {
        Collection<String> collection = new ArrayList<>(4);
        collection.add("foo");
        fs.mkfile(file, collection);
        assertEquals("mkfile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
        assertEquals(collection, spy.getArgument2());
    }

    @Test
    public void delete()
            throws IOException {
        fs.delete(file);
        assertEquals("delete", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void deleteAll()
            throws IOException {
        fs.deleteAll();
        assertEquals("deleteAll", spy.getCalledMethod());
    }

    @Test
    public void getParent() {
        File expected = new File("foo");
        spy.setParent(expected);
        assertEquals(expected, fs.getParent(file));
        assertEquals("getParent", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void getInputStream()
            throws IOException {
        InputStream expected = new BufferedInputStream(null);
        spy.setInput(expected);
        assertEquals(expected, fs.getInputStream(file));
        assertEquals("getInputStream", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void getContent()
            throws IOException {
        String expected = "abc";
        spy.setContent(expected);
        assertEquals(expected, fs.getContent(file));
        assertEquals("getContent", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void getList()
            throws IOException {
        List<String> expected = new ArrayList<>(2);
        expected.add("abc");
        expected.add("def");
        spy.setList(expected);
        assertEquals(expected, fs.getList(file));
        assertEquals("getList", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void getObjectArray()
            throws IOException {
        Object[] expected = new Object[]{"abc", "def"};
        spy.setArray(expected);
        assertArrayEquals(expected, fs.getArray(file));
        assertEquals("getArray", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void getGenericArray()
            throws IOException {
        String[] expected = new String[]{"abc", "def"};
        spy.setArray(expected);
        String[] actual = fs.getArray(file, String[].class);
        assertArrayEquals(expected, actual);
        assertEquals("getArray", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void listChilderen() {
        File[] expected = new File[]{file, file};
        spy.setFiles(expected);
        assertArrayEquals(expected, fs.listChilderen(file));
        assertEquals("listChilderen", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void exists_False() {
        spy.setFiles(new File[]{});
        assertFalse(fs.exists(file));
        assertEquals("exists", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void exists_True() {
        spy.setFiles(new File[]{file});
        assertTrue(fs.exists(file));
        assertEquals("exists", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isDirectory_False() {
        spy.setIsDirectory(false);
        assertFalse(fs.isDirectory(file));
        assertEquals("isDirectory", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isDirectory_True() {
        spy.setIsDirectory(true);
        assertTrue(fs.isDirectory(file));
        assertEquals("isDirectory", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isFile_False() {
        spy.setIsFile(false);
        assertFalse(fs.isFile(file));
        assertEquals("isFile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isFile_True() {
        spy.setIsFile(true);
        assertTrue(fs.isFile(file));
        assertEquals("isFile", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isHidden_False() {
        spy.setIsHidden(false);
        assertFalse(fs.isHidden(file));
        assertEquals("isHidden", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isHidden_True() {
        spy.setIsHidden(true);
        assertTrue(fs.isHidden(file));
        assertEquals("isHidden", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isReadable_False() {
        spy.setIsReadable(false);
        assertFalse(fs.isReadable(file));
        assertEquals("isReadable", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }

    @Test
    public void isReadable_True() {
        spy.setIsReadable(true);
        assertTrue(fs.isReadable(file));
        assertEquals("isReadable", spy.getCalledMethod());
        assertEquals(file, spy.getArgument1());
    }
}
