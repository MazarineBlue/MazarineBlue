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

import java.util.ArrayDeque;
import java.util.Deque;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.exceptions.LinkNotInChainException;
import org.mazarineblue.eventdriven.exceptions.NullLinkException;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class ChainImpl
        implements Chain {

    private final Deque<Link> stack;
    private ChainModifierListener chainModifiedListener;
    private PublisherListener publisherListener;

    ChainImpl() {
        stack = new ArrayDeque<>(4);
        chainModifiedListener = ChainModifierListener.getDummy();
        publisherListener = PublisherListener.getDummy();
    }

    @Override
    public void setChainModifierListener(ChainModifierListener listener) {
        this.chainModifiedListener = listener;
    }

    @Override
    public void setPublisherListener(PublisherListener publisherListener) {
        this.publisherListener = publisherListener;
    }

    @Override
    public int countLinks() {
        return stack.size();
    }

    @Override
    public void addLink(Link link) {
        if (link == null)
            throw new NullLinkException();
        stack.addFirst(link);
        chainModifiedListener.addedLink(link);
    }

    @Override
    public void addLink(Link link, Link after) {
        if (link == null)
            throw new NullLinkException();
        if (after == null)
            throw new LinkNotInChainException();
        Deque<Link> tmp = removeTop(after);
        stack.addFirst(link);
        addTop(tmp);
        chainModifiedListener.addedLink(link, after);
    }

    @Override
    public void removeLink(Link link) {
        if (link == null)
            throw new NullLinkException();
        Deque<Link> tmp = removeTop(link);
        tmp.removeFirst();
        addTop(tmp);
        chainModifiedListener.removedLink(link);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper method for addLink(Link, Link) and removeLink(Link)">
    private Deque<Link> removeTop(Link seek) {
        Deque<Link> tmp = new ArrayDeque<>(16);
        while (!stack.isEmpty()) {
            moveFirstLink(tmp, stack);
            if (isAtSeekedLink(tmp, seek))
                return tmp;
        }
        throw new LinkNotInChainException();
    }

    private void addTop(Deque<Link> tmp) {
        while (!tmp.isEmpty())
            moveFirstLink(stack, tmp);
    }

    private void moveFirstLink(Deque<Link> dst, Deque<Link> src) {
        dst.addFirst(src.removeFirst());
    }

    private boolean isAtSeekedLink(Deque<Link> tmp, Link link) {
        return tmp.peekFirst().equals(link);
    }
    // </editor-fold>

    Link removeLink() {
        Link link = stack.removeLast();
        chainModifiedListener.removedLink(link);
        return link;
    }

    /**
     * Publish a new event on the bus.
     *
     * @param event the event to publish.
     */
    public void publish(Event event) {
        try {
            publisherListener.startPublishingEvent(event);
            for (Link link : stack) {
                link.eventHandler(event);
                if (event.isConsumed())
                    break;
            }
        } catch (RuntimeException ex) {
            publisherListener.exceptionThrown(event, ex);
            throw ex;
        } finally {
            publisherListener.endPublishedEvent(event);
        }
    }
}
