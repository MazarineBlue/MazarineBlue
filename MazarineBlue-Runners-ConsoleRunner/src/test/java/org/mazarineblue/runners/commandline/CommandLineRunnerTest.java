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
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.executors.util.AbstractExecutorFactoryTestHelper;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.utilities.util.TestException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CommandLineRunnerTest
        extends AbstractExecutorFactoryTestHelper {

    private final static String PROPER_FILE = "demo.txt";
    private final static String UNSUPPORTED_FILE = "demo.xls";

    private CommandLineRunner cli;

    @Before
    public void setup()
            throws IOException {
        mkfile(new File(PROPER_FILE), "| Keyword | Argument |");
        mkfile(new File(UNSUPPORTED_FILE), "");
        cli = new CommandLineRunner(getOutput(), getFactory());
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        cli = null;
    }

    @Test
    public void nullArgument_RuntimeExceptionThrown_CausesUnreadableFeeds() {
        addLink(() -> new ExceptionThrowingSubscriber<>(t -> true, t -> new TestException()));
        cli.setArguments((String[]) null);
        cli.start();
        throwFirstException();
        assertHelpPrinted(true);
        assertProcessingFiles();
        assertMissingFiles();
        assertUnreadableFiles();
        assertFilesNotSupported();
    }

    @Test
    public void zeroArgument_RuntimeExceptionThrown_CausesUnreadableFeeds() {
        addLink(() -> new ExceptionThrowingSubscriber<>(t -> true, t -> new TestException()));
        cli.setArguments();
        cli.start();
        throwFirstException();
        assertHelpPrinted(true);
        assertProcessingFiles();
        assertMissingFiles();
        assertUnreadableFiles();
        assertFilesNotSupported();
    }

    @Test(expected = TestException.class)
    public void singleArgument_RuntimeExceptionThrown_CausesUnreadableFeeds() {
        addLink(() -> new ExceptionThrowingSubscriber<>(t -> true, t -> new TestException()));
        cli.execute(PROPER_FILE);
        throwFirstException();
        assertHelpPrinted(true);
        assertProcessingFiles(PROPER_FILE);
        assertMissingFiles();
        assertUnreadableFiles();
        assertFilesNotSupported();
    }

    @Test
    public void singleArgument_Processing() {
        addLink(() -> new ConsumeEventsSubscriber<>());
        cli.execute(PROPER_FILE);
        throwFirstException();
        assertHelpPrinted(false);
        assertProcessingFiles(PROPER_FILE);
        assertMissingFiles();
        assertUnreadableFiles();
        assertFilesNotSupported();
        assertTrue(exists(new File("demo-log-1.xml")));
        assertFalse(exists(new File("demo-log-2.xml")));
    }

    @Test
    public void multipleArgument_Processing() {
        addLink(() -> new ConsumeEventsSubscriber<>());
        cli.execute(new String[]{PROPER_FILE, PROPER_FILE});
        throwFirstException();
        assertHelpPrinted(false);
        assertProcessingFiles(PROPER_FILE, PROPER_FILE);
        assertMissingFiles();
        assertUnreadableFiles();
        assertFilesNotSupported();
        assertTrue(exists(new File("demo-log-1.xml")));
        assertTrue(exists(new File("demo-log-2.xml")));
    }
}
