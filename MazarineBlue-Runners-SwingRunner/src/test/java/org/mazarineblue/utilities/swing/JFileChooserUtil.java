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
package org.mazarineblue.utilities.swing;

import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import static javax.swing.SwingUtilities.invokeLater;
import javax.swing.filechooser.FileFilter;
import static org.awaitility.Awaitility.await;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import static org.mazarineblue.utilities.swing.SwingUtil.fetchChildIndexed;

public class JFileChooserUtil {

    private final JFileChooser fileChooser;
    private final JTextField fileNameInputField;
    private final JComboBox<FileFilter> fileFilterComboBox;
    private final JButton upButton, homeButton, createFolderButton, listButton, detailsButton, openButton, cancleButton;

    @SuppressWarnings("unchecked")
    public JFileChooserUtil(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
        fileNameInputField = fetchChildIndexed(fileChooser, 0, JTextField.class);
        fileFilterComboBox = fetchChildIndexed(fileChooser, 1, JComboBox.class);

        upButton = fetchChildIndexed(fileChooser, 0, JButton.class);
        homeButton = fetchChildIndexed(fileChooser, 1, JButton.class);
        createFolderButton = fetchChildIndexed(fileChooser, 2, JButton.class);
        listButton = fetchChildIndexed(fileChooser, 3, JButton.class);
        detailsButton = fetchChildIndexed(fileChooser, 4, JButton.class);
        openButton = fetchChildIndexed(fileChooser, 9, JButton.class);
        cancleButton = fetchChildIndexed(fileChooser, 10, JButton.class);
    }

    public void waitUtilVisible() {
        await().until(fileChooser::isVisible);
    }

    public void setFilename(String filename) {
        invokeLater(() -> fileNameInputField.setText(filename));
        await().until(() -> fileNameInputField.getText().equals(filename));
    }

    public FileFilter[] getFileTypeOptions() {
        FileFilter[] arr = new FileFilter[fileFilterComboBox.getItemCount()];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = fileFilterComboBox.getItemAt(i);
        return arr;
    }

    public void setFileTypeOption(FileFilter option) {
        invokeLater(() -> fileFilterComboBox.setSelectedItem(option));
        await().until(() -> fileFilterComboBox.getSelectedItem().equals(option));
    }

    public void doClickUpButton() {
        invokeLater(() -> upButton.doClick());
    }

    public void doClickHomeButton() {
        invokeLater(() -> homeButton.doClick());
    }

    public void doClickCreateFolderButton() {
        invokeLater(() -> createFolderButton.doClick());
    }

    public void doClickListButton() {
        invokeLater(() -> listButton.doClick());
    }

    public void doClickDetailsButton() {
        invokeLater(() -> detailsButton.doClick());
    }

    public File doClickOpenButtonAndWait(Callable<File> callable) {
        invokeLater(() -> openButton.doClick());
        await().until(() -> !fileChooser.isShowing());
        return await().until(callable, new IsNot<>(new IsNull<File>()));
    }

    public void doClickCancleButton() {
        invokeLater(() -> cancleButton.doClick());
        await().until(() -> !fileChooser.isVisible());
    }
}
