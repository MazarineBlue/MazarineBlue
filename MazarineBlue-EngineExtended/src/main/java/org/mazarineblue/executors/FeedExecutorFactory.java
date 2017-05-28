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

import org.mazarineblue.executors.listeners.ChainModifierListenerFactory;
import org.mazarineblue.executors.listeners.FeedExecutorListenerFactory;
import org.mazarineblue.executors.listeners.InterpreterListenerFactory;
import org.mazarineblue.executors.listeners.LinkFactory;
import org.mazarineblue.executors.listeners.PublisherListenerFactory;
import org.mazarineblue.executors.listeners.SubscriberFactory;

/**
 * A {@code FeedExecutorFactory} creates {@link FeedExecutor}. Its purpose is
 * to create fresh environment.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface FeedExecutorFactory {

    /**
     * Creates a {@code FeedExecutorFactory}, which is setup using the specified
     * builder.
     *
     * @param builder the builder used to setup this factory.
     * @return the default {@code FeedExecutorFactory} instance.
     */
    public static FeedExecutorFactory getDefaultInstance(FeedExecutorBuilder builder) {
        return new DefaultFeedExecutorFactory(builder);
    }

    /**
     * Creates a {@link FeedExecutor} with a fresh environment.
     *
     * @return the created {@code FeedExecutor}.
     */
    public FeedExecutor create();

    public void addSubscriber(SubscriberFactory subscriberFactory);

    public void addLinkAfterEventBus(LinkFactory link);

    /**
     * Adds a link to the front of the chain, when the {@link Interpreter} is
     * created, using the specified factory.
     *
     * @param link to add to the chain.
     */
    public void addLink(LinkFactory link);

    /**
     * Sets a listener to listen for chain events, when the {@link Interpreter}
     * is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setChainModifierListener(ChainModifierListenerFactory listener);

    /**
     * Sets a listener to listen for publisher events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setPublisherListener(PublisherListenerFactory listener);

    /**
     * Sets a listener to listen for feed executing events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setFeedExecutorListener(FeedExecutorListenerFactory listener);

    /**
     * Sets a listener to listen for interpreter events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setInterpreterListener(InterpreterListenerFactory listener);
}
