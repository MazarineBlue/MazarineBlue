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
package org.mazarineblue.swingrunner.screens.main;

import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.swingrunner.ExceptionHandler;
import org.mazarineblue.swingrunner.FileSelector;
import org.mazarineblue.swingrunner.config.Config;
import org.mazarineblue.swingrunner.exceptions.ConfigNotSetException;
import org.mazarineblue.swingrunner.exceptions.ExceptionHandlerNotSetException;
import org.mazarineblue.swingrunner.exceptions.FeedExecutorFactoryNotSetException;
import org.mazarineblue.swingrunner.exceptions.FileSelectorNotSetException;
import org.mazarineblue.swingrunner.exceptions.FileSystemNotSetException;

/**
 * A {@code MainFrameBuilder} ensures that all required attributes by
 * {@link MainFrame} are set, which it does by acting as a staging area.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MainFrameBuilder {

    private FileSystem fs = null;
    private Config config = null;
    private FileSelector selector = null;
    private ExceptionHandler exceptionHandler = null;
    private FeedExecutorFactory feedExecutorFactory = null;

    /**
     * Verifies if all required attributes are set for {@link MainFrame}.
     */
    public void verify() {
        if (fs == null)
            throw new FileSystemNotSetException();
        if (config == null)
            throw new ConfigNotSetException();
        if (feedExecutorFactory == null)
            throw new FeedExecutorFactoryNotSetException();
        if (selector == null)
            throw new FileSelectorNotSetException();
        if (exceptionHandler == null)
            throw new ExceptionHandlerNotSetException();
    }

    FileSystem getFileSystem() {
        return fs;
    }

    public MainFrameBuilder setFileSystem(FileSystem fs) {
        this.fs = fs;
        return this;
    }

    Config getConfig() {
        return config;
    }

    public MainFrameBuilder setConfig(Config config) {
        this.config = config;
        return this;
    }

    FileSelector getFileSelector() {
        return selector;
    }

    public MainFrameBuilder setFileSelector(FileSelector selector) {
        this.selector = selector;
        return this;
    }

    ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public MainFrameBuilder setExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandler = handler;
        return this;
    }

    FeedExecutorFactory getFeedExecutorFactory() {
        return feedExecutorFactory;
    }

    public MainFrameBuilder setFeedExecutorFactory(FeedExecutorFactory feedExecutorFactory) {
        this.feedExecutorFactory = feedExecutorFactory;
        return this;
    }
}
