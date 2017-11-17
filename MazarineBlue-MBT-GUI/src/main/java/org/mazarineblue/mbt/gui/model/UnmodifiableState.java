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
package org.mazarineblue.mbt.gui.model;

import java.util.Collection;
import java.util.Set;

class UnmodifiableState
        extends UnmodifiableModelElement<State>
        implements State {

    private static final long serialVersionUID = 1L;

    private final State adaptee;

    UnmodifiableState(State adaptee) {
        super(adaptee);
        this.adaptee = adaptee;
    }

    @Override
    public void verify() {
        adaptee.verify();
    }

    @Override
    public State copy(State newState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public State addViews(String... views) {
        throw new UnsupportedOperationException();
    }

    @Override
    public State addViews(Collection<String> views) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getViews() {
        return adaptee.getViews();
    }

    @Override
    public boolean containsView(String view) {
        return adaptee.containsView(view);
    }
}
