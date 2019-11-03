/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web.tabs;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Represents a real browser tab.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class RealTab
        extends Tab {

    private final String name;
    private final String handle;
    private WeakReference<Tab> previous;
    private WeakReference<Tab> next;

    @SuppressWarnings("LeakingThisInConstructor")
    RealTab(String name, String handle) {
        this.name = name;
        this.handle = handle;
        previous = new WeakReference<>(new BorderTab().setNextTab(this));
        next = new WeakReference<>(new BorderTab().setPreviousTab(this));
    }

    @Override
    public String toString() {
        return "name='" + name + "', previous='" + previous.get().name() + "', next='" + next.get().name() + "'";
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String handle() {
        return handle;
    }

    @Override
    public int hashCode() {
        return 29 * 29 * 7
                + 29 * Objects.hashCode(this.name)
                + Objects.hashCode(this.handle);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.name, ((RealTab) obj).name)
                && Objects.equals(this.handle, ((RealTab) obj).handle);
    }

    @Override
    boolean hasPrevious() {
        return !BorderTab.class.isAssignableFrom(previous.get().getClass());
    }

    @Override
    Tab previous() {
        return previous.get();
    }

    @Override
    Tab setPreviousTab(Tab tab) {
        this.previous = new WeakReference<>(tab);
        return this;
    }

    @Override
    boolean hasNext() {
        return !BorderTab.class.isAssignableFrom(next.get().getClass());
    }

    @Override
    Tab next() {
        return next.get();
    }

    @Override
    Tab setNextTab(Tab next) {
        this.next = new WeakReference<>(next);
        return this;
    }
}
