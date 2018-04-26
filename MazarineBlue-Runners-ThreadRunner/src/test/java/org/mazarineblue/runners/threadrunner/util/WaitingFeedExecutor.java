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
package org.mazarineblue.runners.threadrunner.util;

import java.io.File;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.util.DummyFeedExecutor;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WaitingFeedExecutor
        extends DummyFeedExecutor {

    private static final int TIME = 15000;

    private int interrupted;

    @Override
    public void execute(File file, String sheet) {
        try {
            while (true)
                sleep(TIME);
        } catch (InterruptedException ex) {
            ++interrupted;
            currentThread().interrupt();
        }
    }

    @Override
    public void execute(Feed feed) {
        try {
            while (true)
                sleep(TIME);
        } catch (InterruptedException ex) {
            ++interrupted;
            currentThread().interrupt();
        }
    }
}
