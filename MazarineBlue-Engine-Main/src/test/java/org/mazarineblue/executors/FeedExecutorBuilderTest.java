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
package org.mazarineblue.executors;

import java.io.File;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.FileTransformer;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.util.DummyFileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedExecutorBuilderTest {

    private ExecutorBuilder builder;

    @Before
    public void setup() {
        builder = new ExecutorBuilder();
    }

    @After
    public void teardown() {
        builder = null;
    }

    @Test
    public void getBaseFile_BaseLine() {
        assertEquals(new File(""), builder.getBaseFile());
    }

    @Test
    public void setBaseFile() {
        File file = new File("foo");
        builder.setBaseFile(file);
        assertEquals(file, builder.getBaseFile());
    }

    @Test
    public void getFileTransformer_BaseLine() {
        assertEquals(FileTransformer.newInstance(), builder.getFileTransformer());
    }

    @Test
    public void setFileTransformer() {
        FileTransformer fileTransformer = (parent, child) -> {
            throw new UnsupportedOperationException("Not supported yet.");
        };
        builder.setFileTransformer(fileTransformer);
        assertEquals(fileTransformer, builder.getFileTransformer());
    }

    @Test
    public void getFileSystem_BaseLine() {
        assertEquals(new DiskFileSystem(), builder.getFileSystem());
    }

    @Test
    public void setFileSystem() {
        FileSystem fileSystem = new DummyFileSystem();
        builder.setFileSystem(fileSystem);
        assertEquals(fileSystem, builder.getFileSystem());
    }

    @Test
    public void getOutput_BaseLine() {
        assertEquals(ExecutorListener.getDummy(), builder.getListener());
    }

    @Test
    public void setOutput() {
        ExecutorListener output = new FeedExecutorOutputSpy();
        builder.setOutput(output);
        assertEquals(output, builder.getListener());
    }
}
