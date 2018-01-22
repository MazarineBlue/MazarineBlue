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
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * A {@code FeedExecutor} contains everything required to load, execute and
 * process the execution result.
 *
 * @see DefaultFeedExecutor
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Executor {

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
     * Adds a link to the front of the chain.
     *
     * @param link to add to the chain.
     */
    public void addLink(Subscriber<Event> link);

    /**
     * Subscribes an subscriber to receive events from the event bus.
     *
     * @param subscriber the subscriber to register
     */
    public void addLinkAfterVariableParser(Subscriber<Event> subscriber);

    /**
     * Adds a link right behind the event bus.
     *
     * @param link to add to the chain.
     */
    public void addLinkAfterLibraryRegistry(Subscriber<Event> link);

    /**
     * Sets a listener to listen for chain events.
     *
     * @param listener the listener to set
     */
    public void setChainModifierListener(ChainModifierListener listener);

    /**
     * Sets a listener to listen for feed executing events.
     *
     * @param listener the listener to set
     */
    public void setFeedExecutorListener(FeedExecutorListener listener);

    /**
     * Sets a listener to listen for publisher events.
     *
     * @param listener the listener to set
     */
    public void setPublisherListener(PublisherListener listener);
}
