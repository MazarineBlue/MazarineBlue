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
package org.mazarineblue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static java.lang.System.lineSeparator;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mazarineblue.Main.main;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.Runner;
import org.mazarineblue.plugins.RunnerPlugin;
import org.mazarineblue.plugins.util.RunnerSpy;
import org.mazarineblue.utilities.exceptions.NeverThrownException;
import org.mazarineblue.utilities.util.TestUtilityClass;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MainTest
        extends TestUtilityClass {

    private PrintStream backup;
    private ByteArrayOutputStream output;

    public MainTest() {
        super(Main.class);
    }

    @Before
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void setup() {
        backup = System.err;
        output = new ByteArrayOutputStream();
        System.setErr(new PrintStream(output));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        System.setOut(backup);
        backup = null;
        output = null;
    }

    @Test
    public void main_NoArgumens_NoAdditionPlugins() {
        main();
        String expected = "usage: <command> [<args>]" + lineSeparator()
                + "Commands:" + lineSeparator()
                + "help   prints help information" + lineSeparator()
                + lineSeparator();
        assertEquals(expected, output.toString());
    }

    @Test
    public void main_ShowHelp_AdditionalPlugin_WithSmallDescription() {
        PluginLoader.getInstance().injectPlugin(new RunnerPlugin() {
            @Override
            public String name() {
                return "lorem ipsum";
            }

            @Override
            public String description() {
                return "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            }

            @Override
            public Runner createRunner() {
                throw new NeverThrownException();
            }
        });
        main("my plugin");
        String expected = "usage: <command> [<args>]" + lineSeparator()
                + "Commands:" + lineSeparator()
                + "help          prints help information" + lineSeparator()
                + "lorem ipsum   Lorem ipsum dolor sit amet, consectetur adipiscing elit." + lineSeparator()
                + lineSeparator();
        assertEquals(expected, output.toString());
    }

    @Test
    public void main_ShowHelp_AdditionalPlugin_WithLongDescription() {
        PluginLoader.getInstance().injectPlugin(new RunnerPlugin() {
            @Override
            public String name() {
                return "lorem ipsum";
            }

            @Override
            public String description() {
                return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                        + "Nulla nisl justo, venenatis a eros eu, efficitur eleifend nisi.";
            }

            @Override
            public Runner createRunner() {
                throw new NeverThrownException();
            }
        });
        main("help");
        String expected = "usage: <command> [<args>]" + lineSeparator()
                + "Commands:" + lineSeparator()
                + "help          prints help information" + lineSeparator()
                + "lorem ipsum   Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ..." + lineSeparator()
                + lineSeparator();
        assertEquals(expected, output.toString());
    }

    @Test
    public void main_CreateRunner_AdditionalPlugin_WithSmallDescription() {
        RunnerSpy runnerSpy = new RunnerSpy();
        PluginLoader.getInstance().injectPlugin(new RunnerPlugin() {
            @Override
            public String name() {
                return "lorem ipsum";
            }

            @Override
            public String description() {
                throw new NeverThrownException();
            }

            @Override
            public Runner createRunner() {
                return runnerSpy;
            }
        });
        main("lorem ipsum", "Argument 1", "Argument 2");
        assertArrayEquals(new String[]{"Argument 1", "Argument 2"}, runnerSpy.getArguments());
        assertTrue(runnerSpy.isStarted());
    }
}
