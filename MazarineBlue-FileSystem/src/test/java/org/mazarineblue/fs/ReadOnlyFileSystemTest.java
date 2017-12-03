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
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.util.AdapterFileSystemSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ReadOnlyFileSystemTest
        extends AdapterFileSystemTest {

    private AdapterFileSystemSpy spy;
    private FileSystem fs;

    @Before
    public void setup2() {
        spy = getSpy();
        fs = getFs();
    }

    @After
    public void teardown2() {
        spy = null;
        fs = null;
    }

    @Override
    protected AdapterFileSystem createFileSystem(AdapterFileSystemSpy spy) {
        return new ReadOnlyFileSystem(spy);
    }

    @Test(expected = IOException.class)
    @Override
    public void mkdir()
            throws IOException {
        fs.mkdir(null);
    }

    @Test(expected = IOException.class)
    @Override
    public void mkfile_Content()
            throws IOException {
        fs.mkfile(null, "oof");
    }

    @Test(expected = IOException.class)
    @Override
    public void mkdir_InputStream()
            throws IOException {
        fs.mkfile(null, new BufferedInputStream(null));
    }

    @Test(expected = IOException.class)
    @Override
    public void mkfile_Array()
            throws IOException {
        fs.mkfile(null, new Object[]{"abc", "def"});
    }

    @Test(expected = IOException.class)
    @Override
    public void mkfile_Collection()
            throws IOException {
        fs.mkfile(null, new ArrayList<>(4));
    }

    @Test
    @Override
    public void isReadable_False() {
        spy.setIsReadable(false);
        assertTrue(fs.isReadable(null));
    }

    @Test
    @Override
    public void isReadable_True() {
        spy.setIsReadable(true);
        assertTrue(fs.isReadable(null));
    }
}
