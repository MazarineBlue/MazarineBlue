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

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Link;

/**
 * A {@code InterpreterListenerAdapter} is a {@link InterpreterListener} that
 * is an adapter to a {@link FeedExecutorListener} and a {@code ChainModifierListener}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InterpreterListenerAdapter
        implements InterpreterListener {

    private final FeedExecutorListener feedExecuteListener;
    private final ChainModifierListener chainModifiedListener;

    public InterpreterListenerAdapter(FeedExecutorListener feedExecuteListener,
                                      ChainModifierListener chainModifiedListener) {
        this.feedExecuteListener = feedExecuteListener;
        this.chainModifiedListener = chainModifiedListener;
    }

    @Override
    public String toString() {
        return "feed={" + feedExecuteListener + "}, chain={" + chainModifiedListener + '}';
    }

    @Override
    public void openingFeed(Feed feed) {
        feedExecuteListener.openingFeed(feed);
    }

    @Override
    public void closingFeed(Feed feed) {
        feedExecuteListener.closingFeed(feed);
    }

    @Override
    public void startPublishingEvent(Event event) {
        feedExecuteListener.startPublishingEvent(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        feedExecuteListener.exceptionThrown(event, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        feedExecuteListener.endPublishedEvent(event);
    }

    @Override
    public void addedLink(Link link) {
        chainModifiedListener.addedLink(link);
    }

    @Override
    public void addedLink(Link link, Link after) {
        chainModifiedListener.addedLink(link, after);
    }

    @Override
    public void removedLink(Link link) {
        chainModifiedListener.removedLink(link);
    }

    protected FeedExecutorListener getFeedExecutorListener() {
        return feedExecuteListener;
    }

    protected ChainModifierListener getChainModifierListener() {
        return chainModifiedListener;
    }
}
