/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.util.LibraryPluginStub;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class ConvertStringToDurationTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private Executor executor;    

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        executor = TestFeedExecutorFactory.newInstance(fs, output).create();
        executor.execute(new MemoryFeed(new SetFileSystemEvent(fs)));
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("foo", new ConvertLibrary()));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        fs = null;
        output = null;
        executor = null;
    }

    @Test(expected = ArgumentsAreIncompatibleException.class)
    public void duration_Number() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Duration", " 1 "),
                                        e));
        output.throwFirstException();
    }

    @Test(expected = ArgumentsAreIncompatibleException.class)
    public void duration_TimeUnit() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Duration", " d "),
                                        e));
        output.throwFirstException();
    }

    @Test
    public void duration_Number_TimeUnit() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Duration", " 1d "),
                                        e));
        output.throwFirstException();
    }

    @SuppressWarnings("PublicInnerClass")
    public static class ConvertLibrary
            extends Library {

        public ConvertLibrary() {
            super("foo");
        }

        @Keyword("Duration")
        public Duration duration(Duration duration) {
            return duration;
        }
    }
}
