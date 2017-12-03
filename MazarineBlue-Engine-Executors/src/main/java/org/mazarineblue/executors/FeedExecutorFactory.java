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

import java.util.function.Supplier;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.executors.links.DefaulExecutorFactoryLink;
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
    public static FeedExecutorFactory newInstance(FeedExecutorBuilder builder) {
        DefaultFeedExecutorFactory factory = new DefaultFeedExecutorFactory(builder);
        factory.addLink(() -> new DefaulExecutorFactoryLink(factory));
        return factory;

    }

    /**
     * Creates a {@link FeedExecutor} with a fresh environment.
     *
     * @return the created {@code FeedExecutor}.
     */
    public FeedExecutor create();

    /**
     * Adds a {@code SubscriberFactory}, which creates an {@code Subscriber}
     * and adds it to the {@link Interpreter}, when the
     * {@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create}
     *                 method is called.
     */
    public void addSubscriber(SubscriberFactory supplier);

    /**
     * Adds a {@code LinkFactory}, which creates an {@code Link} and adds it to
     * the {@link Interpreter} before the event bus link, when the
     * {@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create} method is
     *                 called.
     */
    public void addLinkAfterEventBus(Supplier<Link> supplier);

    /**
     * Adds a {@code LinkFactory}, which creates an {@code Link} and adds it in
     * the front of the chain of the {@link Interpreter} before the
     * EventBus-link, when the{@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create} method is
     *                 called.
     */
    public void addLink(Supplier<Link> supplier);

    /**
     * Sets a listener to listen for chain events, when the {@link Interpreter}
     * is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setChainModifierListener(Supplier<ChainModifierListener> listener);

    /**
     * Sets a listener to listen for publisher events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param supplier the listener to set
     */
    public void setPublisherListener(Supplier<PublisherListener> supplier);

    /**
     * Sets a listener to listen for feed executing events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param supplier the listener to set
     */
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier);

    /**
     * Sets a listener to listen for interpreter events, when the
     * {@link Interpreter} is created, using the specified factory.
     *
     * @param supplier the listener to set
     */
    public void setInterpreterListener(Supplier<InterpreterListener> supplier);
}
