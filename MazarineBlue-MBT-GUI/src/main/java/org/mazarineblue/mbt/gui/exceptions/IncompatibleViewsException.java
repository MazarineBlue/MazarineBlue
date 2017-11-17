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
package org.mazarineblue.mbt.gui.exceptions;

import java.util.Collection;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.utililities.ArgumentList;

/**
 * A {@code SourceStateRequiredException} is a {@link Transition} when a pair
 * of states (source + destination) was found, during validation, that didn't
 * share at least one view. When a source and destionation doesn't share a
 * view then it may be hidden from view. Validation usually takes place when a
 * {@code Transition} is added to a {@link Model}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class IncompatibleViewsException
        extends ModelBasedException {

    private static final long serialVersionUID = 1L;

    public IncompatibleViewsException(Collection<State> sources, State destination) {
        super(toString(sources) + " -> " + destination);
    }

    private static String toString(Collection<State> states) {
        return "[" + new ArgumentList(states).toString() + "]";
    }
}
