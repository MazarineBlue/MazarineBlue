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

import org.mazarineblue.eventdriven.exceptions.NullLinkException;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.StackEventService;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class ChainImpl
        implements Chain {

    private final StackEventService<Event> links;
    private ChainModifierListener chainModifiedListener;
    private PublisherListener publisherListener;

    ChainImpl() {
        links = new StackEventService<>();
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
    public void addLink(Subscriber<Event> link) {
        if (link == null)
            throw new NullLinkException();
        links.subscribe(link);
        chainModifiedListener.addedLink(link);
    }

    @Override
    public void addLink(Subscriber<Event> link, Subscriber<Event> after) {
        if (link == null)
            throw new NullLinkException();
        links.subscribeAfter(link, after);
        chainModifiedListener.addedLink(link, after);
    }

    @Override
    public void removeLink(Subscriber<Event> link) {
        links.unsubscribe(link);
        chainModifiedListener.removedLink(link);
    }

    /**
     * Publish a new event on the bus.
     *
     * @param event the event to publish.
     */
    void publish(Event event) {
        try {
            publisherListener.startPublishingEvent(event);
            links.publish(event);
        } catch (RuntimeException ex) {
            publisherListener.exceptionThrown(event, ex);
            throw ex;
        } finally {
            publisherListener.endPublishedEvent(event);
        }
    }
}
