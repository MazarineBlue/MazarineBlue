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
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.exceptions.UnbalancedScopeException;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.DummyLibrary;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.SheetNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;
import org.mazarineblue.plugins.util.RuntimeExceptionThrowingLibraryPlugin;
import org.mazarineblue.plugins.util.TestLibraryPlugin;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class BuiltinLibraryTest {

    private static final File FILE = new File("foo.txt");

    private LinkSpy link;
    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutor executor;
    private MemoryFeed initialFeed, nestedFeed;
    private List<Event> expectedEvents;

    @Before
    public void setup() {
        link = new LinkSpy();
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        executor = TestFeedExecutorFactory.newInstance(fs, output).create();
        executor.addLinkAfterEventBus(new ConsumeEventsLink(event -> {
            if (!event.getClass().isAssignableFrom(ExceptionThrownEvent.class))
                return false;
            ExceptionThrownEvent e = (ExceptionThrownEvent) event;
            if (!e.getCause().getClass().isAssignableFrom(ExecuteInstructionLineEvent.class))
                return false;
            ExecuteInstructionLineEvent cause = (ExecuteInstructionLineEvent) e.getCause();
            String keyword = cause.getKeyword();
            return keyword.equals("keyword");
        }));
        executor.addLink(link);
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        link = null;
        output = null;
        executor = null;
        initialFeed = nestedFeed = null;
        expectedEvents = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestImportLibrary {

        @Before
        public void setup() {
            initialFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"));
            expectedEvents = new ArrayList<>(initialFeed.getEvents());
        }

        @Test
        public void importLibrary_NoRegisterdPlugins_ThrowsException() {
            executor.execute(initialFeed);
            assertArrayEquals(new Class<?>[]{LibraryNotFoundException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void importLibrary_RegisterdPluginThrowsException_ThrowsException() {
            PluginLoader.getInstance().injectPlugin(new RuntimeExceptionThrowingLibraryPlugin());
            executor.execute(initialFeed);
            assertArrayEquals(new Class<?>[]{LibraryPluginException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void importLibrary_RegisterdPlugin_CausesAddLibraryEvent() {
            Library library = new DummyLibrary("foo");
            PluginLoader.getInstance().injectPlugin(new TestLibraryPlugin("foo", library));

            executor.execute(initialFeed);
            expectedEvents.add(new AddLibraryEvent(library));

            output.throwFirstException();
            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestImportFeed {

        @SuppressWarnings("PublicInnerClass")
        public class WithoutSheet {

            @Before
            public void setup() {
                initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                             new ExecuteInstructionLineEvent("Import feed", FILE.toString()));
                expectedEvents = new ArrayList<>(initialFeed.getEvents());
            }

            @Test
            public void importFeed_FileDoesNotExists_ThrowsException() {
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new FileNotFoundException(FILE.toString())));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{FileNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void importFeed_FeedDoesExists_OK() {
                fs.mkfile(FILE, "xx");
                nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));

                executor.execute(initialFeed);
                expectedEvents.addAll(nestedFeed.getEvents());

                output.throwFirstException();
                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            }
        }

        @SuppressWarnings("PublicInnerClass")
        public class WithSheet {

            @Before
            public void setup() {
                initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                             new ExecuteInstructionLineEvent("Import feed", FILE.toString(), "foo"));
                expectedEvents = new ArrayList<>(initialFeed.getEvents());
                nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
            }

            @Test
            public void importFeed_FileDoesNotExists_ThrowsException() {
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new FileNotFoundException(FILE.toString())));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{FileNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void importFeed_SheetDoesNotExists_ThrowsException() {
                fs.mkfile(FILE, "xx");
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("oof", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new SheetNotFoundException(FILE, "foo")));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{SheetNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void importFeed_FeedDoesExists_OK() {
                fs.mkfile(FILE, "xx");
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.addAll(nestedFeed.getEvents());

                output.throwFirstException();
                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            }
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestCallFeed {

        public class WithoutSheet {

            @Before
            public void setup() {
                initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                             new ExecuteInstructionLineEvent("Call feed", FILE.toString()));
                expectedEvents = new ArrayList<>(initialFeed.getEvents());
            }

            @Test
            public void callFeed_FileDoesNotExists_ThrowsException() {
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new FileNotFoundException(FILE.toString())));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{FileNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void callFeed_FeedDoesExists_OK() {
                fs.mkfile(FILE, "xx");
                nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));

                executor.execute(initialFeed);
                expectedEvents.add(new StartVariableScopeEvent());
                expectedEvents.addAll(nestedFeed.getEvents());
                expectedEvents.add(new EndVariableScopeEvent());

                output.throwFirstException();
                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            }
        }

        public class WithSheet {

            @Before
            public void setup() {
                initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                             new ExecuteInstructionLineEvent("Call feed", FILE.toString(), "foo"));
                expectedEvents = new ArrayList<>(initialFeed.getEvents());
                nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
            }

            @Test
            public void callFeed_FileDoesNotExists_ThrowsException() {
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new FileNotFoundException(FILE.toString())));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{FileNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void callFeed_SheetDoesNotExists_ThrowsException() {
                fs.mkfile(FILE, "xx");
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("oof", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.add(createExceptionThrownEvent(new SheetNotFoundException(FILE, "foo")));

                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
                assertArrayEquals(new Class<?>[]{SheetNotFoundException.class}, output.getThrownExceptionClasses());
            }

            @Test
            public void callFeed_FeedDoesExists_OK() {
                fs.mkfile(FILE, "xx");
                PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
                executor.execute(initialFeed);
                expectedEvents.add(new StartVariableScopeEvent());
                expectedEvents.addAll(nestedFeed.getEvents());
                expectedEvents.add(new EndVariableScopeEvent());

                output.throwFirstException();
                assertEquals(expectedEvents.size(), link.size());
                expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            }
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestImportSheet {

        @Before
        public void setup() {
            initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                         new ExecuteInstructionLineEvent("Import sheet", "foo"));
            nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
            expectedEvents = new ArrayList<>(initialFeed.getEvents());
        }

        @Test
        public void importSheet_SheetDoesNotExists_ThrowException() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
            executor.execute(initialFeed);
            expectedEvents.add(createExceptionThrownEvent(new UnbalancedScopeException("foo")));

            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            assertArrayEquals(new Class<?>[]{UnbalancedScopeException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void importSheet_FileIsNotOpendFirst_ThrowsException() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
            executor.execute(initialFeed);
            expectedEvents.add(createExceptionThrownEvent(new UnbalancedScopeException("foo")));

            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            assertArrayEquals(new Class<?>[]{UnbalancedScopeException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void importSheet_FileIsOpendFirst_ImportsSheet() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("main", initialFeed)
                    .addSheet("foo", nestedFeed));
            executor.execute(FILE);
            expectedEvents.addAll(nestedFeed.getEvents());

            output.throwFirstException();
            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestCallSheet {

        @Before
        public void setup() {
            initialFeed = new MemoryFeed(new SetFileSystemEvent(fs),
                                         new ExecuteInstructionLineEvent("Call sheet", "foo"));
            nestedFeed = new MemoryFeed(new ExecuteInstructionLineEvent("Comment", "Argument"));
            expectedEvents = new ArrayList<>(initialFeed.getEvents());
        }

        @Test
        public void callSheet_SheetDoesNotExists_ThrowException() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("foo", nestedFeed));
            executor.execute(initialFeed);
            expectedEvents.add(createExceptionThrownEvent(new UnbalancedScopeException("foo")));

            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            assertArrayEquals(new Class<?>[]{UnbalancedScopeException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void callSheet_FileIsNotOpendFirst_ThrowsException() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("foo", nestedFeed));
            executor.execute(initialFeed);
            expectedEvents.add(createExceptionThrownEvent(new UnbalancedScopeException("foo")));

            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
            assertArrayEquals(new Class<?>[]{UnbalancedScopeException.class}, output.getThrownExceptionClasses());
        }

        @Test
        public void callSheet_FileIsOpendFirst_ImportsSheet() {
            fs.mkfile(FILE, "xx");
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin()
                    .addSheet("main", initialFeed)
                    .addSheet("foo", nestedFeed));
            executor.execute(FILE);
            expectedEvents.add(new StartVariableScopeEvent());
            expectedEvents.addAll(nestedFeed.getEvents());
            expectedEvents.add(new EndVariableScopeEvent());

            output.throwFirstException();
            assertEquals(expectedEvents.size(), link.size());
            expectedEvents.stream().forEach(e -> assertEquals(e, link.next()));
        }
    }

    private ExceptionThrownEvent createExceptionThrownEvent(RuntimeException ex) {
        return new ExceptionThrownEvent(expectedEvents.get(expectedEvents.size() - 1), ex);
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestCompareMethods
            extends TestHashCodeAndEquals<Library> {

        @Override
        protected Library getObject() {
            return new BuiltinLibrary();
        }

        @Override
        protected Library getDifferentObject() {
            return new DummyLibrary("foo");
        }
    }
}
