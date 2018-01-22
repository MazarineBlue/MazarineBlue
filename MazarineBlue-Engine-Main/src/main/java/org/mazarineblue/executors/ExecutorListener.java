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
import org.mazarineblue.eventdriven.Feed;

/**
 * A {@code FeedExecutorOutput} is a hook that is called when progress
 * information can be logged or shown to the end user.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface ExecutorListener {

    public static ExecutorListener getDummy() {
        return new DummyExecutorListener();
    }

    /**
     * Reports that the {@code FeedExecutor} started to process a feed.
     */
    public void start();

    /**
     * Reports that the {@code FeedExecutor} ended to process a feed.
     */
    public void stop();

    /**
     * Prints the help message on the console.
     */
    public void printHelp();

    /**
     * Reports that the specified feed is being processed.
     *
     * @param file the feed that is being processed
     */
    public void reportProcessingFeed(File file);

    /**
     * Reports that the specified feed is being processed.
     *
     * @param feed the feed that is being processed
     */
    public void reportProcessingFeed(Feed feed);

    /**
     * Reports that the specified feed is missing.
     *
     * @param file the feed that is missing
     */
    public void reportFileMissing(File file);

    /**
     * Reports that there is no support for the specified feed.
     *
     * @param file the feed that is not supported
     */
    public void reportFileNotSupported(File file);

    /**
     * Reports that the file could not be read.
     *
     * @param file the feed that is unreadable
     */
    public void reportUnreadableFile(File file);

    /**
     * Reports that the file could not be written to.
     *
     * @param file the feed that is unwritable
     */
    public void reportUnwritableFile(File file);

    /**
     * Reports that an error occurred.
     *
     * @param ex the exception thrown
     */
    public void reportException(Exception ex);
}
