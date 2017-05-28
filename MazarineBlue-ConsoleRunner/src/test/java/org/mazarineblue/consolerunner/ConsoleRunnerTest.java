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
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.ExceptionThrowingLink;
import org.mazarineblue.eventdriven.util.TestInvokerEvent;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestException;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.links.util.EventConvertorLink;
import org.mazarineblue.plugins.PluginLoader;

public class ConsoleRunnerTest {

    private final static String PROPER_FILE = "demo.txt";
    private final static String UNSUPPORTED_FILE = "demo.xls";
    private final static String MISSING_FILE = "demo.xlsx";

    private ConsoleRunner cli;
    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutorFactory feedExecutorFactory;

    @Before
    public void setup()
            throws IOException {
        fs = new MemoryFileSystem();
        fs.mkfile(new File(PROPER_FILE), "| Keyword | Argument |");
        fs.mkfile(new File(UNSUPPORTED_FILE), "");
        output = new FeedExecutorOutputSpy(2);
        feedExecutorFactory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
        cli = new ConsoleRunner(output, feedExecutorFactory);
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        output = null;
        cli = null;
    }

    @Test
    public void nullArguments_HelpPrinted() {
        cli.execute((String[]) null);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[0], output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
    }

    @Test
    public void noArguments_helpPrinted() {
        cli.execute(new String[0]);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[0], output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
    }

    @Test
    public void singleArgument_MissingFile_HelpPrinted() {
        cli.execute(MISSING_FILE);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[0], output.getProcessingFiles());
        assertArrayEquals(new String[]{MISSING_FILE}, output.getMissingFiles());
    }

    @Test
    public void singleArgument_RuntimeExceptionThrown_CausesUnreadableFeeds() {
        feedExecutorFactory.addLink(() -> new ExceptionThrowingLink(TestException::new));
        cli.execute(PROPER_FILE);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[]{PROPER_FILE}, output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
        assertArrayEquals(new String[0], output.getUnreadableFiles());
        assertArrayEquals(new String[0], output.getFilesNotSupported());
        assertEquals(1, output.getThrownExceptions().length);
        assertEquals(TestException.class, output.getThrownExceptions()[0].getClass());
    }

    @Test
    public void singleArgument_FileNotSupportedExceptionThrown_CausesUnreadableFeed() {
        cli.execute(UNSUPPORTED_FILE);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[]{UNSUPPORTED_FILE}, output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getUnreadableFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
        assertArrayEquals(new String[]{UNSUPPORTED_FILE}, output.getFilesNotSupported());
        assertEquals(0, output.getThrownExceptions().length);
    }

    @Test
    public void singleArgument_UnsupprtedFeed_CausesCausesUnreadableFeed() {
        cli.execute(UNSUPPORTED_FILE);
        assertTrue(output.helpPrinted());
        assertArrayEquals(new String[]{UNSUPPORTED_FILE}, output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
        assertArrayEquals(new String[0], output.getUnreadableFiles());
        assertArrayEquals(new String[]{UNSUPPORTED_FILE}, output.getFilesNotSupported());
        assertEquals(0, output.getThrownExceptions().length);
    }

    @Test
    public void singleArgument_Processing() {
        feedExecutorFactory.addLink(() -> new EventConvertorLink(new TestInvokerEvent()));
        cli.execute(PROPER_FILE);
        assertFalse(output.helpPrinted());
        assertArrayEquals(new String[]{PROPER_FILE}, output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
        assertArrayEquals(new String[0], output.getUnreadableFiles());
        assertArrayEquals(new String[0], output.getFilesNotSupported());
        assertEquals(0, output.getThrownExceptions().length);
        assertTrue(fs.exists(new File("demo-log-1.xml")));
        assertFalse(fs.exists(new File("demo-log-2.xml")));
    }

    @Test
    public void multipleArgument_Processing() {
        feedExecutorFactory.addLink(() -> new ConsumeEventsLink());
        cli.execute(new String[]{PROPER_FILE, PROPER_FILE});
        assertFalse(output.helpPrinted());
        assertArrayEquals(new String[]{PROPER_FILE, PROPER_FILE}, output.getProcessingFiles());
        assertArrayEquals(new String[0], output.getMissingFiles());
        assertArrayEquals(new String[0], output.getUnreadableFiles());
        assertArrayEquals(new String[0], output.getFilesNotSupported());
        assertEquals(0, output.getThrownExceptions().length);
        assertTrue(fs.exists(new File("demo-log-1.xml")));
        assertTrue(fs.exists(new File("demo-log-2.xml")));
    }
}
