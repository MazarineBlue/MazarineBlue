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
import java.util.Collection;
import java.util.function.BiConsumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.FILLED_REGEX;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import static org.mazarineblue.mbt.gui.StringConstants.VALID_CHARACTERS_REGEX;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.verifiers.CollectionInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.InputVerifierGroup;
import org.mazarineblue.mbt.gui.verifiers.PatternInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.SerializableInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.UniqueCollectionObjectMatcher;
import org.mazarineblue.mbt.gui.verifiers.ValidationLabelSwitcher;

public class StateDialog
        extends JDialog {

    private static final long serialVersionUID = 1L;

    private final DefaultComboBoxModel<String> views = new DefaultComboBoxModel<>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea actionTextArea;
    private org.mazarineblue.mbt.gui.control.ControlPanel<State> controlPanel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nameValidationLabel;
    private org.mazarineblue.mbt.gui.list.JListPanel<String> viewListPanel;
    // End of variables declaration//GEN-END:variables

    public StateDialog(Frame parent, String title) {
        super(parent, title, true);
        initComponents();
        nameTextField.setInputVerifier(new InputVerifierGroup(
                nameCantBeBlank(),
                nameContainsValidCharactersInputVerifier()
        ));
        viewListPanel.setVerifier(new InputVerifierGroup(
                viewCantBeBlank(),
                viewContainsValidCharactersInputVerifier(),
                viewContainsUniqueItemsInputVerifier()
        ));
        controlPanel.setAccept(this, "OK");
        controlPanel.setReject(this, "Cancle");
        controlPanel.setAcceptSupplier(this::getNewState);
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

    private SerializableInputVerifier viewCantBeBlank() {
        return new PatternInputVerifier(FILLED_REGEX,
                                        new ValidationLabelSwitcher(viewListPanel, CANT_BE_BLANK));
    }

    private SerializableInputVerifier viewContainsValidCharactersInputVerifier() {
        return new PatternInputVerifier(VALID_CHARACTERS_REGEX,
                                        new ValidationLabelSwitcher(viewListPanel, INVALID_CHARACTERS_USED));
    }

    private SerializableInputVerifier viewContainsUniqueItemsInputVerifier() {
        return new CollectionInputVerifier<>(viewListPanel::getAllItems,
                                             new UniqueCollectionObjectMatcher<>(
                                                     new ValidationLabelSwitcher(viewListPanel, IS_ALREADY_ADDED)));
    }

    private boolean verifyAcceptInput() {
        // No logical operators, all validations need to be done!
        return nameTextField.getInputVerifier().verify(nameTextField);
    }

    private State getNewState() {
        if (!verifyAcceptInput())
            return null;
        State newState = State.createDefault(nameTextField.getText());
        newState.addViews(viewListPanel.getAllItems());
        newState.setAction(actionTextArea.getText());
        newState.verify();
        return newState;
    }
    //</editor-fold>

    public void setOptions(Collection<String> asList) {
        viewListPanel.setOptions(asList);
    }

    public void setOld(State old) {
        controlPanel.setOld(old);
        nameTextField.setText(old.getName());
        viewListPanel.setItems(old.getViews());
        actionTextArea.setText(old.getAction());
        nameValidationLabel.setVisible(false);
    }

    public void setAcceptAction(BiConsumer<State, State> acceptAction) {
        controlPanel.setAcceptAction(acceptAction);
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
        javax.swing.JLabel viewLabel = new javax.swing.JLabel();
        javax.swing.JScrollPane actionScrollPane = new javax.swing.JScrollPane();
        actionTextArea = new javax.swing.JTextArea();
        javax.swing.JLabel actionLabel = new javax.swing.JLabel();
        viewListPanel = new JListPanel<>(views);
        nameValidationLabel = new javax.swing.JLabel();
        controlPanel = new org.mazarineblue.mbt.gui.control.ControlPanel<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameLabel.setLabelFor(nameTextField);
        nameLabel.setText("Name");

        nameTextField.setName("nameTextField"); // NOI18N

        viewLabel.setLabelFor(viewListPanel);
        viewLabel.setText("Views");

        actionScrollPane.setName("actionScrollPane"); // NOI18N

        actionTextArea.setColumns(20);
        actionTextArea.setRows(5);
        actionTextArea.setName("actionTextArea"); // NOI18N
        actionScrollPane.setViewportView(actionTextArea);

        actionLabel.setLabelFor(actionTextArea);
        actionLabel.setText("Action");

        viewListPanel.setName("viewListPanel"); // NOI18N
        viewListPanel.setEditable(true);

        nameValidationLabel.setForeground(new java.awt.Color(255, 0, 0));
        nameValidationLabel.setText("Name validation");
        nameValidationLabel.setName("nameValidationLabel"); // NOI18N
        nameValidationLabel.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(actionLabel)
                            .addComponent(viewLabel)
                            .addComponent(nameLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nameValidationLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(actionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                            .addComponent(viewListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameTextField)))
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
                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameValidationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(viewListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(actionLabel)
                            .addComponent(actionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
                    .addComponent(viewLabel))
                .addGap(18, 18, 18)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
}
