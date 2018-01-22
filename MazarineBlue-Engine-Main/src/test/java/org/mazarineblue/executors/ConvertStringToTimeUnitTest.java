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

import java.util.concurrent.TimeUnit;
import org.junit.After;
import static org.junit.Assert.assertEquals;
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

public class ConvertStringToTimeUnitTest {

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
    public void timeUnit_NotTimeUnit() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "xxx"),
                                        e));
        assertEquals(null, e.getValue());
        output.throwFirstException();
    }

    /*
     * default:
     * throw new IllegalArgumentException(format("Unsupported time unit: %s", timeUnit));
     *
     */
    @Test
    public void timeUnit_DaysInCapitals() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "DAYS"),
                                        e));
        assertEquals(TimeUnit.DAYS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_D() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "d"),
                                        e));
        assertEquals(TimeUnit.DAYS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Hours() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "hours"),
                                        e));
        assertEquals(TimeUnit.HOURS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_H() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "h"),
                                        e));
        assertEquals(TimeUnit.HOURS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Minutes() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "minutes"),
                                        e));
        assertEquals(TimeUnit.MINUTES, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Min() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "min"),
                                        e));
        assertEquals(TimeUnit.MINUTES, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_M() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "m"),
                                        e));
        assertEquals(TimeUnit.MINUTES, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Seconds() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "seconds"),
                                        e));
        assertEquals(TimeUnit.SECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Sec() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "sec"),
                                        e));
        assertEquals(TimeUnit.SECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_S() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "s"),
                                        e));
        assertEquals(TimeUnit.SECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Milliseconds() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "milliseconds"),
                                        e));
        assertEquals(TimeUnit.MILLISECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Millis() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "millis"),
                                        e));
        assertEquals(TimeUnit.MILLISECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Ms() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "ms"),
                                        e));
        assertEquals(TimeUnit.MILLISECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Microseconds() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "microseconds"),
                                        e));
        assertEquals(TimeUnit.MICROSECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Micros() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "micros"),
                                        e));
        assertEquals(TimeUnit.MICROSECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_µs() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "µs"),
                                        e));
        assertEquals(TimeUnit.MICROSECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Nanoseconds() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "nanoseconds"),
                                        e));
        assertEquals(TimeUnit.NANOSECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Nanos() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "nanos"),
                                        e));
        assertEquals(TimeUnit.NANOSECONDS, e.getValue());
        output.throwFirstException();
    }

    @Test
    public void timeUnit_Ns() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "=Time unit", "ns"),
                                        e));
        assertEquals(TimeUnit.NANOSECONDS, e.getValue());
        output.throwFirstException();
    }

    @SuppressWarnings("PublicInnerClass")
    public static class ConvertLibrary
            extends Library {

        public ConvertLibrary() {
            super("foo");
        }

        @Keyword("Time unit")
        public TimeUnit timeUnit(TimeUnit input) {
            return input;
        }
    }
}
