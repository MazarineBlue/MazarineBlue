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
package org.mazarineblue.runners.swingrunner.util;

import java.io.File;
import static javax.swing.SwingUtilities.invokeLater;
import static org.awaitility.Awaitility.await;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.mazarineblue.runners.swingrunner.screens.main.MainFrame;
import org.mazarineblue.utilities.swing.JFileChooserUtil;
import static org.mazarineblue.utilities.swing.SwingUtil.clickButton;
import static org.mazarineblue.utilities.swing.SwingUtil.getComboBoxOptionAtIndex;
import static org.mazarineblue.utilities.swing.SwingUtil.getComboBoxOptions;
import static org.mazarineblue.utilities.swing.SwingUtil.getComboBoxSelectedItem;
import static org.mazarineblue.utilities.swing.SwingUtil.setComboBoxOption;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MainFrameUtil {

    private static final String SELECT_FILE_BUTTON_NAME = "selectFileButton";
    private static final String FILE_COMBO_BOX_NAME = "fileComboBox";
    private static final String SHEET_COMBO_BOX_NAME = "sheetComboBox";
    private static final String EXECUTE_BUTTON_NAME = "executeButton";

    private final MainFrame frame;
    private JFileChooserUtil fileChooser;

    public MainFrameUtil(MainFrame frame) {
        this.frame = frame;
    }

    public void dispose() {
        frame.dispose();
    }

    public void setFileChooser(JFileChooserUtil fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void setVisible() {
        invokeLater(() -> frame.setVisible(true));
        await().until(frame::isVisible);
    }

    public void doClickSelectFileButton() {
        if (fileChooser == null)
            clickButton(frame, SELECT_FILE_BUTTON_NAME);
        else {
            invokeLater(() -> clickButton(frame, SELECT_FILE_BUTTON_NAME));
            fileChooser.waitUtilVisible();
        }
    }

    public File getFileOption(int index) {
        return await().until(() -> (File) getComboBoxOptionAtIndex(frame, FILE_COMBO_BOX_NAME, index),
                             new IsNot<>(new IsNull<>()));
    }

    public File[] getFileOptions() {
        return await().until(() -> (File[]) getComboBoxOptions(frame, FILE_COMBO_BOX_NAME),
                             new IsNot<>(new IsNull<>()));
    }

    public File getSelectedFile() {
        return (File) getComboBoxSelectedItem(frame, FILE_COMBO_BOX_NAME);
    }

    public void setSelectedFile(File option) {
        setComboBoxOption(frame, FILE_COMBO_BOX_NAME, option);
    }

    public String getSheetOption(int index) {
        return (String) getComboBoxOptionAtIndex(frame, SHEET_COMBO_BOX_NAME, index);
    }

    public String[] getSheetOptions() {
        return (String[]) getComboBoxOptions(frame, SHEET_COMBO_BOX_NAME);
    }

    public String getSelectedSheet() {
        return (String) getComboBoxSelectedItem(frame, SHEET_COMBO_BOX_NAME);
    }

    public void setSelectedSheet(String option) {
        setComboBoxOption(frame, SHEET_COMBO_BOX_NAME, option);
    }

    public void doClickExecuteButton() {
        clickButton(frame, EXECUTE_BUTTON_NAME);
    }
}
