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
package org.mazarineblue.eventdriven;

import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * A {@code Chain} is a set of {@link Subscriber links}.
 * A chain is able to process and consume events, using the links in the chain.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Chain {

    /**
     * Adds a link to the front of the chain.
     *
     * @param link to add to the chain.
     */
    public void addLink(Subscriber<Event> link);

    /**
     * Adds a link behind another link in the chain.
     *
     * @param link  to add to the chain.
     * @param after insert link after this link.
     */
    public void addLink(Subscriber<Event> link, Subscriber<Event> after);

    /**
     * Removes the specified link from the chain.
     *
     * @param link to remove from the chain.
     */
    public void removeLink(Subscriber<Event> link);

    /**
     * Sets a listener to listen for chain events.
     *
     * @param listener the listener to set
     */
    public void setChainModifierListener(ChainModifierListener listener);

    /**
     * Sets a listener to listen for publisher events.
     *
     * @param listener the listener to set
     */
    public void setPublisherListener(PublisherListener listener);
}
