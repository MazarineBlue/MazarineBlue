/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.keyworddriven.links;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.exceptions.ChainModificationException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Chain {

    private final Deque<Link> chain;
    private Link insertAfter = null;

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public Chain() {
        chain = new ArrayDeque<>();
    }

    public Chain(Link... links) {
        chain = new ArrayDeque<>(links.length);
        for (Link link : links)
            chain.add(link);
    }

    /**
     * Passes the event to the first link, then the second link and so forth.
     * This method returns when there are no more links or when the event is
     * consumed.
     *
     * @param event the event to publish
     */
    public void publish(Event event) {
        try {
            for (Link link : chain)
                if (event.isConsumed() == false)
                    link.eventHandler(event);
        } catch (ConcurrentModificationException ex) {
            if (event.isConsumed() == false)
                throw new ChainModificationException(ex);
        }
    }

    /**
     * Instructs insert(Link) to insert links after the specified link.
     *
     * @param link if null then add links at the beginning of the chain.
     */
    public void insertNewLinksAfter(Link link) {
        insertAfter = link;
    }

    /**
     * Inserts a link to the chain.
     *
     * @param link the link to insert in from of the chain.
     */
    public void insert(Link link) {
        if (insertAfter == null)
            chain.addFirst(link);
        else
            insert(link, insertAfter);
    }

    /**
     * Inserts a link to the front of the chain.
     *
     * @param link the link to insert in from of the chain.
     */
    public void insertFirst(Link link) {
        chain.addFirst(link);
    }

    /**
     * Inserts a link to the front of the chain.
     *
     * @param link        the link to insert in the chain.
     * @param insertAfter the link to insert After.
     */
    public void insert(Link link, Link insertAfter) {
        Deque<Link> tmp = new ArrayDeque<>();
        if (insertAfter != null) {
            while (chain.peek().equals(insertAfter) == false)
                tmp.addLast(chain.removeFirst());
            tmp.addLast(chain.removeFirst());
        }
        chain.addFirst(link);
        while (tmp.isEmpty() == false)
            chain.addFirst(tmp.removeLast());
    }

    /**
     * Removes a link from the chain.
     *
     * @param link the link to remove from the chain.
     * @return true when the link was removed.
     */
    public boolean remove(Link link) {
        return chain.remove(link);
    }

    public int size() {
        return chain.size();
    }
}
