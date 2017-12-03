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

import org.junit.After;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.mazarineblue.fs.util.AdapterFileSystemSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SocketFileSystemTest
        extends AdapterFileSystemTest {

    private AdapterFileSystemSpy spy;
    private AdapterFileSystem fs;

    @After
    public void teardown2() {
        spy = null;
        fs = null;
    }

    @Override
    protected AdapterFileSystem createFileSystem(AdapterFileSystemSpy spy) {
        this.spy = spy;
        return fs = new SocketFileSystem(spy);
    }

    @Test
    @Override
    public void isDirectory_False() {
        spy.setIsDirectory(false);
        assertFalse(fs.isDirectory(null));
    }

    @Test
    @Override
    public void isDirectory_True() {
        spy.setIsDirectory(true);
        assertFalse(fs.isDirectory(null));
    }

    @Test
    @Override
    public void isFile_False() {
        spy.setIsFile(false);
        assertFalse(fs.isFile(null));
    }

    @Test
    @Override
    public void isFile_True() {
        spy.setIsFile(true);
        assertFalse(fs.isFile(null));
    }
}
