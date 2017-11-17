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

import java.util.Objects;

class UnmodifiableModelElement<T extends ModelElement>
        implements ModelElement<T> {

    private static final long serialVersionUID = 1L;

    private final ModelElement<T> adaptee;

    UnmodifiableModelElement(ModelElement<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }

    @Override
    public String getName() {
        return adaptee.getName();
    }

    @Override
    public T setAction(String action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAction() {
        return adaptee.getAction();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(adaptee);
    }

    @Override
    public boolean equals(Object obj) {
        return adaptee.equals(obj);
    }
}
