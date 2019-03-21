/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

class DummyExecutorListener
        implements ExecutorListener {

    @Override
    public void printHelp() {
        // A dummy does nothing.
    }

    @Override
    public void start() {
        // A dummy does nothing.
    }

    @Override
    public void stop() {
        // A dummy does nothing.
    }

    @Override
    public void reportFileMissing(File file) {
        // A dummy does nothing.
    }

    @Override
    public void reportProcessingFeed(File file) {
        // A dummy does nothing.
    }

    @Override
    public void reportProcessingFeed(Feed feed) {
        // A dummy does nothing.
    }

    @Override
    public void reportFileNotSupported(File file) {
        // A dummy does nothing.
    }

    @Override
    public void reportUnreadableFile(File file) {
        // A dummy does nothing.
    }

    @Override
    public void reportUnwritableFile(File file) {
        // A dummy does nothing.
    }

    @Override
    public void reportException(Exception ex) {
        // A dummy does nothing.
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass();
    }
}
