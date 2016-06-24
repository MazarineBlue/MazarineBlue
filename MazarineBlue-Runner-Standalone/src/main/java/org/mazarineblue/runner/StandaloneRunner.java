/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.runner;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.mazarineblue.runner.swing.DocumentManager;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.mazarineblue.keyworddriven.feeds.FeedBuilderImpl;
import org.mazarineblue.runner.sheetFactorySelector.DefaultSheetFactorySelector;
import org.mazarineblue.runner.sheetFactorySelector.SheetFactorySelector;
import org.mazarineblue.runner.swing.FileDocumentManager;
import org.mazarineblue.runner.swing.MainFrame;
import org.mazarineblue.runner.tasks.Task;
import org.mazarineblue.runner.tasks.TaskTargetException;
import org.mazarineblue.runner.tasks.TestTask;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class StandaloneRunner {

    public static void main(String args[]) {
        FeedBuilder feedBuilder = new FeedBuilderImpl();
        SheetFactorySelector sheetFactorySelector = new DefaultSheetFactorySelector(feedBuilder);
        StandaloneRunner runner = new StandaloneRunner(sheetFactorySelector);
        try {
            CommandLine line = runner.parse(args);
            if (line.hasOption("gui") || line.getArgs().length == 0) {
                runner.setNimbusLookAndFeel();
                runner.startGUI();
            } else if (line.getArgs().length <= 1)
                runner.helper();
            else {
                Task task = runner.createTask(line);
                task.run();
            }
        } catch (ParseException ex) {
            System.err.println(
                    "Could not parse the arguments. Reason: " + ex.getMessage());
            runner.helper();
        } catch (TaskTargetException ex) {
            System.err.println(
                    "Could not run the task. Reason: " + ex.getMessage());
            runner.helper();
        } catch (InterruptedException ex) {
            System.err.println("Task interrupted. Reason: " + ex.getMessage());
            runner.helper();
        }
    }

    private final SheetFactorySelector sheetFactorySelector;

    public StandaloneRunner(SheetFactorySelector sheetFactorySelector) {
        this.sheetFactorySelector = sheetFactorySelector;
    }

    public final void helper() {
        Options options = getOptions();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar MazarineBlue.jar [options] [feed sheet]",
                            options);
    }

    private Options getOptions() {
        Options options = new Options();
        options.addOption(null, "gui", false, "Open GUI");
        options.addOption("l", "log", false, "Open the log afterwards");
        options.addOption("r", "report", false, "Open the report afterwards");
        return options;
    }

    public CommandLine parse(String[] args)
            throws ParseException {
        Options options = getOptions();
        CommandLineParser parser = new PosixParser();
        CommandLine line = parser.parse(options, args);
        return line;
    }

    public Task createTask(CommandLine line) {
        String[] args = line.getArgs();
        TestTask task = createTask(new File(args[0]), args[1]);
        task.setScheduledDate(new Date());
        if (line.hasOption("l"))
            task.setOpenLog();
        if (line.hasOption("r"))
            task.setOpenReport();
        return task;
    }

    private TestTask createTask(File file, String sheetName) {
        storeAddtionalRecentFile(file);
        DocumentManager manager = new FileDocumentManager(file);
        SheetFactory sheetFactory = sheetFactorySelector.getSheetFactory(file);
        Feed feed = sheetFactory.getSheetFeed(sheetName);
        return new TestTask(manager, sheetFactory, feed);
    }

    private void storeAddtionalRecentFile(File file) {
        try {
            new RecentFiles().storeAdditionFile(file);
        } catch (IOException ex) {
        }
    }

    public void setNimbusLookAndFeel() {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    public void startGUI() {
        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame(sheetFactorySelector);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }
}
