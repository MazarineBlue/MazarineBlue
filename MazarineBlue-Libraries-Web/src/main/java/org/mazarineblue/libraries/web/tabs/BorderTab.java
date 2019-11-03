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

/**
 * A fake tab to act as the border node.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class BorderTab
        extends Tab {

    private Tab previous = null;
    private Tab next = null;

    BorderTab() {
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String handle() {
        return "";
    }

    @Override
    boolean hasPrevious() {
        return previous != null;
    }

    @Override
    Tab previous() {
        return previous != null ? previous : this;
    }

    @Override
    Tab setPreviousTab(Tab previous) {
        this.previous = previous;
        return this;
    }

    @Override
    boolean hasNext() {
        return next != null;
    }

    @Override
    Tab next() {
        return next != null ? next : this;
    }

    @Override
    Tab setNextTab(Tab next) {
        this.next = next;
        return this;
    }
}
