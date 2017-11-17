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
package org.mazarineblue.mbt.gui.pages;

import static java.awt.EventQueue.invokeAndWait;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.mazarineblue.mbt.gui.StateDialog;
import org.mazarineblue.mbt.gui.list.JListPanel;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;

public class StatePage
        implements AutoCloseable {

    private static final int TIMEOUT = 500;

    public final StateDialog dialog;
    private final JTextField nameTextField;
    private final JLabel nameValidationLabel;
    private final JListPanel<String> viewPanel;
    private final JComboBox<?> viewComboBox;
    private final JButton addViewButton;
    private final JLabel viewValidationLabel;
    private final JTextArea actionTextArea;
    private final JButton acceptButton;
    private final JButton rejectButton;

    @SuppressWarnings("unchecked")
    public StatePage(StateDialog dialog) {
        this.dialog = dialog;
        nameTextField = fetchChildNamed(dialog, "nameTextField", JTextField.class);
        nameValidationLabel = fetchChildNamed(dialog, "nameValidationLabel", JLabel.class);

        viewPanel = fetchChildNamed(dialog, "viewListPanel", JListPanel.class);
        viewComboBox = fetchChildNamed(viewPanel, "optionsComboBox", JComboBox.class);
        addViewButton = fetchChildNamed(viewPanel, "addButton", JButton.class);
        viewValidationLabel = fetchChildNamed(viewPanel, "validationLabel", JLabel.class);

        actionTextArea = fetchChildNamed(dialog, "actionTextArea", JTextArea.class);
        acceptButton = fetchChildNamed(dialog, "acceptButton", JButton.class);
        rejectButton = fetchChildNamed(dialog, "rejectButton", JButton.class);
        nameTextField.requestFocus();
    }

    @Override
    public void close()
            throws Exception {
        dialog.dispose();
    }

    //<editor-fold defaultstate="collapsed" desc="nameTextField">
    public void focusOnName()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(nameTextField::requestFocus);
    }

    public String getName()
            throws TimeoutException {
        return waitFor(nameTextField::getText, TIMEOUT);
    }

    public void setName(String name)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> nameTextField.setText(name));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="nameValidationLabel">
    public String getNameErrorMessage()
            throws TimeoutException {
        return waitFor(nameValidationLabel::getText, 500);
    }

    public boolean isNameErrorVisible() {
        return nameValidationLabel.isVisible();
    }

    public void setNameErrorVisible(boolean flag)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> nameValidationLabel.setVisible(flag));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="viewPanel">
    public JPanel getViewComponent(int index, Class<JPanel> type) {
        return viewPanel.getContentComponent(index, type);
    }

    public String getView(int index)
            throws TimeoutException {
        return waitFor(() -> viewPanel.getItem(index), TIMEOUT);
    }

    public int getViewCount()
            throws TimeoutException {
        return waitFor(viewPanel::getContentCount, TIMEOUT);
    }

    public void addView(String item)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> {
            viewComboBox.requestFocus();
            viewComboBox.setSelectedItem(item);
            addViewButton.doClick();
        });
    }

    public void addView()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> addViewButton.doClick());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="viewComboBox">
    public void focusOnOptions()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> viewComboBox.requestFocus());
    }

    public int getOptionCount()
            throws TimeoutException {
        return waitFor(viewComboBox::getItemCount, 500);
    }

    public Object getOption(int index)
            throws TimeoutException {
        return waitFor(() -> viewComboBox.getItemAt(index), TIMEOUT);
    }

    public int getSelectedIndex()
            throws TimeoutException {
        return waitFor(viewComboBox::getSelectedIndex, 500);
    }

    public boolean isOptionEditable()
            throws TimeoutException {
        return waitFor(viewComboBox::isEditable, 500);
    }

    public Object getSelectedOption()
            throws TimeoutException {
        return waitFor(viewComboBox::getSelectedItem, 500);
    }

    public void setSelectedOption(String option)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> viewComboBox.setSelectedItem(option));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="viewValidationLabel">
    public void setViewErrorVisible(boolean flag)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> viewValidationLabel.setVisible(flag));
    }

    public String getViewErrorMessage()
            throws TimeoutException {
        return waitFor(viewValidationLabel::getText, TIMEOUT);
    }

    public boolean isViewErrorVisible() {
        return viewValidationLabel.isVisible();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="actionTextArea">
    public void focusOnAction()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> actionTextArea.requestFocus());
    }

    public String getAction()
            throws TimeoutException {
        return waitFor(actionTextArea::getText, TIMEOUT);
    }

    public void setAction(String action)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> actionTextArea.setText(action));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="acceptButton">
    public void accept()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> acceptButton.doClick());
    }
    //</editor-fold>
}
