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
package org.mazarineblue.mazarineblue.libraries.web;

class MiddleTab
        extends Tab {

    private final String name;
    private final String handle;
    private Tab previous;
    private Tab next;

    @SuppressWarnings("LeakingThisInConstructor")
    MiddleTab(String name, String handle) {
        this.name = name;
        this.handle = handle;
        previous = new BorderTab().setNextTab(this);
        next = new BorderTab().setPreviousTab(this);
    }

    @Override
    String getName() {
        return name;
    }

    @Override
    String getHandle() {
        return handle;
    }

    @Override
    boolean hasPrevious() {
        return !BorderTab.class.isAssignableFrom(previous.getClass());
    }

    @Override
    Tab previousTab() {
        return previous;
    }

    @Override
    Tab setPreviousTab(Tab tab) {
        this.previous = tab;
        return this;
    }

    @Override
    boolean hasNext() {
        return !BorderTab.class.isAssignableFrom(next.getClass());
    }

    @Override
    Tab nextTab() {
        return next;
    }

    @Override
    Tab setNextTab(Tab next) {
        this.next = next;
        return this;
    }
}
