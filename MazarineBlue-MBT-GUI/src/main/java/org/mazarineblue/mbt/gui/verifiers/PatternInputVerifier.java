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

import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * A {@code PatternInputVerifier} is a {@code InputVerifier} that validates a
 * component and, depending on the result, shows an error message or hide the
 * message label. It fetches the input and convert it to a string and then
 * check if it matches the given regular expression.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PatternInputVerifier
        extends SerializableInputVerifier {

    private static final long serialVersionUID = 1L;

    private final Pattern pattern;
    private final ValidationLabelSwitcher switcher;

    /**
     * Constructs a {@code PatternInputVerifier} that checks components using
     * the specified regular expression and switches a label on or off though
     * {@code ValidationLabelSwitcher}.
     *
     * @param regex    the regular expression to use for the validation.
     * @param switcher the switch control over the validation label.
     */
    public PatternInputVerifier(String regex, ValidationLabelSwitcher switcher) {
        this(Pattern.compile(regex), switcher);
    }

    /**
     * Constructs a {@code PatternInputVerifier} that checks components using
     * the specified regular expression and switches a label on or off though
     * {@code ValidationLabelSwitcher}.
     *
     * @param pattern  the regular expression to use for the validation.
     * @param switcher the switch control over the validation label.
     */
    public PatternInputVerifier(Pattern pattern, ValidationLabelSwitcher switcher) {
        this.pattern = pattern;
        this.switcher = switcher;
    }

    @Override
    public boolean verify(JComponent input) {
        boolean match = pattern.matcher(convert(input)).matches();
        switcher.setSucces(match);
        return match;
    }

    private String convert(JComponent input) {
        if (input instanceof JTextComponent)
            return ((JTextComponent) input).getText();
        if (input instanceof JComboBox)
            return ((JComboBox) input).getSelectedItem().toString();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
