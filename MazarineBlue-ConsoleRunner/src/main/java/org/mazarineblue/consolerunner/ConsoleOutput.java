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
package org.mazarineblue.consolerunner;

import java.io.File;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.FeedExecutorOutput;

class ConsoleOutput
        implements FeedExecutorOutput {

    private static final Logger LOGGER = Logger.getGlobal();

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void printHelp() {
        System.err.println("Usage: app feed1 [feed2 [...]]");
    }

    @Override
    public void reportProcessingFeed(File file) {
        LOGGER.log(INFO, "Processing feed: %s", file.toString());
    }

    @Override
    public void reportProcessingFeed(Feed feed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportFileMissing(File file) {
        LOGGER.log(SEVERE, "Feed not found: %s", file.toString());
    }

    @Override
    public void reportFileNotSupported(File file) {
        LOGGER.log(SEVERE, "Feed not supported: %s", file.toString());
    }

    @Override
    public void reportUnreadableFile(File file) {
        LOGGER.log(SEVERE, "Feed unreadable: %s", file.toString());
    }

    @Override
    public void reportUnwritableFile(File file) {
        LOGGER.log(SEVERE, "Feed unwritable: %s", file.toString());
    }

    @Override
    public void reportException(Exception ex) {
        LOGGER.log(SEVERE, "Error: %s", ex.getMessage());
    }
}
