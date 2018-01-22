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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.util.SubscriberSpy;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.exceptions.UnbalancedScopeException;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

@RunWith(HierarchicalContextRunner.class)
public class BuiltinFeedLibraryTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private Executor executor;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        fs.mkfile(new File("foo.txt"), "xx");
        output = new FeedExecutorOutputSpy();
        executor = TestFeedExecutorFactory.newInstance(fs, output).create();
        executor.execute(new MemoryFeed(new SetFileSystemEvent(fs)));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        fs = null;
        output = null;
        executor = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenFeedWithSheets {

        private SubscriberSpy<Event> spy;

        @Before
        public void setup() {
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("first", new MemoryFeed(new TestEvent()))
                    .addSheet("second", new MemoryFeed(new TestEvent(), new TestEvent())));
            spy = new SubscriberSpy<>(matchesAny(TestEvent.class, StartVariableScopeEvent.class, EndVariableScopeEvent.class));
            executor.addLink(spy);
        }

        @Test(expected = FileNotFoundException.class)
        public void importFeed_FeedNotFound() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import feed", "bar.txt")));
            output.throwFirstException();
        }

        @Test
        public void importFeed_FeedExists_SheetNotProvided() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import feed", "foo.txt")));
            output.throwFirstException();
            spy.assertClasses(TestEvent.class);
        }

        @Test(expected = SheetNotFoundException.class)
        public void importFeed_FeedExists_SheetNotFound() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import feed", "foo.txt", "foo")));
            output.throwFirstException();
        }

        @Test
        public void importFeed_FeedExistsAndHasASheet() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import feed", "foo.txt", "second")));
            output.throwFirstException();
            spy.assertClasses(TestEvent.class, TestEvent.class);
        }

        @Test(expected = FileNotFoundException.class)
        public void callFeed_FeedNotFound() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Call feed", "bar.txt")));
            output.throwFirstException();
        }

        @Test
        public void callFeed_FeedExists_SheetNotProvided() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Call feed", "foo.txt")));
            output.throwFirstException();
            spy.assertClasses(StartVariableScopeEvent.class, TestEvent.class, EndVariableScopeEvent.class);
        }

        @Test(expected = SheetNotFoundException.class)
        public void callFeed_FeedExists_SheetNotFound() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Call feed", "foo.txt", "foo")));
            output.throwFirstException();
        }

        @Test
        public void callFeed_FeedExists_SheetExists() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Call feed", "foo.txt", "second")));
            output.throwFirstException();
            spy.assertClasses(StartVariableScopeEvent.class, TestEvent.class, TestEvent.class, EndVariableScopeEvent.class);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenPluginWithSheets {

        private MemoryFeed mainFeed;
        private SubscriberSpy<Event> subscriber;

        @Before
        public void setup() {
            mainFeed = new MemoryFeed();
            fs.mkfile(new File("foo.txt"), "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("first", mainFeed)
                    .addSheet("second", new MemoryFeed(new TestEvent())));
            subscriber = new SubscriberSpy<>(matchesAny(TestEvent.class, StartVariableScopeEvent.class, EndVariableScopeEvent.class));
            executor.addLink(subscriber);
        }

        @Test(expected = UnbalancedScopeException.class)
        public void importSheet_FileNotOpend() {
            mainFeed.add(new ExecuteInstructionLineEvent("Import sheet", "foo"));
            executor.execute(mainFeed);
            output.throwFirstException();
        }

        @Test(expected = UnbalancedScopeException.class)
        public void importSheet_SheetNotFound() {
            mainFeed.add(new ExecuteInstructionLineEvent("Import sheet", "foo"));
            executor.execute(new File("foo.txt"));
            output.throwFirstException();
        }

        @Test
        public void importSheet_SheetExists() {
            mainFeed.add(new ExecuteInstructionLineEvent("Import sheet", "second"));
            executor.execute(new File("foo.txt"));
            output.throwFirstException();
            subscriber.assertClasses(TestEvent.class);
        }

        @Test(expected = UnbalancedScopeException.class)
        public void callSheet_FileNotOpend() {
            mainFeed.add(new ExecuteInstructionLineEvent("Call sheet", "foo"));
            executor.execute(mainFeed);
            output.throwFirstException();
        }

        @Test(expected = UnbalancedScopeException.class)
        public void callSheet_SheetNotFound() {
            mainFeed.add(new ExecuteInstructionLineEvent("Call sheet", "foo"));
            executor.execute(new File("foo.txt"));
            output.throwFirstException();
        }

        @Test
        public void callSheet_SheetExists() {
            mainFeed.add(new ExecuteInstructionLineEvent("Call sheet", "second"));
            executor.execute(new File("foo.txt"));
            output.throwFirstException();
            subscriber.assertClasses(StartVariableScopeEvent.class, TestEvent.class, EndVariableScopeEvent.class);
        }
    }
}
