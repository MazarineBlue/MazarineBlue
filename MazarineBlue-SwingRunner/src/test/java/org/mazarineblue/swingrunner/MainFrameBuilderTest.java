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
package org.mazarineblue.swingrunner;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.executors.FeedExecutorOutput;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.swingrunner.config.Config;
import org.mazarineblue.swingrunner.exceptions.ConfigNotSetException;
import org.mazarineblue.swingrunner.exceptions.ExceptionHandlerNotSetException;
import org.mazarineblue.swingrunner.exceptions.FeedExecutorFactoryNotSetException;
import org.mazarineblue.swingrunner.exceptions.FileSelectorNotSetException;
import org.mazarineblue.swingrunner.exceptions.FileSystemNotSetException;
import org.mazarineblue.swingrunner.screens.main.MainFrameBuilder;
import org.mazarineblue.swingrunner.util.TestFileSelector;

public class MainFrameBuilderTest {

    private MainFrameBuilder builder;

    @Before
    public void setup() {
        builder = new MainFrameBuilder();
        MemoryFileSystem fs = new MemoryFileSystem();
        builder.setFileSystem(fs);
        builder.setConfig(new Config(fs));
        builder.setFileSelector(new TestFileSelector(new File(".")));
        builder.setExceptionHandler(new DummyExceptionHandler());
        FeedExecutorOutput output = new FeedExecutorOutputSpy(2);
        builder.setFeedExecutorFactory(TestFeedExecutorFactory.getDefaultInstance(fs, output));
    }

    @After
    public void teardown() {
        builder = null;
    }

    @Test(expected = FileSystemNotSetException.class)
    public void setFileSystem_Null_ThrowsException() {
        builder.setFileSystem(null);
        builder.verify();
    }

    @Test(expected = ConfigNotSetException.class)
    public void setConfig_Null_ThrowsException() {
        builder.setConfig(null);
        builder.verify();
    }

    @Test(expected = FeedExecutorFactoryNotSetException.class)
    public void setInterpreterFactory_Null_ThrowsException() {
        builder.setFeedExecutorFactory(null);
        builder.setFeedExecutorFactory(null);
        builder.verify();
    }

    @Test(expected = FileSelectorNotSetException.class)
    public void setFileSelector_Null_ThrowsException() {
        builder.setFileSelector(null);
        builder.verify();
    }

    @Test(expected = ExceptionHandlerNotSetException.class)
    public void setExceptionHandler_Null_ThrowsException() {
        builder.setExceptionHandler(null);
        builder.verify();
    }
}
