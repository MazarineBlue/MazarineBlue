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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static java.util.Arrays.asList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.libraries.DummyLibrary;
import org.mazarineblue.parser.ExpressionBuilderParser;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.expressions.Expression;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;
import org.mazarineblue.plugins.util.ExceptionThrowingLibraryPluginStub;
import org.mazarineblue.plugins.util.LibraryPluginStub;
import org.mazarineblue.utilities.util.TestException;

@RunWith(HierarchicalContextRunner.class)
public class BuiltinLibraryTest {

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
    public class GivenLibraryPluginThatThrowsAnRuntimeException {

        @Before
        public void setup() {
            PluginLoader.getInstance().injectPlugin(new ExceptionThrowingLibraryPluginStub(() -> new TestException()));
        }

        @Test(expected = LibraryPluginException.class)
        public void importLibrary() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo")));
            output.throwFirstException();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenLibraryPluginWithLibrary {

        private Library library;

        @Before
        public void setup() {
            library = new DummyLibrary("foo");
            PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("foo", library));
        }

        @Test(expected = LibraryNotFoundException.class)
        public void importLibrary_UnkownLibrary() {
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "bar")));
            output.throwFirstException();
        }

        @Test
        public void importLibrary_KnownLibrary() {
            FetchLibrariesEvent e = new FetchLibrariesEvent(lib -> lib.namespace().equals("foo"));
            executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                            e));
            assertEquals(asList(library), e.getLibraries());
        }
    }

    @Test
    public void eventHandler_CallFileSystemMethodEvent_DeleteAll()
            throws NoSuchMethodException {
        assertTrue(fs.exists(new File("foo.txt")));
        Method method = FileSystem.class.getDeclaredMethod("deleteAll");
        CallFileSystemMethodEvent e = new CallFileSystemMethodEvent(method);
        executor.execute(new MemoryFeed(e));
        assertTrue(e.isConsumed());
        assertFalse(e.isExceptionThrown());
        assertFalse(fs.exists(new File("foo.txt")));
    }

    @Test
    public void eventHandler_CallFileSystemMethodEvent_Mkfile()
            throws NoSuchMethodException {
        fs.deleteAll();
        Method method = FileSystem.class.getDeclaredMethod("mkfile", File.class, String.class);
        CallFileSystemMethodEvent e = new CallFileSystemMethodEvent(method, new File("foo.txt"), "foo");
        executor.execute(new MemoryFeed(e));
        assertTrue(e.isConsumed());
        assertFalse(e.isExceptionThrown());
        assertTrue(fs.exists(new File("foo.txt")));
    }

    @Test(expected = InvocationTargetException.class)
    public void eventHandler_CallFileSystemMethodEvent_Mkfile_OnReadOnlyFileSystem()
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        fs.deleteAll();
        Method method = FileSystem.class.getDeclaredMethod("mkfile", File.class, String.class);
        CallFileSystemMethodEvent e = new CallFileSystemMethodEvent(method, new File("foo.txt"), "foo");
        executor.execute(new MemoryFeed(new SetFileSystemEvent(new ReadOnlyFileSystem(fs)),
                                        e));
        assertTrue(e.isConsumed());
        assertTrue(e.isExceptionThrown());
        assertFalse(fs.exists(new File("foo.txt")));
        e.throwException();
    }

    @Test
    public void evaluateExpressionEvent() {
        Parser<String, Expression> parser = new ExpressionBuilderParser();
        Expression expression = parser.parse("a + 2");
        EvaluateExpressionEvent e = new EvaluateExpressionEvent(expression);
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
        assertEquals(3L, e.getResult());
    }

    @Test(expected = ArgumentsAreIncompatibleException.class)
    public void evaluateExpression_IncorrectExpression() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Evaluate expression", "a +");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
    }

    @Test
    public void evaluateExpression_CorrectExpression() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Evaluate expression", "a + 2");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
        assertEquals(3L, e.getResult());
    }

    @Test
    public void expressionIsTrue_TrueExpression() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Expression is true", "a == 1");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
        assertEquals(true, e.getResult());
    }

    @Test
    public void expressionIsTrue_FalseExpression() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Expression is true", "a == 2");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
        assertEquals(false, e.getResult());
    }

    @Test
    public void expressionIsFalse_TrueExpression() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Expression is false", "a == 1");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "a", 1),
                                        e));
        output.throwFirstException();
        assertEquals(false, e.getResult());
    }
}
