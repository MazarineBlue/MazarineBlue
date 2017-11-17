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

/**
 * A {@code UniqueListitemMatcher} is a {@code ListItemMatcher} that checks if
 * the given collection does not contain the given object.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class UniqueListItemMatcher<T>
        implements ListItemMatcher<T> {

    private static final long serialVersionUID = 1L;

    private final ValidationLabelSwitcher switcher;

    /**
     * Constructs a {@code UniqueListitemMatcher} that shows its result using
     * the specified label switcher, depending on weather or not the given
     * object is in the given collection.
     *
     * @param switcher the switch control over the validation label.
     */
    public UniqueListItemMatcher(ValidationLabelSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public boolean match(Collection<T> collection, Object selected) {
        boolean nonMatch = collection.stream().noneMatch(selected::equals);
        switcher.setSucces(nonMatch);
        return nonMatch;
    }
}
