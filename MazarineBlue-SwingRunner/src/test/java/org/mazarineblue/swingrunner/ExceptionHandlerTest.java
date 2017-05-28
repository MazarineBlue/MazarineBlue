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
package org.mazarineblue.swingrunner;

import java.awt.Window;
import java.io.File;
import java.util.concurrent.TimeoutException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.swing.SwingUtil;
import org.mazarineblue.swingrunner.archive.exceptions.FileAccessException;
import org.mazarineblue.swingrunner.archive.exceptions.UnableToReadFromFileException;
import org.mazarineblue.swingrunner.archive.exceptions.UnableToWriteToFileException;
import org.mazarineblue.swingrunner.exceptions.NoFileSelectedException;
import org.mazarineblue.swingrunner.exceptions.NoSheetSelectedException;
import org.mazarineblue.swingrunner.exceptions.UserException;
import org.mazarineblue.swingrunner.util.DummyExceptionReporter;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionHandlerTest {

    private static final int TIMEOUT = 1000;

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
        dialog = openErrorDialog(new NullPointerException(), TIMEOUT);
        assertErrorMessage("Unknown error occurred.", dialog);
    }

    @Test
    public void NoFileSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoFileSelectedException(), TIMEOUT);
        assertErrorMessage("A file needs to be selected.", dialog);
    }

    @Test
    public void NoSheetSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoSheetSelectedException(), TIMEOUT);
        assertErrorMessage("A sheet needs to be selected.", dialog);
    }

    @Test
    public void SwingException()
            throws TimeoutException {
        dialog = openErrorDialog(new UserException(), TIMEOUT);
        assertErrorMessage("Unknown user error occurred.", dialog);
    }

    @Test
    public void UnableToReadFromFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new UnableToReadFromFileException(file, null), TIMEOUT);
        assertErrorMessage("Unable to read from file: " + file, dialog);
    }

    @Test
    public void UnableToWriteToFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new UnableToWriteToFileException(file, null), TIMEOUT);
        assertErrorMessage("Unable to write to file: " + file, dialog);
    }

    @Test
    public void FileAccessException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new FileAccessException(file, null), TIMEOUT);
        assertErrorMessage("Unknown error occurred with file: " + file, dialog);
    }

    private JDialog openErrorDialog(Throwable ex, long timeout)
            throws TimeoutException {
        SwingUtilities.invokeLater(() -> handler.uncaughtException(Thread.currentThread(), ex));
        return SwingUtil.waitFor(() -> SwingUtil.fetchWindowTitled(parent, title, JDialog.class), timeout);
    }

    private void assertErrorMessage(String errorMessage, JDialog dialog) {
        assertEquals(errorMessage, SwingUtil.fetchChildIndexed(dialog, 0, JLabel.class).getText());
    }
}
