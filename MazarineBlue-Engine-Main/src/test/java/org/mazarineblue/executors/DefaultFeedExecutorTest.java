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
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.subscribers.EventConvertorSubscriber;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventdriven.util.feeds.TestFeeds;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.subscribers.DummySubscriber;
import org.mazarineblue.eventnotifier.util.SubscriberSpy;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.RuntimeExceptionThrowingFeedPlugin;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.fs.util.CorruptFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;
import org.mazarineblue.utilities.util.TestException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class DefaultFeedExecutorTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private ExecutorFactory factory;

    private Executor executor;

    private SubscriberSpy<Event> linkBeforeBus;
    private SubscriberSpy<Event> linkAfterBus;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        linkBeforeBus = new SubscriberSpy<>(e -> true);
        linkAfterBus = new SubscriberSpy<>(e -> true);
    }

    @After
    @SuppressWarnings("NestedAssignment")
    public void teardown() {
        PluginLoader.getInstance().reload();
        fs = null;
        output = null;
        factory = null;
        executor = null;
        linkBeforeBus = linkAfterBus = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class GiveFileSystem {

        private File FEED_FILE;

        @Before
        public void setup()
                throws IOException {
            fs.mkdir(new File("feed.xxx"));
            FEED_FILE = new File("feed.xxx");
        }

        @Test
        public void execute_MissingFile() {
            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
            executor.execute(new File("bar.txt"), "Main");

            output.throwFirstException();
            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{"bar.txt"}, output.getMissingFiles());
        }

        @Test
        public void execute_NotSupported() {
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            output.throwFirstException();
            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getFilesNotSupported());
        }

        @Test
        public void execute_CorruptFileSystem_UnreadableFile() {
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("Main"));
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.newInstance(new CorruptFileSystem(fs), output);
            executor = factory.create();
            executor.execute(FEED_FILE);

            output.throwFirstException();
            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getUnreadableFiles());
        }

        @Test(expected = TestException.class)
        public void execute_ExceptionThrownWhenCreatingFeed_UnreadableFile() {
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("Main", () -> {
                          throw new TestException();
                      }));
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            output.throwFirstException();
        }

        @Test
        public void execute_RuntimThrowingFeedPlugin_ExceptionReported() {
            PluginLoader.getInstance().injectPlugin(new RuntimeExceptionThrowingFeedPlugin());
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertEquals(1, output.getThrownExceptions().length);
        }

        @Test
        public void execute_CorruptFeed_ExceptionReported() {
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("Main", () -> TestFeeds.createExceptionThrowingFeed()));
            fs.mkfile(FEED_FILE, "");

            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
            executor.execute(FEED_FILE, "Main");

            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{FEED_FILE.toString()}, output.getProcessingFiles());
            assertEquals(1, output.getThrownExceptions().length);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GiveUnwritableFileSystem {

        @Before
        public void setup()
                throws IOException {
            fs.mkfile(new File("feed.xxx"));
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("Main"));
            factory = TestFeedExecutorFactory.newInstance(new ReadOnlyFileSystem(fs), output);
            executor = factory.create();
        }

        @Test
        public void execute_UnwritableFileSystem_ExceptionReported() {
            executor.execute(new File("feed.xxx"));

            output.throwFirstException();
            assertTrue(executor.containsErrors());
            assertArrayEquals(new String[]{"feed.xxx"}, output.getProcessingFiles());
            assertArrayEquals(new String[]{"feed-log-1.xml"}, output.getUnwritableFiles());
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }
    }

    public class GivenWritableFileSystem {

        @Before
        public void setup()
                throws IOException {
            fs.mkdir(new File("feed.xxx"));
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("Main"));
            fs.mkfile(new File("feed.xxx"), "never read, we use the plugin stub");

            factory = TestFeedExecutorFactory.newInstance(fs, output);
            executor = factory.create();
        }

        @Test
        public void execute_NormalOperationWithoutSheet_LogFileCreated() {
            executor.execute(new File("feed.xxx"));
            output.throwFirstException();
            assertFalse(executor.containsErrors());
            assertArrayEquals(new String[]{"feed.xxx"}, output.getProcessingFiles());
            assertArrayEquals(new String[0], output.getMissingFiles());
            assertArrayEquals(new String[0], output.getFilesNotSupported());
            assertArrayEquals(new String[0], output.getUnreadableFiles());
            assertArrayEquals(new String[0], output.getUnwritableFiles());
            assertEquals(0, output.getThrownExceptions().length);
            assertTrue(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_NormalOperationWithSheet_LogFileCreated() {
            executor.execute(new File("feed.xxx"), "Main");
            output.throwFirstException();
            assertFalse(executor.containsErrors());
            assertArrayEquals(new String[]{"feed.xxx"}, output.getProcessingFiles());
            assertArrayEquals(new String[0], output.getMissingFiles());
            assertArrayEquals(new String[0], output.getFilesNotSupported());
            assertArrayEquals(new String[0], output.getUnreadableFiles());
            assertArrayEquals(new String[0], output.getUnwritableFiles());
            assertEquals(0, output.getThrownExceptions().length);
            assertTrue(fs.exists(new File("feed-log-1.xml")));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestListeners {

        @Before
        public void setup() {
            factory = TestFeedExecutorFactory.newInstance(fs, output);
        }

        @Test
        public void execute_FeedExecutorListener_UsingFactory() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            factory.setFeedExecutorListener(() -> spy);
            executor = factory.create();
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_FeedExecutorListener_UsingExecutor() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            executor = factory.create();
            executor.setFeedExecutorListener(spy);
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_PublisherListener_UsingFactory() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            factory.setPublisherListener(() -> spy);
            executor = factory.create();
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            spy.startEvents().assertClasses(ExecuteInstructionLineEvent.class);
            spy.exceptions().assertClasses(InstructionNotFoundException.class);
            spy.endEvents().assertClasses(ExecuteInstructionLineEvent.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_PublisherListener_UsingExecutor() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            executor = factory.create();
            executor.setPublisherListener(spy);
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            spy.startEvents().assertClasses(ExecuteInstructionLineEvent.class);
            spy.exceptions().assertClasses(InstructionNotFoundException.class);
            spy.endEvents().assertClasses(ExecuteInstructionLineEvent.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_ChainModifierListener_UsingFactory() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            factory.setChainModifierListener(() -> spy);
            executor = factory.create();
            executor.addLink(new DummySubscriber<>());
            spy.links().assertClasses(DummySubscriber.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }

        @Test
        public void execute_ChainModifierListener_UsingExecutor() {
            ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            factory.setChainModifierListener(() -> spy);
            executor = factory.create();
            executor.setChainModifierListener(spy);
            executor.addLink(new DummySubscriber<>());
            spy.links().assertClasses(DummySubscriber.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }
    }

    @Ignore
    @SuppressWarnings("PublicInnerClass")
    public class TestWithLinks {

        private ProcessorListenerSpy processorListenerSpy;

        @Before
        public void setup() {
            processorListenerSpy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
            factory = TestFeedExecutorFactory.newInstance(fs, output);
            factory.setChainModifierListener(() -> processorListenerSpy);
            factory.setFeedExecutorListener(() -> processorListenerSpy);
            factory.setPublisherListener(() -> processorListenerSpy);
            factory.addLink(() -> linkBeforeBus);
            factory.addLinkAfterEventNotifier(() -> linkAfterBus);
            executor = factory.create();
        }

        @Test
        public void test() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            processorListenerSpy.links().assertClasses(DummySubscriber.class, DummySubscriber.class, DummySubscriber.class);
            processorListenerSpy.openedFeeds().assertClasses(MemoryFeed.class);
            processorListenerSpy.closedFeeds().assertClasses(MemoryFeed.class);
            processorListenerSpy.startEvents().assertClasses(ExecuteInstructionLineEvent.class);
            processorListenerSpy.exceptions().assertClasses(InstructionNotFoundException.class);
            processorListenerSpy.endEvents().assertClasses(ExecuteInstructionLineEvent.class);
            linkBeforeBus.assertClasses(TestEvent.class, TestEvent.class);
            linkAfterBus.assertClasses(TestEvent.class);
            assertFalse(fs.exists(new File("feed-log-1.xml")));
        }
    }

    @Ignore
    @SuppressWarnings("PublicInnerClass")
    public class TestWithSubscriber {

        private ProcessorListenerSpy processorListenerSpy;
        private SubscriberSpy subscriberSpy;

        @Before
        public void setup() {
            processorListenerSpy = new ProcessorListenerSpy();
            factory = TestFeedExecutorFactory.newInstance(fs, output);
            subscriberSpy = new SubscriberSpy(t -> true);
            EventConvertorSubscriber l = new EventConvertorSubscriber(new TestInvokerEvent());

            factory.setChainModifierListener(() -> processorListenerSpy);
            factory.setFeedExecutorListener(() -> processorListenerSpy);
            factory.setPublisherListener(() -> processorListenerSpy);
            factory.addLinkAfterVariableParser(() -> subscriberSpy);
            factory.addLinkAfterEventNotifier(() -> linkAfterBus);
            factory.addLink(() -> l);
            factory.addLink(() -> linkBeforeBus);

            executor = factory.create();
        }

        @After
        public void teardown() {
            processorListenerSpy = null;
            subscriberSpy = null;
        }

        @Test
        public void test() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Keyword", "Argument")));
            processorListenerSpy.links().assertClasses(DummySubscriber.class, DummySubscriber.class,
                                                         DummySubscriber.class, DummySubscriber.class);
            processorListenerSpy.openedFeeds().assertClasses(MemoryFeed.class);
            processorListenerSpy.closedFeeds().assertClasses(MemoryFeed.class);
            processorListenerSpy.startEvents().assertClasses(ExecuteInstructionLineEvent.class,
                                                             ExecuteInstructionLineEvent.class,
                                                             ExecuteInstructionLineEvent.class);
            processorListenerSpy.exceptions().assertClasses();
            processorListenerSpy.endEvents().assertClasses(ExecuteInstructionLineEvent.class,
                                                             ExecuteInstructionLineEvent.class,
                                                             ExecuteInstructionLineEvent.class);
            assertEquals(2, subscriberSpy.getCount());
            linkBeforeBus.assertClasses(TestEvent.class, TestEvent.class, TestEvent.class);
            linkAfterBus.assertClasses(TestEvent.class, TestEvent.class);
            assertTrue(fs.exists(new File("feed-log-1.xml")));
        }
    }
}
