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

import static java.util.Arrays.asList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.mbt.gui.exceptions.InvalidContentException;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.verifiers.PatternInputVerifier;
import org.mazarineblue.mbt.gui.verifiers.ValidationLabelSwitcher;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

public class JListPanelTest {

    private static final String CANT_BE_BLANK = "can't be blank";

    private JListPanel<String> panel;
    private JComboBox<String> optionsComboBox;
    private JButton addButton;
    private JLabel validationLabel;
    private JPanel contentPanel;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        panel = new JListPanel<>();
        panel.setEditable(true);
        optionsComboBox = fetchChildNamed(panel, "optionsComboBox", JComboBox.class);
        addButton = fetchChildNamed(panel, "addButton", JButton.class);
        validationLabel = fetchChildNamed(panel, "validationLabel", JLabel.class);
        panel.setVerifier(new PatternInputVerifier("^.+$", new ValidationLabelSwitcher(validationLabel, CANT_BE_BLANK)));
        contentPanel = fetchChildNamed(panel, "contentPanel", JPanel.class);
    }

    @After
    public void teardown() {
        panel = null;
    }

    @Test
    public void addEmptyString() {
        optionsComboBox.setSelectedItem("");
        addButton.doClick();

        assertEquals(0, panel.getContentCount());
        assertEquals(0, panel.getContentComponents().length);
        assertEquals(0, optionsComboBox.getItemCount());
        assertEquals(0, contentPanel.getComponentCount());
        assertTrue(validationLabel.isVisible());
        assertEquals(CANT_BE_BLANK, validationLabel.getText());
    }

    @Test
    public void addOnce() {
        optionsComboBox.setSelectedItem("Test view");
        addButton.doClick();

        assertEquals(1, panel.getContentCount());
        assertEquals(1, panel.getContentComponents().length);
        assertEquals(1, optionsComboBox.getItemCount());
        assertEquals(1, contentPanel.getComponentCount());

        JLabel label = fetchChildNamed(contentPanel.getComponent(0), "itemLabel", JLabel.class);
        assertEquals("Test view", optionsComboBox.getItemAt(0));
        assertEquals("Test view", label.getText());
        assertEquals("Test view", panel.getItem(0));
        assertFalse(validationLabel.isVisible());
    }

    @Test
    public void addTwice() {
        optionsComboBox.setSelectedItem("Test view");
        addButton.doClick();
        addButton.doClick();

        assertEquals(2, panel.getContentCount());
        assertEquals(2, panel.getContentComponents().length);
        assertEquals(1, optionsComboBox.getItemCount());
        assertEquals(2, contentPanel.getComponentCount());

        JLabel label0 = fetchChildNamed(contentPanel.getComponent(0), "itemLabel", JLabel.class);
        JLabel label1 = fetchChildNamed(contentPanel.getComponent(1), "itemLabel", JLabel.class);
        assertEquals("Test view", optionsComboBox.getItemAt(0));
        assertEquals("Test view", label0.getText());
        assertEquals("Test view", label1.getText());
        assertEquals("Test view", panel.getItem(0));
        assertEquals("Test view", panel.getItem(1));
        assertFalse(validationLabel.isVisible());
    }

    @Test
    public void addAndRemove() {
        optionsComboBox.setSelectedItem("Test view");
        addButton.doClick();

        JButton button = fetchChildNamed(contentPanel.getComponent(0), "removeButton", JButton.class);
        button.doClick();

        assertEquals(0, panel.getContentCount());
        assertEquals(0, panel.getContentComponents().length);
        assertEquals(1, optionsComboBox.getItemCount());
        assertEquals(0, contentPanel.getComponentCount());
        assertEquals("Test view", optionsComboBox.getItemAt(0));
        assertFalse(validationLabel.isVisible());
    }

    @Test(expected = InvalidContentException.class)
    public void setContent_InvalidContent() {
        validationLabel.setText(CANT_BE_BLANK);
        panel.setItems(asList(""));
    }

    @Test
    public void setContent_ValidContent() {
        validationLabel.setText(CANT_BE_BLANK);
        panel.setItems(asList("View A", "View B"));
        assertEquals(-1, optionsComboBox.getSelectedIndex());
        assertEquals(null, optionsComboBox.getSelectedItem());
        assertEquals(2, panel.getContentCount());
        assertEquals(2, panel.getContentComponents().length);
        assertEquals(2, optionsComboBox.getItemCount());
        assertEquals(2, contentPanel.getComponentCount());
        assertEquals("View A", optionsComboBox.getItemAt(0));
        assertEquals("View B", optionsComboBox.getItemAt(1));
        assertFalse(validationLabel.isVisible());
    }
}
