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
package org.mazarineblue.mbt.gui.verifiers;

import java.io.Serializable;
import javax.swing.JLabel;

/**
 * A {@code ValidationLabelSwitcher} has control over a label can can hide it
 * or use it to show an error message.
 */
public class ValidationLabelSwitcher
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ValidationLabel label;
    private final String message;

    /**
     * Constructs a {@code ValidationLabelSwitcher} that has control over the
     * specified label and can set the specified message to it.
     *
     * @param label   the label to show or hide.
     * @param message the message to set to the label.
     */
    public ValidationLabelSwitcher(JLabel label, String message) {
        this(ValidationLabel.getDefault(label), message);
    }

    /**
     * Constructs a {@code ValidationLabelSwitcher} that has control over a
     * label though a the mediator and can set the specified message to it.
     *
     * @param label   the label to show or hide.
     * @param message the message to set to the label.
     */
    public ValidationLabelSwitcher(ValidationLabel label, String message) {
        this.label = label;
        this.message = message;
    }

    /**
     * Hides the controlled label, if true, or shows the preset message though
     * the controlled label, if false.
     */
    void setSucces(boolean flag) {
        if (flag)
            label.hideMessage();
        else
            label.setMessage(message);
    }
}
