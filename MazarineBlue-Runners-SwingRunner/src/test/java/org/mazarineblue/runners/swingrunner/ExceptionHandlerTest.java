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

import java.awt.Window;
import java.io.File;
import static java.lang.Thread.currentThread;
import java.util.concurrent.TimeoutException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static javax.swing.SwingUtilities.invokeLater;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.runners.swingrunner.archive.exceptions.FileAccessException;
import org.mazarineblue.runners.swingrunner.archive.exceptions.UnableToReadFromFileException;
import org.mazarineblue.runners.swingrunner.archive.exceptions.UnableToWriteToFileException;
import org.mazarineblue.runners.swingrunner.exceptions.NoFileSelectedException;
import org.mazarineblue.runners.swingrunner.exceptions.NoSheetSelectedException;
import org.mazarineblue.runners.swingrunner.exceptions.UserException;
import org.mazarineblue.runners.swingrunner.util.DummyExceptionReporter;
import static org.mazarineblue.utilities.swing.SwingUtil.fetchChildIndexed;
import static org.mazarineblue.utilities.swing.SwingUtil.fetchWindowTitled;
import static org.mazarineblue.utilities.swing.SwingUtil.waitFor;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionHandlerTest {

    private static final int TIMEOUT = 2000;

    private JDialog dialog;
    private String title;
    private Window parent;
    private SwingRunnerUncaughtExceptionHandler handler;

    @Before
    public void setup() {
        title = "Error";
        parent = new JFrame();
        handler = new SwingRunnerUncaughtExceptionHandler(title, parent, new DummyExceptionReporter());
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        handler = null;
    }

    @Test
    public void nullPointer()
            throws TimeoutException {
        dialog = openErrorDialog(new NullPointerException());
        assertErrorMessage("Unknown error occurred.", dialog);
    }

    @Test
    public void NoFileSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoFileSelectedException());
        assertErrorMessage("A file needs to be selected.", dialog);
    }

    @Test
    public void NoSheetSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoSheetSelectedException());
        assertErrorMessage("A sheet needs to be selected.", dialog);
    }

    @Test
    public void SwingException()
            throws TimeoutException {
        dialog = openErrorDialog(new UserException());
        assertErrorMessage("Unknown user error occurred.", dialog);
    }

    @Test
    public void UnableToReadFromFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new UnableToReadFromFileException(file, null));
        assertErrorMessage("Unable to read from file: " + file, dialog);
    }

    @Test
    public void UnableToWriteToFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new UnableToWriteToFileException(file, null));
        assertErrorMessage("Unable to write to file: " + file, dialog);
    }

    @Test
    public void FileAccessException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new FileAccessException(file, null));
        assertErrorMessage("Unknown error occurred with file: " + file, dialog);
    }

    private JDialog openErrorDialog(Throwable ex)
            throws TimeoutException {
        invokeLater(() -> handler.uncaughtException(currentThread(), ex));
        return waitFor(() -> fetchWindowTitled(parent, title, JDialog.class), TIMEOUT);
    }

    private void assertErrorMessage(String errorMessage, JDialog dialog) {
        assertEquals(errorMessage, fetchChildIndexed(dialog, 0, JLabel.class).getText());
    }
}
