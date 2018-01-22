/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventdriven.listeners;

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * A {@code ChainModifierListener} is a listener that, when the chain was
 * modified, reports the links that was added or removed from the chain.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface ChainModifierListener {

    public static ChainModifierListener getDummy() {
        return new DummyChainModifierListener();
    }

    /**
     * Called when a link was added to the chain.
     *
     * @param link added link
     */
    public void addedLink(Subscriber<Event> link);

    /**
     * Called when a link was added to the chain.
     *
     * @param link  added Subscriber
     * @param after the link it was added after
     */
    public void addedLink(Subscriber<Event> link, Subscriber<Event> after);

    /**
     * Called when a link was removed from the chain.
     *
     * @param link the removed Subscriber
     */
    public void removedLink(Subscriber<Event> link);
}
