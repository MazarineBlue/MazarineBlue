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

import static java.lang.System.lineSeparator;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.ExecutorListener;
import org.mazarineblue.executors.util.ConsoleLogger;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ConsoleOutputTest {

    private ConsoleLogger util;
    private ExecutorListener consoleOutput;

    @Before
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void setup() {
        util = new ConsoleLogger();
        consoleOutput = new ConsoleOutput();
    }

    @After
    public void teardown() {
        util.restore();
        util = null;
        consoleOutput = null;
    }

    @Test
    public void testReportProcessingAnonymousFeed() {
        consoleOutput.reportProcessingFeed((Feed) null);
        assertEquals("INFO: Processing anonymous feed" + lineSeparator(), util.toString());
    }

    @Test
    public void testReportException() {
        consoleOutput.reportException(new Exception("abc", new Exception("foo")));
        String str = "Error: abc";
        assertEquals("SEVERE: " + str + lineSeparator() + str + lineSeparator(), util.toString());
    }
}
