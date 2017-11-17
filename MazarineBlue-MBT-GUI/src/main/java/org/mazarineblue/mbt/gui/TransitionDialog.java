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

import java.awt.Frame;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.function.BiConsumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import static org.mazarineblue.mbt.gui.StringConstants.AFTER_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.BEFORE_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.FILLED_REGEX;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import static org.mazarineblue.mbt.gui.StringConstants.VALID_CHARACTERS_REGEX;
import static org.mazarineblue.mbt.gui.StringConstants.VALID_VARIABLE_CHARACTERS_REGEX;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.verifiers.CollectionInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.InputVerifierGroup;
import org.mazarineblue.mbt.gui.verifiers.PatternInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.SerializableInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.StatesShareViewInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.UniqueCollectionObjectMatcher;
import org.mazarineblue.mbt.gui.verifiers.ValidationLabelSwitcher;

public class TransitionDialog
        extends JDialog {

    private static final long serialVersionUID = 1L;

    private final DefaultComboBoxModel<State> beforeStateModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<State> afterStateModel = new DefaultComboBoxModel<>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea actionTextArea;
    private javax.swing.JComboBox<State> afterStateComboBox;
    private javax.swing.JLabel afterStateValidationLabel;
    private org.mazarineblue.mbt.gui.list.JListPanel<State> beforeStateListPanel;
    private javax.swing.JSlider businessValueSlider;
    private org.mazarineblue.mbt.gui.control.ControlPanel<Transition> controlPanel;
    private javax.swing.JTextField guardTextField;
    private javax.swing.JLabel guardValidationLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nameValidationLabel;
    // End of variables declaration//GEN-END:variables

    public TransitionDialog(Frame parent, String title) {
        super(parent, title, true);
        initComponents();
        nameTextField.setInputVerifier(new InputVerifierGroup(
                nameCantBeBlank(),
                nameContainsValidCharactersInputVerifier()
        ));
        guardTextField.setInputVerifier(
                guardContainsValidCharactersInputVerifier()
        );
        beforeStateListPanel.setVerifier(new InputVerifierGroup(
                beforeStateContainsUniqueItemsInputVerifier(),
                beforeStatesViewMatchesAfterStatesView()
        ));
        afterStateComboBox.setSelectedIndex(-1);
        afterStateComboBox.setInputVerifier(
                afterStateViewMatchesBeforeStatesView()
        );
        controlPanel.setAccept(this, "OK");
        controlPanel.setReject(this, "Cancle");
        controlPanel.setAcceptSupplier(this::getNewTransition);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for constructor">
    private SerializableInputVerifier nameCantBeBlank() {
        return new PatternInputVerifier(FILLED_REGEX,
                                        new ValidationLabelSwitcher(nameValidationLabel, CANT_BE_BLANK));
    }

    private SerializableInputVerifier nameContainsValidCharactersInputVerifier() {
        return new PatternInputVerifier(VALID_CHARACTERS_REGEX,
                                        new ValidationLabelSwitcher(nameValidationLabel, INVALID_CHARACTERS_USED));
    }

    private SerializableInputVerifier guardContainsValidCharactersInputVerifier() {
        return new PatternInputVerifier(VALID_VARIABLE_CHARACTERS_REGEX,
                                        new ValidationLabelSwitcher(guardValidationLabel, INVALID_CHARACTERS_USED));
    }

    private SerializableInputVerifier beforeStateContainsUniqueItemsInputVerifier() {
        return new CollectionInputVerifier<>(beforeStateListPanel::getAllItems,
                                             new UniqueCollectionObjectMatcher<>(
                                                     new ValidationLabelSwitcher(beforeStateListPanel, IS_ALREADY_ADDED)));
    }

    private SerializableInputVerifier beforeStatesViewMatchesAfterStatesView() {
        return new StatesShareViewInputVerifier(() -> convert(afterStateComboBox),
                                                new ValidationLabelSwitcher(beforeStateListPanel,
                                                                            BEFORE_STATE_DOESNT_SHARE_VIEW));
    }

    private Collection<State> convert(JComboBox<State> comboBox) {
        State state = (State) comboBox.getSelectedItem();
        return state == null ? null : asList(state);
    }

    private InputVerifier afterStateViewMatchesBeforeStatesView() {
        return new StatesShareViewInputVerifier(() -> convert(beforeStateListPanel),
                                                new ValidationLabelSwitcher(afterStateValidationLabel,
                                                                            AFTER_STATE_DOESNT_SHARE_VIEW));
    }

    private Collection<State> convert(JListPanel<State> listPanel) {
        int n = listPanel.getContentCount();
        return n == 0 ? null : listPanel.getAllItems();
    }

    private boolean verifyAcceptInput() {
        // No logical operators, all validations need to be done!
        return nameTextField.getInputVerifier().verify(nameTextField)
                | guardTextField.getInputVerifier().verify(guardTextField);
    }

    private Transition getNewTransition() {
        if (!verifyAcceptInput())
            return null;
        Transition newTransition = Transition.createDefault(nameTextField.getText());
        newTransition.setGuard(guardTextField.getText());
        newTransition.setBusinessValue(businessValueSlider.getValue());
        newTransition.setSources(convert(beforeStateListPanel));
        newTransition.setDestination((State) afterStateComboBox.getSelectedItem());
        newTransition.setAction(actionTextArea.getText());
        newTransition.verify();
        return newTransition;
    }
    //</editor-fold>

    public void setOld(Transition old) {
        controlPanel.setOld(old);
        nameValidationLabel.setVisible(false);
        guardValidationLabel.setVisible(false);
        nameTextField.setText(old.getName());
        guardTextField.setText(old.getGuard());
        businessValueSlider.setValue(old.getBusinessValue());
        afterStateComboBox.setSelectedItem(old.getDestination());
        beforeStateListPanel.setItems(old.getSources());
        actionTextArea.setText(old.getAction());
    }

    public void setAcceptAction(BiConsumer<Transition, Transition> acceptAction) {
        controlPanel.setAcceptAction(acceptAction);
    }

    public void addSource(State state) {
        beforeStateListPanel.addItems(state);
    }

    /**
     * Set the options.
     *
     * @param states the states to set as options.
     */
    public void setOptions(Collection<State> states) {
        afterStateModel.removeAllElements();
        states.stream().forEach(afterStateModel::addElement);
        afterStateComboBox.setSelectedIndex(-1);
        beforeStateListPanel.setOptions(states);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        nameValidationLabel = new javax.swing.JLabel();
        javax.swing.JLabel beforeStateLabel = new javax.swing.JLabel();
        beforeStateListPanel = new JListPanel<>(beforeStateModel);
        javax.swing.JLabel afterStateLabel = new javax.swing.JLabel();
        afterStateComboBox = new javax.swing.JComboBox<>();
        javax.swing.JScrollPane actionScrollPane = new javax.swing.JScrollPane();
        actionTextArea = new javax.swing.JTextArea();
        javax.swing.JLabel actionLabel = new javax.swing.JLabel();
        afterStateValidationLabel = new javax.swing.JLabel();
        guardTextField = new javax.swing.JTextField();
        javax.swing.JLabel guardLabel = new javax.swing.JLabel();
        guardValidationLabel = new javax.swing.JLabel();
        javax.swing.JLabel businessValueLabel = new javax.swing.JLabel();
        businessValueSlider = new javax.swing.JSlider();
        controlPanel = new org.mazarineblue.mbt.gui.control.ControlPanel<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameLabel.setLabelFor(nameTextField);
        nameLabel.setText("Name");
        nameLabel.setName("nameLabel"); // NOI18N

        nameTextField.setName("nameTextField"); // NOI18N

        nameValidationLabel.setVisible(false);
        nameValidationLabel.setForeground(new java.awt.Color(255, 0, 0));
        nameValidationLabel.setText("Name validation");
        nameValidationLabel.setName("nameValidationLabel"); // NOI18N

        beforeStateLabel.setLabelFor(beforeStateListPanel);
        beforeStateLabel.setText("Before state");
        beforeStateLabel.setName("beforeStateLabel"); // NOI18N

        beforeStateListPanel.setName("beforeStateListPanel"); // NOI18N
        beforeStateListPanel.setEditable(false);

        afterStateLabel.setLabelFor(afterStateComboBox);
        afterStateLabel.setText("After state");
        afterStateLabel.setName("afterStateLabel"); // NOI18N

        afterStateComboBox.setModel(afterStateModel);
        afterStateComboBox.setName("afterStateComboBox"); // NOI18N
        afterStateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afterStateComboBoxActionPerformed(evt);
            }
        });

        actionScrollPane.setName("actionScrollPane"); // NOI18N

        actionTextArea.setColumns(20);
        actionTextArea.setRows(5);
        actionTextArea.setName("actionTextArea"); // NOI18N
        actionScrollPane.setViewportView(actionTextArea);

        actionLabel.setLabelFor(actionTextArea);
        actionLabel.setText("Action");
        actionLabel.setName("actionLabel"); // NOI18N

        afterStateValidationLabel.setForeground(new java.awt.Color(255, 0, 0));
        afterStateValidationLabel.setText("After state validation");
        afterStateValidationLabel.setName("afterStateValidationLabel"); // NOI18N
        afterStateValidationLabel.setVisible(false);

        guardTextField.setName("guardTextField"); // NOI18N

        guardLabel.setText("Guard");
        guardLabel.setName("guardLabel"); // NOI18N

        guardValidationLabel.setForeground(new java.awt.Color(255, 0, 0));
        guardValidationLabel.setText("Guard validation");
        guardValidationLabel.setName("guardValidationLabel"); // NOI18N
        guardValidationLabel.setVisible(false);

        businessValueLabel.setText("Business value");
        businessValueLabel.setName("businessValueLabel"); // NOI18N

        businessValueSlider.setMajorTickSpacing(10);
        businessValueSlider.setMinorTickSpacing(5);
        businessValueSlider.setPaintLabels(true);
        businessValueSlider.setPaintTicks(true);
        businessValueSlider.setSnapToTicks(true);
        businessValueSlider.setName("businessValueSlider"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(beforeStateLabel)
                            .addComponent(afterStateLabel)
                            .addComponent(actionLabel)
                            .addComponent(guardLabel)
                            .addComponent(businessValueLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afterStateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(actionScrollPane)
                            .addComponent(nameTextField)
                            .addComponent(guardTextField)
                            .addComponent(beforeStateListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                            .addComponent(guardValidationLabel)
                            .addComponent(afterStateValidationLabel)
                            .addComponent(nameValidationLabel)
                            .addComponent(businessValueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameValidationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guardLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guardValidationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(businessValueSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(beforeStateListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(beforeStateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afterStateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(afterStateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afterStateValidationLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(actionLabel)
                            .addComponent(actionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                    .addComponent(businessValueLabel))
                .addGap(18, 18, 18)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void afterStateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_afterStateComboBoxActionPerformed
        if (afterStateComboBox.getInputVerifier() == null)
            return;
        afterStateComboBox.getInputVerifier().verify(afterStateComboBox);
    }//GEN-LAST:event_afterStateComboBoxActionPerformed
}
