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
package org.mazarineblue.mbt.gui.list;

import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.mazarineblue.mbt.gui.exceptions.InvalidContentException;
import org.mazarineblue.mbt.gui.verifiers.SerializableInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.ValidationLabel;

/**
 * A {@code JListPanel} is a component that manages a list of items. Items
 * can be added by selecting them from a combobox and removed using a button
 * that is presented next to the item.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of items to manage.
 */
public class JListPanel<T extends Serializable>
        extends JPanel
        implements ValidationLabel {

    private static final String REMOVE = "Remove";
    private static final long serialVersionUID = 1L;

    private transient JPanel contentPanel;
    private DefaultComboBoxModel<T> model;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private transient javax.swing.JComboBox<T> optionsComboBox;
    private transient javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Creates a default JListPanel.
     */
    public JListPanel() {
        this(new DefaultComboBoxModel<>());
    }

    /**
     * Creates a {@code JListPanel} with the options in specified model.
     *
     * @param model the model with the options.
     */
    public JListPanel(DefaultComboBoxModel<T> model) {
        this.model = model;
        initComponents();
    }

    /**
     * Determines whether the options field is editable.
     *
     * @param editable when {@code true} indicated editable.
     */
    public void setEditable(boolean editable) {
        optionsComboBox.setEditable(editable);
    }

    /**
     * Sets the input verifier for the options field.
     *
     * @param verifier the verifier to use on the options field.
     */
    public void setVerifier(SerializableInputVerifier verifier) {
        optionsComboBox.setInputVerifier(verifier);
    }

    @Override
    public void setMessage(String msg) {
        validationLabel.setText(msg);
        validationLabel.setVisible(true);
    }

    @Override
    public void hideMessage() {
        validationLabel.setVisible(false);
    }

    /**
     * Gets the number of content components in this panel.
     * <p>
     * Note: This method should be called under AWT tree lock.
     *
     * @return the number of components in this panel
     *
     * @see #getComponent
     * @since JDK1.1
     * @see Component#getTreeLock()
     */
    public int getContentCount() {
        return contentPanel.getComponentCount();
    }

    /**
     * Gets the nth content component in this container.
     * <p>
     * Note: This method should be called under AWT tree lock.
     *
     * @param <T>   the type of the component
     * @param index the index of the component to get
     * @param type  the type of component
     * @return the n<sup>th</sup> component in this container
     *
     * @exception ArrayIndexOutOfBoundsException if the n<sup>th</sup> value
     *                                           does not exist.
     * @see Component#getTreeLock()
     */
    public <T extends Component> T getContentComponent(int index, Class<T> type) {
        return type.cast(contentPanel.getComponent(index));
    }

    /**
     * Gets all the content components in this container.
     * <p>
     * Note: This method should be called under AWT tree lock.
     *
     * @return an array of all the components in this container.
     *
     * @see Component#getTreeLock()
     */
    public Component[] getContentComponents() {
        return contentPanel.getComponents();
    }

    /**
     * Fetches the content in this container.
     *
     * @return a collection of all content in this container.
     */
    @SuppressWarnings("unchecked")
    public Collection<T> getAllItems() {
        return asList(contentPanel.getComponents()).stream()
                .map(comp -> (JItemPanel<T>) comp)
                .map(panel -> panel.getItem())
                .collect(() -> new ArrayList<>(contentPanel.getComponentCount()), Collection::add, Collection::addAll);
    }

    /**
     * Gets the n<sup>th</sup> item.
     *
     * @param n the index of the item to get.
     * @return the n<sup>th</sup> item.
     */
    public T getItem(int n) {
        return getItemPanel(n).getItem();
    }

    @SuppressWarnings("unchecked")
    private JItemPanel<T> getItemPanel(int index) {
        return (JItemPanel<T>) contentPanel.getComponent(index);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
        optionsComboBox = new javax.swing.JComboBox<>();
        javax.swing.JButton addButton = new javax.swing.JButton();
        validationLabel = new javax.swing.JLabel();
        validationLabel.setVisible(false);

        scrollPane.setName("scrollPane"); // NOI18N
        contentPanel = new JPanel(new ListLayoutManager());
        contentPanel.setName("contentPanel");
        scrollPane.setViewportView(contentPanel);

        optionsComboBox.setModel(model);
        optionsComboBox.setName("optionsComboBox"); // NOI18N
        optionsComboBox.setSelectedIndex(-1);

        addButton.setText("Add");
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add(evt);
            }
        });

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));
        validationLabel.setText("Validation");
        validationLabel.setName("validationLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(optionsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addButton))
            .addGroup(layout.createSequentialGroup()
                .addComponent(validationLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optionsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setOptions(Collection<T> collection) {
        model.removeAllElements();
        collection.stream().forEach(model::addElement);
        optionsComboBox.setSelectedIndex(-1);
    }

    @SuppressWarnings("unchecked")
    public void addItems(T... items) {
        List<T> collection = asList(items);
        validateContent(collection);
        addItems(collection);
    }

    public void setItems(Collection<T> collection) {
        validateContent(collection);
        contentPanel.removeAll();
        addItems(collection);
        optionsComboBox.setSelectedIndex(-1);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper method for setItems()">
    private void validateContent(Collection<T> collection) {
        collection.stream().forEach(this::validateItem);
    }

    private void validateItem(T item) {
        if (verifyInput(item))
            return;
        validationLabel.setVisible(false);
        throw new InvalidContentException(item);
    }

    private void addItems(Collection<T> collection) {
        collection.stream().forEach(this::addItem);
    }
    //</editor-fold>

    private void add(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add
        if (!verifyInput(optionsComboBox))
            return;
        addItem(getSelectedOption());
    }//GEN-LAST:event_add

    //<editor-fold defaultstate="collapsed" desc="Helper methods for add()">
    private boolean verifyInput(T item) {
        return verifyInput(new JComboBox<>(new Object[]{item}));
    }

    private boolean verifyInput(JComboBox<?> comboBox) {
        return optionsComboBox.getInputVerifier().verify(comboBox);
    }

    @SuppressWarnings("unchecked")
    private T getSelectedOption() {
        return (T) optionsComboBox.getSelectedItem();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Helper methods for add() setItems()">
    private void addItem(T item) {
        addToComboBox(item);
        addPanel(createPanel(item));
    }

    private void addToComboBox(T obj) {
        if (model.getIndexOf(obj) < 0)
            model.addElement(obj);
    }

    private JItemPanel<T> createPanel(T obj) {
        ListPanelMediator meditor = new ListPanelMediator(contentPanel);
        RemoveItemAction action = new RemoveItemAction(meditor, REMOVE);
        JItemPanel<T> panel = new JItemPanel<>(obj, action);
        meditor.set(panel);
        return panel;
    }

    private void addPanel(JItemPanel<T> panel) {
        contentPanel.add(panel);
        contentPanel.validate();
    }
    //</editor-fold>
}
