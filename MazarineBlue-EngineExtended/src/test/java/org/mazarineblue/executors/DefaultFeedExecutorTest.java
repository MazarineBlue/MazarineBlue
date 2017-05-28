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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.ChainModifierListenerSpy;
import org.mazarineblue.eventdriven.util.ExceptionThrowingFeed;
import org.mazarineblue.eventdriven.util.FeedExecutorListenerSpy;
import org.mazarineblue.eventdriven.util.InterpreterListenerSpy;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.eventdriven.util.PublisherListenerSpy;
import org.mazarineblue.eventdriven.util.TestInvokerEvent;
import org.mazarineblue.executors.listeners.SubscriberFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.IOExceptionThrowingFeedPlugin;
import org.mazarineblue.executors.util.RuntimeExceptionThrowingFeedPlugin;
import org.mazarineblue.executors.util.SupportAllFilesFeedPlugin;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.fs.util.CorruptFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.links.util.EventConvertorLink;
import org.mazarineblue.plugins.PluginLoader;

@RunWith(HierarchicalContextRunner.class)
public class DefaultFeedExecutorTest {

    private static File FEED_FILE;
    private static File LOG_FILE;

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutorFactory factory;
    private MemoryFeed feed;

    private FeedExecutor executor;

    private InterpreterListenerSpy interpreterListenerSpy;
    private FeedExecutorListenerSpy feedExecutorListenerSpy;
    private PublisherListenerSpy publisherListenerSpy;
    private ChainModifierListenerSpy chainModifierListenerSpy;

    private LinkSpy linkBeforeBus;
    private LinkSpy linkAfterBus;

    @BeforeClass
    public static void setupClass() {
        FEED_FILE = new File("feed.xxx");
        LOG_FILE = new File("feed-log-1.xml");
    }

    @AfterClass
    public static void teardownClass() {
        FEED_FILE = LOG_FILE = null;
    }

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        feed = new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument"));

        interpreterListenerSpy = new InterpreterListenerSpy();
        feedExecutorListenerSpy = new FeedExecutorListenerSpy();
        publisherListenerSpy = new PublisherListenerSpy();
        chainModifierListenerSpy = new ChainModifierListenerSpy();

        linkBeforeBus = new LinkSpy();
        linkAfterBus = new LinkSpy();
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();

        feed = null;
        factory = null;
        executor = null;

        interpreterListenerSpy = null;
        feedExecutorListenerSpy = null;
        publisherListenerSpy = null;
        chainModifierListenerSpy = null;

        linkBeforeBus = linkAfterBus = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestFileFeed {

        @Test
        public void execute_MissingFile() {
            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getMissingFiles());
        }

        @Test
        public void execute_NotSupported() {
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getFilesNotSupported());
        }

        @Test
        public void execute_CorruptFileSystem_Unreadable() {
            PluginLoader.getInstance().injectPlugin(new SupportAllFilesFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(new CorruptFileSystem(fs), output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getUnreadableFiles());
        }

        @Test
        public void execute_IOExceptionThrowingFeedPlugin_Unreadable() {
            PluginLoader.getInstance().injectPlugin(new IOExceptionThrowingFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getUnreadableFiles());
        }

        @Test
        public void execute_RuntimThrowingFeedPlugin_ExceptionReported() {
            PluginLoader.getInstance().injectPlugin(new RuntimeExceptionThrowingFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertEquals(1, output.getThrownExceptions().length);
        }

        @Test
        public void execute_CorruptFeed_ExceptionReported() {
            PluginLoader.getInstance().injectPlugin(new SupportAllFilesFeedPlugin().setFeed(new ExceptionThrowingFeed()));
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertEquals(1, output.getThrownExceptions().length);
        }

        @Test
        public void execute_UnwritableFileSystem_ExceptionReported() {
            PluginLoader.getInstance().injectPlugin(new SupportAllFilesFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(new ReadOnlyFileSystem(fs), output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{LOG_FILE.toString()}, output.getUnwritableFiles());
            assertFalse(fs.exists(LOG_FILE));
        }

        @Test
        public void execute_NormalOperator_LogFileCreated() {
            PluginLoader.getInstance().injectPlugin(new SupportAllFilesFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertFalse(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[0], output.getMissingFiles());
            assertArrayEquals(new String[0], output.getFilesNotSupported());
            assertArrayEquals(new String[0], output.getUnreadableFiles());
            assertArrayEquals(new String[0], output.getUnwritableFiles());
            assertEquals(0, output.getThrownExceptions().length);
            assertTrue(fs.exists(LOG_FILE));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestListeners {

        @Before
        public void setup() {
            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
        }

        @Test
        public void execute_InterpreterListener() {
            factory.setInterpreterListener(() -> interpreterListenerSpy);
            executor = factory.create();
            executor.execute(feed);

            assertEquals(1, interpreterListenerSpy.countOpeningFeed());
            assertEquals(1, interpreterListenerSpy.countClosingFeed());
            assertEquals(2, interpreterListenerSpy.countStartingEvents());
            assertEquals(1, interpreterListenerSpy.countExceptions());
            assertEquals(2, interpreterListenerSpy.countEndingEvents());
            assertEquals(0, interpreterListenerSpy.countLinks());
            assertFalse(fs.exists(LOG_FILE));
        }

        @Test
        public void execute_FeedExecutorListener() {
            factory.setFeedExecutorListener(() -> feedExecutorListenerSpy);
            executor = factory.create();
            executor.execute(feed);

            assertEquals(1, feedExecutorListenerSpy.countOpeningFeed());
            assertEquals(1, feedExecutorListenerSpy.countClosingFeed());
            assertEquals(2, feedExecutorListenerSpy.countStartEvents());
            assertEquals(1, feedExecutorListenerSpy.countExceptions());
            assertEquals(2, feedExecutorListenerSpy.countEndEvents());
            assertFalse(fs.exists(LOG_FILE));
        }

        @Test
        public void execute_PublisherListener() {
            factory.setPublisherListener(() -> publisherListenerSpy);
            executor = factory.create();
            executor.execute(feed);

            assertEquals(2, publisherListenerSpy.countStartEvents());
            assertEquals(1, publisherListenerSpy.countExceptions());
            assertEquals(2, publisherListenerSpy.countEndEvents());
            assertFalse(fs.exists(LOG_FILE));
        }

        @Test
        public void execute_ChainModifierListener() {
            factory.setChainModifierListener(() -> interpreterListenerSpy);
            executor = factory.create();
            executor.execute(feed);

            assertEquals(0, chainModifierListenerSpy.countLinks());
            assertFalse(fs.exists(LOG_FILE));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestWithLinks {

        @Before
        public void setup() {
            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            factory.setInterpreterListener(() -> interpreterListenerSpy);
            factory.addLink(() -> linkBeforeBus);
            factory.addLinkAfterEventBus(() -> linkAfterBus);
            executor = factory.create();
        }

        @Test
        public void test() {
            executor.execute(feed);

            assertEquals(1, interpreterListenerSpy.countOpeningFeed());
            assertEquals(1, interpreterListenerSpy.countClosingFeed());
            assertEquals(2, interpreterListenerSpy.countStartingEvents());
            assertEquals(1, interpreterListenerSpy.countExceptions());
            assertEquals(2, interpreterListenerSpy.countEndingEvents());
            assertEquals(2, interpreterListenerSpy.countLinks());

            assertEquals(2, linkBeforeBus.size());
            assertEquals(1, linkAfterBus.size());
            assertFalse(fs.exists(LOG_FILE));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestWithSubscriber {

        private SubscriberSpy subscriberSpy;

        @Before
        public void setup() {
            factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
            subscriberSpy = new SubscriberSpy();
            EventConvertorLink l = new EventConvertorLink(new TestInvokerEvent());

            factory.setInterpreterListener(() -> interpreterListenerSpy);
            factory.addSubscriber(SubscriberFactory.getDefaultInstance(Event.class, null, subscriberSpy));
            factory.addLinkAfterEventBus(() -> linkAfterBus);
            factory.addLink(() -> l);
            factory.addLink(() -> linkBeforeBus);

            executor = factory.create();
        }

        @Test
        public void test() {
            executor.execute(feed);

            assertEquals(1, interpreterListenerSpy.countOpeningFeed());
            assertEquals(1, interpreterListenerSpy.countClosingFeed());
            assertEquals(2, interpreterListenerSpy.countStartingEvents());
            assertEquals(0, interpreterListenerSpy.countExceptions());
            assertEquals(2, interpreterListenerSpy.countEndingEvents());
            assertEquals(3, interpreterListenerSpy.countLinks());

            assertEquals(1, subscriberSpy.getCount());
            assertEquals(2, linkBeforeBus.size());
            assertEquals(1, linkAfterBus.size());
            assertTrue(fs.exists(LOG_FILE));
        }
    }
}
