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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import static java.lang.System.lineSeparator;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.executors.util.ConsoleLogger;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.fs.WriteOnlyFileSystem;
import org.mazarineblue.plugins.RunnerPlugin;
import static org.mazarineblue.utilities.util.TestUtil.assertRegex;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class CommandLineRunnerPluginTest {

    private final static String PROPER_FILE = "demo.txt";
    private final static String LOG_FILE = "demo-log-\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}\\.xml";
    private final static String UNSUPPORTED_FILE = "demo.xls";
    private final static String MISSING_FILE = "demo.xlsx";
    private ConsoleLogger util;
    private FileSystem fs;
    private RunnerPlugin plugin;
    private CommandLineRunner cli;

    @After
    public void teardown() {
        if (util != null)
            util.restore();
        util = null;
        fs = null;
        plugin = null;
        cli = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestMetaData {

        @Before
        public void setup() {
            plugin = new CommandLineRunnerPlugin();
        }

        @Test
        public void testName() {
            assertEquals("cli", plugin.name());
        }

        @Test
        public void testDescription() {
            assertEquals("execute the feeds provided in the argument list", plugin.description());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestWithFileSystem {

        @Before
        public void setup() {
            util = new ConsoleLogger();
            MemoryFileSystem mfs = new MemoryFileSystem();
            mfs.mkfile(new File(PROPER_FILE), "| Comment | Argument |");
            mfs.mkfile(new File(UNSUPPORTED_FILE), "");
            fs = mfs;
        }

        @SuppressWarnings("PublicInnerClass")
        public class TestConsoleRunner {

            @Before
            public void setup() {
                plugin = new CommandLineRunnerPlugin(fs);
                cli = (CommandLineRunner) plugin.createRunner();
            }

            @Test
            public void nullArguments_HelpPrinted() {
                cli.execute((String[]) null);
                String format = "Usage: %s %s feed1 [feed2 [...]]" + lineSeparator();
                String expected = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME);
                assertEquals(expected, util.toString());
            }

            @Test
            public void noArguments_helpPrinted() {
                cli.execute();
                String format = "Usage: %s %s feed1 [feed2 [...]]" + lineSeparator();
                String expected = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME);
                assertEquals(expected, util.toString());
            }

            @Test
            public void singleArgument_MissingFile_HelpPrinted() {
                cli.execute(MISSING_FILE);
                String format = "INFO: Started" + lineSeparator()
                        + "SEVERE: Feed not found: " + MISSING_FILE + lineSeparator()
                        + "Feed not found: demo.xlsx" + lineSeparator()
                        + "INFO: Stopped" + lineSeparator()
                        + "Usage: %s %s feed1 [feed2 [...]]" + lineSeparator();
                String expected = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME);
                assertEquals(expected, util.toString());
            }

            @Test
            public void singleArgument_FileNotSupported_CausesUnreadableFeed() {
                cli.execute(UNSUPPORTED_FILE);
                String format = "INFO: Started" + lineSeparator()
                        + "INFO: Processing feed: " + UNSUPPORTED_FILE + lineSeparator()
                        + "SEVERE: Feed not supported: " + UNSUPPORTED_FILE + lineSeparator()
                        + "Feed not supported: " + UNSUPPORTED_FILE + lineSeparator()
                        + "INFO: Stopped" + lineSeparator()
                        + "Usage: %s %s feed1 [feed2 [...]]" + lineSeparator();
                String expected = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME);
                assertEquals(expected, util.toString());
            }
        }

        public class SpecialCases {

            @Test
            public void fileSystemUnreadable() {
                plugin = new CommandLineRunnerPlugin(new WriteOnlyFileSystem(fs));
                cli = (CommandLineRunner) plugin.createRunner();
                cli.execute(PROPER_FILE);
                String format = "INFO: Started" + lineSeparator()
                        + "INFO: Processing feed: " + PROPER_FILE + lineSeparator()
                        + "SEVERE: File unreadable: " + PROPER_FILE + lineSeparator()
                        + "File unreadable: " + PROPER_FILE + lineSeparator()
                        + "INFO: Stopped" + lineSeparator()
                        + "Usage: %s %s feed1 [feed2 [...]]" + lineSeparator();
                String expected = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME);
                assertEquals(expected, util.toString());
            }

            @Test
            public void fileSystemUnwritable() {
                plugin = new CommandLineRunnerPlugin(new ReadOnlyFileSystem(fs));
                cli = (CommandLineRunner) plugin.createRunner();
                cli.execute(PROPER_FILE);
                String format = "INFO: Started" + lineSeparator()
                        + "INFO: Processing feed: " + PROPER_FILE + lineSeparator()
                        + "SEVERE: File unwritable: %3$s" + lineSeparator()
                        + "File unwritable: %3$s" + lineSeparator()
                        + "INFO: Stopped" + lineSeparator()
                        + "Usage: %1$s %2$s feed1 \\[feed2 \\[\\.\\.\\.]]" + lineSeparator();
                String regex = String.format(format, CommandLineRunnerPlugin.APP, CommandLineRunnerPlugin.NAME, LOG_FILE);
                assertRegex(regex, util.toString());
            }
        }
    }
}
