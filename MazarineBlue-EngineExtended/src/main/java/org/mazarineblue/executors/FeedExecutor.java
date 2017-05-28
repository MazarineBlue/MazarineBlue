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
package org.mazarineblue.executors;

import java.io.File;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;

/**
 * A {@code FeedExecutor} contains everything required to load, execute and
 * process the result.
 *
 * @see DefaultFeedExecutor
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface FeedExecutor {

    /**
     * Executes the specified feed.
     *
     * @param file the file containing the feed
     */
    default void execute(File file) {
        execute(file, null);
    }

    /**
     * Executes the specified feed.
     *
     * @param file  the file containing the feed
     * @param sheet the sheet that is read
     */
    public void execute(File file, String sheet);

    /**
     * Execute the specified feed.
     *
     * @param feed the feed to execute.
     */
    public void execute(Feed feed);

    /**
     * Test if any error occurred during execution.
     *
     * @return {@code true] if an error occured.
     */
    public boolean containsErrors();

    /**
     * Subscribes an subscriber to receive events from the event bus.
     *
     * @param eventType  the type of events to listen to
     * @param filter     the filter to apply
     * @param subscriber the subscriber to register
     * @return {@code true} op on success
     */
    public void addSubscriber(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber);

    /**
     * Adds a link right behind the event bus.
     *
     * @param link to add to the chain.
     */
    public void addLinkAfterEventBus(Link link);

    /**
     * Adds a link to the front of the chain.
     *
     * @param link to add to the chain.
     */
    public void addLink(Link link);

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

    /**
     * Sets a listener to listen for feed executing events.
     *
     * @param listener the listener to set
     */
    public void setFeedExecutorListener(FeedExecutorListener listener);

    /**
     * Sets a listener to listen for interpreter events.
     *
     * @param listener the listener to set
     */
    public void setInterpreterListener(InterpreterListener listener);
}
