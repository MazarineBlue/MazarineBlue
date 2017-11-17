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

import java.util.Collection;
import java.util.function.Supplier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.mazarineblue.mbt.gui.model.State;

/**
 * A {@code StatesShareViewInputVerifier} is a {@code InputVerifier} that
 * validates a {@link State} shares a view with a group of other
 * {@code State states}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StatesShareViewInputVerifier
        extends SerializableInputVerifier {

    private static final long serialVersionUID = 1L;

    private final transient Supplier<Collection<State>> viewsFetcher;
    private final ValidationLabelSwitcher switcher;

    /**
     * Constructs a {@code StatesShareViewInputVerifier} that fetches a
     * collection of {@link State states} to use in verification and switches a
     * label on or off though {@code ValidationLabelSwitcher}.
     *
     * @param viewsFetcher the supplier of the collection of {@code states}.
     * @param switcher     the switch control over the validation label.
     */
    public StatesShareViewInputVerifier(Supplier<Collection<State>> viewsFetcher, ValidationLabelSwitcher switcher) {
        this.viewsFetcher = viewsFetcher;
        this.switcher = switcher;
    }

    @Override
    public boolean verify(JComponent input) {
        boolean flag = isAllowedToAddState(input);
        switcher.setSucces(flag);
        return flag;
    }

    private boolean isAllowedToAddState(JComponent input) {
        return StatesShareViewChecker.doStatePairsShareAView(getInputState(input), viewsFetcher);
    }

    @SuppressWarnings("unchecked")
    private static State getInputState(JComponent input) {
        JComboBox<State> inputComboBox = (JComboBox<State>) input;
        return (State) inputComboBox.getSelectedItem();
    }
}
