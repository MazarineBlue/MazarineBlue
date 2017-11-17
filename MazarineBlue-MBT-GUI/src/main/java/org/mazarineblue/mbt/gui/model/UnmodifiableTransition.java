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
import java.util.List;

class UnmodifiableTransition
        extends UnmodifiableModelElement<Transition>
        implements Transition {

    private static final long serialVersionUID = 1L;

    private final Transition adaptee;

    UnmodifiableTransition(Transition adaptee) {
        super(adaptee);
        this.adaptee = adaptee;
    }

    @Override
    public void verify() {
        adaptee.verify();
    }

    @Override
    public Transition copy(Transition transition, StateConvertor convertor) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean containsSourceWithView(String view) {
        return adaptee.containsSourceWithView(view);
    }

    @Override
    public boolean containsDestinationWithView(String view) {
        return adaptee.containsDestinationWithView(view);
    }

    @Override
    public String getGuard() {
        return adaptee.getGuard();
    }

    @Override
    public Transition setGuard(String guard) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBusinessValue() {
        return adaptee.getBusinessValue();
    }

    @Override
    public Transition setBusinessValue(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<State> getSources() {
        return adaptee.getSources();
    }

    @Override
    public Transition setSources(State... states) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transition setSources(Collection<State> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public State getDestination() {
        return adaptee.getDestination();
    }

    @Override
    public Transition setDestination(State state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(State state) {
        return adaptee.contains(state);
    }

    @Override
    public boolean isSource(State state) {
        return adaptee.isSource(state);
    }

    @Override
    public boolean isDestination(State state) {
        return adaptee.isDestination(state);
    }
}
