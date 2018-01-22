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
package org.mazarineblue.runners.swingrunner;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.plugins.RunnerPlugin;
import org.mazarineblue.runners.swingrunner.util.MainFrameUtil;
import org.mazarineblue.utilities.swing.JFileChooserUtil;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SwingRunnerPluginTest {

    private RunnerPlugin plugin;

    @Before
    public void setup() {
        plugin = new SwingRunnerPlugin();
    }

    @After
    public void teardown() {
        plugin = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestMetaData {

        @Test
        public void testName() {
            assertEquals("swing", plugin.name());
        }

        @Test
        public void testDescription() {
            assertEquals("starts a swing user interface", plugin.description());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SwingRunnerTest {

        private JFileChooserUtil fileChooser;
        private MainFrameUtil frame;

        @Before
        public void setup() {
            SwingRunner runner = (SwingRunner) plugin.createRunner();
            runner.setReadOnly();
            fileChooser = new JFileChooserUtil(runner.getDiskSelector().getFileChooser());
            frame = new MainFrameUtil(runner.getMainFrame());
            frame.setFileChooser(fileChooser);
        }

        @After
        public void teardown() {
            frame.dispose();
            frame = null;
            fileChooser = null;
        }

        @Test
        public void test()
                throws InterruptedException {
            frame.setVisible();
            frame.doClickSelectFileButton();
            String filename = "../src/test/resources/feed.txt";
            fileChooser.setFilename(filename);
            File expected = new File(filename).getAbsoluteFile();
            File actual = fileChooser.doClickOpenButtonAndWait(() -> frame.getSelectedFile()).getAbsoluteFile();
            assertEquals(expected, actual);
            assertNotNull(frame.getSelectedSheet());
        }
    }
}
