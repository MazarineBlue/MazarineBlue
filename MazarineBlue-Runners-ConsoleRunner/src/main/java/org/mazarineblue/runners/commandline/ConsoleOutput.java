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
package org.mazarineblue.runners.commandline;

import java.io.File;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.ExecutorListener;

class ConsoleOutput
        implements ExecutorListener {

    private static final Logger LOGGER = Logger.getGlobal();

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void printHelp() {
        System.err.println(String.format("Usage: %s %s feed1 [feed2 [...]]",
                                         CommandLineRunnerPlugin.APP,
                                         CommandLineRunnerPlugin.NAME));
    }

    @Override
    public void start() {
        LOGGER.log(INFO, "Started");
    }

    @Override
    public void stop() {
        LOGGER.log(INFO, "Stopped");
    }

    @Override
    public void reportProcessingFeed(File file) {
        LOGGER.log(INFO, "Processing feed: {0}", file);
    }

    @Override
    public void reportProcessingFeed(Feed feed) {
        LOGGER.log(INFO, "Processing anonymous feed");
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void reportFileMissing(File file) {
        String str = String.format("Feed not found: %s", file);
        LOGGER.log(SEVERE, str);
        System.err.println(str);
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void reportFileNotSupported(File file) {
        String str = String.format("Feed not supported: %s", file);
        LOGGER.log(SEVERE, str);
        System.err.println(str);
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void reportUnreadableFile(File file) {
        String str = String.format("File unreadable: %s", file);
        LOGGER.log(SEVERE, str);
        System.err.println(str);
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void reportUnwritableFile(File file) {
        String str = String.format("File unwritable: %s", file);
        LOGGER.log(SEVERE, str);
        System.err.println(str);
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void reportException(Exception ex) {
        String str = String.format("Error: %s", ex.getMessage());
        LOGGER.log(SEVERE, str);
        System.err.println(str);
    }
}
