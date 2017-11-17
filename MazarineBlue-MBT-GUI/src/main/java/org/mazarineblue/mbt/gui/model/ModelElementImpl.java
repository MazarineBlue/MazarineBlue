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

class ModelElementImpl<T extends ModelElement>
        implements ModelElement<T> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String action;

    ModelElementImpl(String name) {
        this.name = name;
    }

    ModelElementImpl(ModelElement<T> e) {
        name = e.getName();
        action = e.getAction();
    }

    void copy(ModelElement<T> e) {
        name = e.getName();
        action = e.getAction();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setAction(String action) {
        this.action = action;
        return (T) this;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public int hashCode() {
        return 7 * 41 * 41
                + 41 * Objects.hashCode(name)
                + Objects.hashCode(action);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof ModelElement
                && Objects.equals(name, ((ModelElement<?>) obj).getName())
                && Objects.equals(action, ((ModelElement<?>) obj).getAction());
    }
}
