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
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.util.TestFileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AdapterFileSystemTest {

    private File file;
    private List<File> files;
    private TestFileSystem spy;
    private FileSystem fs;

    @Before
    public void setup() {
        file = new File("foo");
        files = new ArrayList<>(1);
        files.add(file);
        spy = new TestFileSystem();
        fs = createFileSystem(spy);
    }

    @After
    public void teardown() {
    }

    protected FileSystem createFileSystem(TestFileSystem spy) {
        return new AdapterFileSystem(spy);
    }

    public TestFileSystem getSpy() {
        return spy;
    }

    public FileSystem getFs() {
        return fs;
    }

    @Test
    public void mkdir()
            throws IOException {
        fs.mkdir(file);
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void mkfileContent()
            throws IOException {
        String content = "oof";
        fs.mkfile(file, content);
        assertEquals(content, spy.getContent());
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void mkdirInputStream()
            throws IOException {
        InputStream input = new BufferedInputStream(null);
        fs.mkfile(file, input);
        assertEquals(input, spy.getInput());
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void mkfileArray()
            throws IOException {
        Object[] arr = new Object[]{"abc", "def"};
        fs.mkfile(file, arr);
        assertArrayEquals(arr, spy.getArray());
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void mkfileIterator()
            throws IOException {
        Collection<?> collection = new ArrayList<>(4);
        fs.mkfile(file, collection);
        Collection<?> spyCollection = spy.getCollection();
        assertEquals(collection.size(), spyCollection.size());
        Iterator<?> iterator = collection.iterator();
        Iterator<?> spyIterator = spyCollection.iterator();
        while (iterator.hasNext())
            assertEquals(iterator.next(), spyIterator.next());
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getParent() {
        File parent = new File("foo");
        spy.setParentFile(parent);
        assertEquals(parent, fs.getParent(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getInputStream()
            throws IOException {
        InputStream input = new BufferedInputStream(null);
        spy.setInput(input);
        assertEquals(input, fs.getInputStream(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getContent()
            throws IOException {
        String content = "abc";
        spy.setContent(content);
        assertEquals(content, fs.getContent(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getList()
            throws IOException {
        List<String> list = new ArrayList<>(2);
        list.add("abc");
        list.add("def");
        spy.setList(list);
        assertEquals(list, fs.getList(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getObjectArray()
            throws IOException {
        Object[] arr = new Object[]{"abc", "def"};
        spy.setArray(arr);
        assertArrayEquals(arr, fs.getArray(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void getGenericArray()
            throws IOException {
        String[] expected = new String[]{"abc", "def"};
        spy.setArray(expected);
        String[] actual = fs.getArray(file, String[].class);
        assertArrayEquals(expected, actual);
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void listChilderen() {
        spy.setChilderen(file);
        assertArrayEquals(new File[]{file}, fs.listChilderen(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void exists_False() {
        spy.setExists(false);
        assertFalse(fs.exists(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void exists_True() {
        spy.setExists(true);
        assertTrue(fs.exists(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isDirectory_False() {
        spy.setDirectory(false);
        assertFalse(fs.isDirectory(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isDirectory_True() {
        spy.setDirectory(true);
        assertTrue(fs.isDirectory(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isFile_False() {
        spy.setFile(false);
        assertFalse(fs.isFile(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isFile_True() {
        spy.setFile(true);
        assertTrue(fs.isFile(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isHidden_False() {
        spy.setHidden(false);
        assertFalse(fs.isHidden(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isHidden_True() {
        spy.setHidden(true);
        assertTrue(fs.isHidden(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isReadable_False() {
        spy.setReadable(false);
        assertFalse(fs.isReadable(file));
        assertEquals(files, spy.getFiles());
    }

    @Test
    public void isReadable_True() {
        spy.setReadable(true);
        assertTrue(fs.isReadable(file));
        assertEquals(files, spy.getFiles());
    }
}
