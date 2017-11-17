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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;

public class TransitionPage
        implements AutoCloseable {

    private static final int TIMEOUT = 500;

    public final TransitionDialog dialog;
    private final JTextField nameTextField;
    private final JLabel nameValidationLabel;
    private final JTextField guardTextField;
    private final JLabel guardValidationLabel;
    private final JSlider businessValueSlider;
    private final JListPanel<State> beforeStatesPanel;
    private final JComboBox<?> beforeStateComboBox;
    private final JButton beforeStateAddButton;
    private final JLabel beforeStateValidationLabel;
    private final JComboBox<?> afterStateComboBox;
    private final JLabel afterStateValidationLabel;
    private final JTextArea actionTextArea;
    private final JButton acceptButton;
    private final JButton rejectButton;

    @SuppressWarnings("unchecked")
    public TransitionPage(TransitionDialog dialog) {
        this.dialog = dialog;

        nameTextField = fetchChildNamed(dialog, "nameTextField", JTextField.class);
        nameValidationLabel = fetchChildNamed(dialog, "nameValidationLabel", JLabel.class);
        guardTextField = fetchChildNamed(dialog, "guardTextField", JTextField.class);
        guardValidationLabel = fetchChildNamed(dialog, "guardValidationLabel", JLabel.class);
        businessValueSlider = fetchChildNamed(dialog, "businessValueSlider", JSlider.class);

        beforeStatesPanel = fetchChildNamed(dialog, "beforeStateListPanel", JListPanel.class);
        beforeStateComboBox = fetchChildNamed(beforeStatesPanel, "optionsComboBox", JComboBox.class);
        beforeStateAddButton = fetchChildNamed(beforeStatesPanel, "addButton", JButton.class);
        beforeStateValidationLabel = fetchChildNamed(beforeStatesPanel, "validationLabel", JLabel.class);

        afterStateComboBox = fetchChildNamed(dialog, "afterStateComboBox", JComboBox.class);
        afterStateValidationLabel = fetchChildNamed(dialog, "afterStateValidationLabel", JLabel.class);
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
    public void focusOnName() {
        nameTextField.requestFocus();
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
    public String getNameErrorMessage() {
        return nameValidationLabel.getText();
    }

    public boolean isNameErrorVisible() {
        return nameValidationLabel.isVisible();
    }

    public void setNameErrorVisible(boolean flag) {
        nameValidationLabel.setVisible(flag);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="guardTextField">
    public void focusOnGuard()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(guardTextField::requestFocus);
    }

    public String getGuard()
            throws TimeoutException {
        return waitFor(guardTextField::getText, TIMEOUT);
    }

    public void setGuard(String name)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> {
            guardTextField.requestFocus();
            guardTextField.setText(name);
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="guardValidationLabel">
    public String getGuardErrorMessage()
            throws TimeoutException {
        return waitFor(guardValidationLabel::getText, TIMEOUT);
    }

    public boolean isGuardErrorVisible() {
        return guardValidationLabel.isVisible();
    }

    public void setGuardErrorVisible(boolean flag)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> guardValidationLabel.setVisible(flag));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="businessValueSlider">
    public void setBusinessValue(int value)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> businessValueSlider.setValue(value));
    }

    public int getBusinessValue() {
        return businessValueSlider.getValue();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="beforeStatesPanel">
    public JPanel getBeforeStateComponent(int index, Class<JPanel> type) {
        return beforeStatesPanel.getContentComponent(index, type);
    }

    public int getBeforeStateCount()
            throws TimeoutException {
        return waitFor(beforeStatesPanel::getContentCount, TIMEOUT);
    }

    public void addBeforeState(int index)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> {
            beforeStateComboBox.requestFocus();
            beforeStateComboBox.setSelectedIndex(index);
            beforeStateAddButton.doClick();
        });
    }

    public void addBeforeState(State s)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> {
            beforeStateComboBox.requestFocus();
            beforeStateComboBox.setSelectedItem(s);
            beforeStateAddButton.doClick();
        });
    }

    public void addBeforeState()
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> beforeStateAddButton.doClick());
    }

    public State getBeforeState(int index)
            throws TimeoutException {
        return waitFor(() -> beforeStatesPanel.getItem(index), TIMEOUT);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="beforeStateComboBox">
    public void focusOnBeforeStateOptions() {
        beforeStateComboBox.requestFocus();
    }

    public int getBeforeStateOptionCount()
            throws TimeoutException {
        return waitFor(beforeStateComboBox::getItemCount, TIMEOUT);
    }

    public Object getBeforeStateOption(int index)
            throws TimeoutException {
        return waitFor(() -> beforeStateComboBox.getItemAt(index), TIMEOUT);
    }

    public int getBeforeStateSelectedIndex()
            throws TimeoutException {
        return waitFor(beforeStateComboBox::getSelectedIndex, TIMEOUT);
    }

    public boolean isBeforeStateOptionEditable()
            throws TimeoutException {
        return waitFor(beforeStateComboBox::isEditable, TIMEOUT);
    }

    public Object getBeforeStateSelectedOption()
            throws TimeoutException {
        return waitFor(beforeStateComboBox::getSelectedItem, TIMEOUT);
    }

    public void setBeforeStateSelectedInded(int index)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> beforeStateComboBox.setSelectedIndex(index));
    }

    public void setBeforeStateSelectedOption(State option)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> beforeStateComboBox.setSelectedItem(option));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="beforeStateValidationLabel">
    public String getBeforeStateErrorMessage()
            throws TimeoutException {
        return waitFor(beforeStateValidationLabel::getText, TIMEOUT);
    }

    public boolean isBeforeStateErrorVisible() {
        return beforeStateValidationLabel.isVisible();
    }

    public void setBeforeStateErrorVisible(boolean flag)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> beforeStateValidationLabel.setVisible(flag));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="afterStateComboBox">
    public void focusOnAfterStateOptions() {
        afterStateComboBox.requestFocus();
    }

    public int getAfterStateOptionCount()
            throws TimeoutException {
        return waitFor(afterStateComboBox::getItemCount, TIMEOUT);
    }

    public Object getAfterStateOption(int index)
            throws TimeoutException {
        return waitFor(() -> afterStateComboBox.getItemAt(index), TIMEOUT);
    }

    public int getAfterStateSelectedIndex()
            throws TimeoutException {
        return waitFor(afterStateComboBox::getSelectedIndex, TIMEOUT);
    }

    public boolean isAfterStateOptionEditable()
            throws TimeoutException {
        return waitFor(afterStateComboBox::isEditable, TIMEOUT);
    }

    public Object getAfterStateSelectedOption()
            throws TimeoutException {
        return waitFor(afterStateComboBox::getSelectedItem, TIMEOUT);
    }

    public void setAfterStateSelectedIndex(int index)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> afterStateComboBox.setSelectedIndex(index));
    }

    public void setAfterStateSelectedOption(State option)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> afterStateComboBox.setSelectedItem(option));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="afterStateValidationLabel">
    public String getAfterStateErrorMessage()
            throws TimeoutException {
        return waitFor(afterStateValidationLabel::getText, TIMEOUT);
    }

    public boolean isAfterStateErrorVisible() {
        return afterStateValidationLabel.isVisible();
    }

    public void setAfterStateErrorVisible(boolean flag)
            throws InterruptedException, InvocationTargetException {
        invokeAndWait(() -> afterStateValidationLabel.setVisible(flag));
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
