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
package org.mazarineblue.mbt.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

public class ExceptionDialogTest {

    private static final String MESSAGE = "Test Message";

    private Exception ex;
    private ExceptionDialog dialog;
    private JLabel iconLabel;
    private JLabel messageLabel;
    private JTextArea exceptionTextArea;
    private JButton closeButton;

    @Before
    public void setup() {
        try {
            throwException();
        } catch (UnsupportedOperationException ex) {
            this.ex = ex;
            dialog = new ExceptionDialog(new JFrame(), ex);
            iconLabel = fetchChildNamed(dialog, "iconLabel", JLabel.class);
            messageLabel = fetchChildNamed(dialog, "messageLabel", JLabel.class);
            exceptionTextArea = fetchChildNamed(dialog, "exceptionTextArea", JTextArea.class);
            closeButton = fetchChildNamed(dialog, "closeButton", JButton.class);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    private void throwException() {
        first();
    }

    private void first() {
        second();
    }

    private void second() {
        thirth();
    }

    private void thirth() {
        throw new UnsupportedOperationException(MESSAGE);
    }
    //</editor-fold>

    @After
    public void teardown() {
        dialog.dispose();
        ex = null;
        dialog = null;
    }

    @Test
    public void test() {
        assertEquals("", iconLabel.getText());
        assertEquals(UIManager.getIcon("OptionPane.errorIcon"), iconLabel.getIcon());
        assertEquals("(java.lang.UnsupportedOperationException) " + MESSAGE, messageLabel.getText());
        assertEquals(null, messageLabel.getIcon());
        assertEquals(getStackTrace(ex), exceptionTextArea.getText());
    }

    private static String getStackTrace(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder builder = new StringBuilder(stackTrace.length * 96);
        for (StackTraceElement e : stackTrace)
            builder.append(e.toString()).append(System.lineSeparator());
        return builder.toString();
    }

    @Test
    public void close() {
        closeButton.doClick();
        assertEquals(false, dialog.isDisplayable());
    }
}
