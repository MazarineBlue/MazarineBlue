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
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import static org.mazarineblue.eventnotifier.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.executors.util.AbstractExecutorFactoryTestHelper;
import org.mazarineblue.feeds.plaintext.PlainTextFeed;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.util.MemoryFeedPlugin;
import org.mazarineblue.runners.swingrunner.config.Config;
import static org.mazarineblue.runners.swingrunner.config.Config.maxRecentFiles;
import static org.mazarineblue.runners.swingrunner.config.Config.recentFilesLocation;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrame;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrameBuilder;
import org.mazarineblue.runners.swingrunner.util.MainFrameUtil;
import org.mazarineblue.runners.swingrunner.util.TestFileSelector;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class MainFrameTest
        extends AbstractExecutorFactoryTestHelper {

    private File freshFile;
    private File recentFile;
    private File fileNotAvailable;
    private File fileNotSupported;
    private File[] recentFiles;
    private Config config;
    private TestFileSelector selector;
    private MainFrameBuilder builder;
    private MainFrameUtil frame;

    private static File[] createInitializedFileArray(String prefix, String postfix) {
        File[] files = new File[maxRecentFiles()];
        for (int i = 0; i < files.length; ++i)
            files[i] = new File(prefix + i + postfix);
        return files;
    }

    @Before
    public void setup()
            throws IOException {
        freshFile = new File("feed.txt");
        fileNotAvailable = new File("fileNotAvailable.txt");
        fileNotSupported = new File("fileNotSupported.pdf");

        mkfile(freshFile, "| Keyword | Argument |");
        mkfile(fileNotSupported, "");

        config = new Config(this);
        config.setReadOnly();
        selector = new TestFileSelector(freshFile);

        builder = new MainFrameBuilder();
        builder.setFileSystem(this);
        builder.setConfig(config);
        builder.setFileSelector(selector);
        builder.setExceptionHandler(new DummyExceptionHandler());
        builder.setFeedExecutorFactory(this);
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        freshFile = recentFile = fileNotAvailable = fileNotSupported = null;
        recentFiles = null;
        config = null;
        selector = null;
        frame.dispose();
        frame = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestSelectFileButton {

        @Before
        public void setup()
                throws Exception {
            frame = new MainFrameUtil(new MainFrame(builder));
        }

        @Test
        public void selectFile_FileIsNull_FieldsAreUntouched() {
            selector.setFile(null);
            frame.doClickSelectFileButton();
            assertNull(frame.getSelectedFile());
            assertNull(frame.getSelectedSheet());
        }

        @Test(expected = FileNotFoundException.class)
        @Ignore("Test is not needed, but it helps to make the exception explicit")
        public void selectFile_FileNotFound_ThrowsException() {
            selector.setFile(fileNotAvailable);
            frame.doClickSelectFileButton();
            assertEquals(freshFile, frame.getSelectedFile());
            assertNotNull(frame.getSelectedSheet());
        }

        @Test(expected = FileNotSupportedException.class)
        @Ignore("Test is not needed, but it helps to make the exception explicit")
        public void selectFile_FileNotSupported() {
            selector.setFile(fileNotSupported);
            frame.doClickSelectFileButton();
            assertEquals(freshFile, frame.getSelectedFile());
            assertNotNull(frame.getSelectedSheet());
        }

        @Test
        public void selectFile_FileExists_FieldsAreModified() {
            frame.doClickSelectFileButton();
            assertEquals(freshFile, frame.getSelectedFile());
            assertNotNull(frame.getSelectedSheet());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestRecentFiles {

        @Test
        public void recentFiles_AllDeadFiles_FieldsAreEmpty()
                throws Exception {
            File[] recentFiles = createInitializedFileArray("file", ".xml");
            mkfile(recentFilesLocation(), (Object[]) recentFiles);
            frame = new MainFrameUtil(new MainFrame(builder));
            assertNull(frame.getSelectedFile());
            assertNull(frame.getSelectedSheet());
        }

        @Test
        public void recentFiles_NoDeadFiles_FieldsAreModified()
                throws Exception {
            File[] recentFiles = createInitializedFileArray("file", ".txt");
            mkfile(recentFilesLocation(), (Object[]) recentFiles);
            mkfile(recentFiles[0], "| Keyword | Argument |");
            frame = new MainFrameUtil(new MainFrame(builder));
            assertEquals(recentFiles[0], frame.getSelectedFile());
            assertNotNull(frame.getSelectedSheet());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestComboBoxes {

        @Before
        public void setup()
                throws Exception {
            PluginLoader.getInstance().injectPlugin(new MemoryFeedPlugin().addSheet("First", "Second"));
            recentFiles = createInitializedFileArray("file", ".txt");
            mkfile(recentFilesLocation(), (Object[]) recentFiles);
            mkfile(recentFiles[0], "| Keyword | Argument |");
            mkfile(recentFiles[1], "| Keyword | Argument |");
            frame = new MainFrameUtil(new MainFrame(builder));
        }

        @Test
        public void fileComboBox_Switch_FieldsAreModified()
                throws Exception {
            frame.setSelectedFile(frame.getFileOption(1));
            assertEquals(recentFiles[1], frame.getSelectedFile());
            assertEquals("First", frame.getSelectedSheet());
        }

        @Test
        public void sheetComboBox_Switch_FieldsAreModified()
                throws Exception {
            frame.setSelectedFile(frame.getFileOption(1));
            frame.setSelectedSheet(frame.getSheetOption(1));
            assertEquals(recentFiles[1], frame.getSelectedFile());
            assertEquals("Second", frame.getSelectedSheet());
        }
    }

    @Test
    public void executeButton_Switch_FeedWasExecuted()
            throws Exception {
        recentFiles = createInitializedFileArray("file", ".txt");
        mkfile(recentFilesLocation(), (Object[]) recentFiles);
        mkfile(recentFiles[0], "| Keyword | Argument |");

        ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesNoneAutoConsumable());
        setChainModifierListener(() -> spy);
        setFeedExecutorListener(() -> spy);
        setPublisherListener(() -> spy);
        addLink(() -> new ConsumeEventsSubscriber<>());

        frame = new MainFrameUtil(new MainFrame(builder));
        frame.setVisible();
        frame.doClickExecuteButton();

        spy.openedFeeds().assertClasses(PlainTextFeed.class);
        spy.closedFeeds().assertClasses(PlainTextFeed.class);
        spy.startEvents().assertClasses(ExecuteInstructionLineEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(ExecuteInstructionLineEvent.class);
    }
}
