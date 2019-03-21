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
package org.mazarineblue.mazarineblue.libraries.web.tabs;

/**
 * A tab references a tab in the browser.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class Tab {

    /**
     * Returns the name of this tab.
     *
     * @return the name of this tab
     */
    abstract String getName();

    /**
     * Returns the WebDriver window handle of this tab.
     *
     * @return the WebDriver window handle of this tab.
     */
    public abstract String getHandle();

    /**
     * Determins if this tab has a previous tab.
     *
     * @return {@literal true} if this tab has a previous tab
     */
    abstract boolean hasPrevious();

    /**
     * Returns the previous tab of a {@link BorderTab} if there is no previous
     * tab.
     *
     * @return the previous tab of a {@link BorderTab} if there is no previous
     * tab.
     */
    abstract Tab previousTab();

    /**
     * Sets the previous tab.
     *
     * @param previous the previous tab to set
     * @return the this tab, so you can call {@link #setNextTab(Tab) }
     */
    abstract Tab setPreviousTab(Tab previous);

    /**
     * Determins if this tab has a next tab.
     *
     * @return {@literal true} if this tab has a next tab
     */
    abstract boolean hasNext();

    /**
     * Returns the previous tab of a {@link BorderTab} if there is no next tab.
     *
     * @return the previous tab of a {@link BorderTab} if there is no next tab
     */
    abstract Tab nextTab();

    /**
     * Sets the next tab.
     *
     * @param next the next tab to set
     * @return the this tab, so you can call {@link #setPreviousTab(Tab)}
     */
    abstract Tab setNextTab(Tab next);
}
