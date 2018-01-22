/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.awt.EventQueue.invokeLater;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.Runner;
import org.mazarineblue.runners.swingrunner.config.Config;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrame;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrameBuilder;
import org.mazarineblue.runners.swingrunner.util.LoggerExceptionReporter;

/**
 * {@code StandaloneGUI} is an application that starts a graphical user
 * interface allowing the user to select and execute a {@code Feed}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SwingRunner
        implements Runner {

    private final DiskFileSelector diskSelector;
    private final MainFrame mainFrame;
    private final Config config;

    public SwingRunner() {
        setNimbusLookAndFeel();
        setDefaultUncaughtExceptionHandler(new SwingRunnerUncaughtExceptionHandler("Error", null,
                                                                                   new LoggerExceptionReporter()));

        FileSystem fs = new DiskFileSystem();
        config = new Config(fs);

        diskSelector = new DiskFileSelector(config.getMostRecentDirectory(), new FeedFilter(fs));
        mainFrame = new MainFrame(getBuilder(fs, config, diskSelector));
    }

    @Override
    public void setArguments(String... args) {
        // A SwingRunner does not do antything with arguments.
    }

    @Override
    public void start() {
        invokeLater(() -> getMainFrame().setVisible(true));
    }

    private static void setNimbusLookAndFeel() {
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Use the default, if an exception is thrown.
        }
    }

    private static MainFrameBuilder getBuilder(FileSystem fs, Config config, FileSelector selector) {
        MainFrameBuilder builder = new MainFrameBuilder();
        builder.setFileSystem(fs);
        builder.setConfig(config);
        builder.setFeedExecutorFactory(ExecutorFactory.newInstance(new ExecutorBuilder().setFileSystem(fs)));
        builder.setFileSelector(selector);
        builder.setExceptionHandler(new DummyExceptionHandler());
        return builder;
    }

    public DiskFileSelector getDiskSelector() {
        return diskSelector;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    void setReadOnly() {
        config.setReadOnly();
    }
}
