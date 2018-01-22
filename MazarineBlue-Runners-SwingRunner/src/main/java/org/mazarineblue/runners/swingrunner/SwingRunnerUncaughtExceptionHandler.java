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

import java.awt.Component;
import static java.util.logging.Level.SEVERE;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import org.mazarineblue.runners.swingrunner.archive.exceptions.FileAccessException;
import org.mazarineblue.runners.swingrunner.archive.exceptions.UnableToReadFromFileException;
import org.mazarineblue.runners.swingrunner.archive.exceptions.UnableToWriteToFileException;
import org.mazarineblue.runners.swingrunner.exceptions.NoFileSelectedException;
import org.mazarineblue.runners.swingrunner.exceptions.NoSheetSelectedException;
import org.mazarineblue.runners.swingrunner.exceptions.UserException;
import org.mazarineblue.runners.swingrunner.util.ExceptionReporter;

/**
 * An {@code ExceptionHandler} is a {@code Thread.UncaughtExceptionHandler}
 * that catches uncaught exceptions, displays a message dialog and (optionally)
 * logs an exception.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class SwingRunnerUncaughtExceptionHandler
        implements Thread.UncaughtExceptionHandler {

    private final String title;
    private final Component parent;
    private final ExceptionReporter logger;

    /**
     * Constructs a {@code Thread.UncaughtExceptionHandler} that processes
     * exceptions that where left uncaught. When this occurs it opens a dialog
     * with the specified title. And it prints the stack trace using the
     * specified logger.
     *
     * @param title  the title to show in the dialog.
     * @param parent the parent of the dialogs.
     * @param logger the logger to print the stack trace to.
     */
    SwingRunnerUncaughtExceptionHandler(String title, Component parent, ExceptionReporter logger) {
        this.title = title;
        this.parent = parent;
        this.logger = logger;
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        JOptionPane.showMessageDialog(parent, getErrorMessage(ex), title, ERROR_MESSAGE);
        logger.log(SEVERE, null, ex);
    }

    private String getErrorMessage(Throwable ex) {
        if (ex instanceof UserException)
            return getErrorMessage((UserException) ex);
        if (ex instanceof FileAccessException)
            return getErrorMessage((FileAccessException) ex);
        return "Unknown error occurred.";
    }

    private String getErrorMessage(UserException ex) {
        if (ex instanceof NoFileSelectedException)
            return "A file needs to be selected.";
        if (ex instanceof NoSheetSelectedException)
            return "A sheet needs to be selected.";
        return "Unknown user error occurred.";
    }

    private String getErrorMessage(FileAccessException ex) {
        if (ex instanceof UnableToReadFromFileException)
            return "Unable to read from file: " + ex.getFile();
        if (ex instanceof UnableToWriteToFileException)
            return "Unable to write to file: " + ex.getFile();
        return "Unknown error occurred with file: " + ex.getFile();
    }
}
