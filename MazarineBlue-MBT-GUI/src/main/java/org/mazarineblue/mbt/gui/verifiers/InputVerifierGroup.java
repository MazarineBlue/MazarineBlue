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

import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 * A {@code InputVerifierGroup} is a {@code InputVerifier} that verifies the
 * input by calling other {@code InputVerifier}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InputVerifierGroup
        extends SerializableInputVerifier {

    private static final long serialVersionUID = 1L;

    private final SerializableInputVerifier[] verifiers;

    /**
     * Constructs a {@code Input InputVerifier} that calls the specified
     * {@code InputVerifier} when verifying a input.
     *
     * @param verifiers the {@code InputVerifier} to call.
     */
    public InputVerifierGroup(SerializableInputVerifier... verifiers) {
        this.verifiers = verifiers;
    }

    @Override
    public boolean verify(JComponent input) {
        for (InputVerifier verifier : verifiers)
            if (!verifier.verify(input))
                return false;
        return true;
    }
}
