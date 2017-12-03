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

import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.util.AdapterFileSystemSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WriteOnlyFileSystemTest
        extends AdapterFileSystemTest {

    private AdapterFileSystemSpy spy;
    private AdapterFileSystem fs;

    @Before
    public void setup3() {
        spy = getSpy();
        fs = getFs();
    }

    @After
    public void teardown3() {
        spy = null;
        fs = null;
    }

    @Override
    protected AdapterFileSystem createFileSystem(AdapterFileSystemSpy spy) {
        return new WriteOnlyFileSystem(spy);
    }

    @Test(expected = IOException.class)
    @Override
    public void getInputStream()
            throws IOException {
        fs.getInputStream(null);
    }

    @Test(expected = IOException.class)
    @Override
    public void getContent()
            throws IOException {
        fs.getContent(null);
    }

    @Test(expected = IOException.class)
    @Override
    public void getList()
            throws IOException {
        fs.getList(null);
    }

    @Test(expected = IOException.class)
    @Override
    public void getObjectArray()
            throws IOException {
        fs.getArray(null);
    }

    @Test(expected = IOException.class)
    @Override
    public void getGenericArray()
            throws IOException {
        fs.getArray(null, String[].class);
    }

    @Test
    @Override
    public void isReadable_False() {
        spy.setIsReadable(false);
        assertFalse(fs.isReadable(null));
    }

    @Test
    @Override
    public void isReadable_True() {
        spy.setIsReadable(true);
        assertFalse(fs.isReadable(null));
    }
}
