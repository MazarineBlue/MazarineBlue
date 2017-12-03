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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MemoryFileSystemTest
        extends AbstractFileSystemTest {

    private MemoryFileSystem fs;

    @Override
    protected FileSystem getFileSystem() {
        return fs = new MemoryFileSystem();
    }

    @Override
    protected FileSystem getDifferentFileSystem() {
        return new DiskFileSystem();
    }

    @Test
    public void hashCode_DifferentDirectories()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        other.mkdir(new File("oof"));
        int a = fs.hashCode();
        int b = other.hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFileNames()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo"), "foo");
        other.mkfile(new File("oof"), "foo");
        int a = fs.hashCode();
        int b = other.hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFileContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo"), "foo");
        other.mkfile(new File("foo"), "oof");
        int a = fs.hashCode();
        int b = other.hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalFileContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo", "foo"), "foo");
        other.mkfile(new File("foo", "foo"), "foo");
        int a = fs.hashCode();
        int b = other.hashCode();
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalDirectories()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        other.mkdir(new File("foo"));
        int a = fs.hashCode();
        int b = other.hashCode();
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        fs.mkfile(new File("foo", "foo"), "foo");
        other.mkdir(new File("foo"));
        other.mkfile(new File("foo", "foo"), "foo");
        int a = fs.hashCode();
        int b = other.hashCode();
        assertEquals(a, b);
    }

    @Test
    public void equals_DifferentDirectories()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        other.mkdir(new File("oof"));
        int a = fs.hashCode();
        int b = other.hashCode();
        assertFalse(fs.equals(other));
    }

    @Test
    public void equals_IdenticalDirectories()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        other.mkdir(new File("foo"));
        assertTrue(fs.equals(other));
    }

    @Test
    public void equals_DifferentFileNames()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo"), "foo");
        other.mkfile(new File("oof"), "foo");
        assertFalse(fs.equals(other));
    }

    @Test
    public void equals_DifferentFileContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo"), "foo");
        other.mkfile(new File("foo"), "oof");
        assertFalse(fs.equals(other));
    }

    @Test
    public void equals_IdenticalFileContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo", "foo"), "foo");
        other.mkfile(new File("foo", "foo"), "foo");
        assertTrue(fs.equals(other));
    }

    @Test
    public void equals_FileSystemsMismatch1()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkfile(new File("foo"), "foo");
        other.mkdir(new File("foo"));
        assertFalse(fs.equals(other));
    }

    @Test
    public void equals_FileSystemsMismatch2()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        other.mkfile(new File("foo"), "foo");
        assertFalse(fs.equals(other));
    }

    @Test
    public void equals_IdenticalContent()
            throws Exception {
        MemoryFileSystem other = new MemoryFileSystem();
        fs.mkdir(new File("foo"));
        fs.mkfile(new File("foo", "foo"), "foo");
        other.mkdir(new File("foo"));
        other.mkfile(new File("foo", "foo"), "foo");
        assertTrue(fs.equals(other));
    }

    @Override
    @Test
    public void deleteAll()
            throws IOException {
        fs.mkdir(new File("foo"));
        fs.mkfile(new File("foo", "foo"), "foo");
        fs.deleteAll();
        assertFalse(fs.exists(new File("foo")));
        assertFalse(fs.exists(new File("foo", "foo")));
    }
}
