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
import java.util.Collection;
import java.util.function.Supplier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * A {@code CollectionInputVerifier} is a {@code InputVerifier} that checks
 * an object, that it retrieves from the input component, against a collection,
 * that it retrieves though a {@code Supplier}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CollectionInputVerifier<T extends Serializable>
        extends SerializableInputVerifier {

    private static final long serialVersionUID = 1L;

    private final CollectionObjectMatcher<T> matcher;
    private Supplier<Collection<T>> fetchItems;

    public CollectionInputVerifier(Supplier<Collection<T>> fetchItems, CollectionObjectMatcher<T> matcher) {
        this.matcher = matcher;
        this.fetchItems = fetchItems;
    }

    @Override
    public boolean verify(JComponent input) {
        Object selected = input instanceof JComboBox
                ? ((JComboBox) input).getSelectedItem()
                : ((JTextComponent) input).getText();
        return matcher.match(fetchItems.get(), selected);
    }
}
