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
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.executors.subscribers.DefaulExecutorFactorySubscriber;

/**
 * A {@code FeedExecutorFactory} creates {@link Executor}. Its purpose is
 * to create fresh environment.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface ExecutorFactory {

    /**
     * Creates a {@code FeedExecutorFactory}, which is setup using the specified
     * builder.
     *
     * @param builder the builder used to setup this factory.
     * @return the default {@code FeedExecutorFactory} instance.
     */
    public static ExecutorFactory newInstance(ExecutorBuilder builder) {
        DefaultExecutorFactory factory = new DefaultExecutorFactory(builder);
        factory.addLink(() -> new DefaulExecutorFactorySubscriber(factory));
        return factory;

    }

    /**
     * Creates a {@link Executor} with a fresh environment.
     *
     * @return the created {@code FeedExecutor}.
     */
    public Executor create();

    /**
     * Adds a {@code LinkFactory}, which creates an {@code Subscriber} and adds
     * it in the front of the chain of the {@link Processor} before the
     * EventNotifier-link, when the{@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create} method is
     *                 called.
     */
    public void addLink(Supplier<Subscriber<Event>> supplier);

    /**
     * Adds a {@code SubscriberFactory}, which creates an {@code Subscriber}
     * and adds it to the {@link Processor}, when the
     * {@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create}
     *                 method is called.
     */
    public void addLinkAfterVariableParser(Supplier<Subscriber<Event>> supplier);

    /**
     * Adds a {@code LinkFactory}, which creates an {@code Subscriber} and adds
     * it to the {@link Processor} before the event bus link, when the
     * {@link #create() create} method is called.
     *
     * @param supplier the factory to use when the {@code create} method is
     *                 called.
     */
    public void addLinkAfterEventNotifier(Supplier<Subscriber<Event>> supplier);

    /**
     * Sets a listener to listen for chain events, when the {@link Processor}
     * is created, using the specified factory.
     *
     * @param listener the listener to set
     */
    public void setChainModifierListener(Supplier<ChainModifierListener> listener);

    /**
     * Sets a listener to listen for feed executing events, when the
     * {@link Processor} is created, using the specified factory.
     *
     * @param supplier the listener to set
     */
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier);

    /**
     * Sets a listener to listen for publisher events, when the
     * {@link Processor} is created, using the specified factory.
     *
     * @param supplier the listener to set
     */
    public void setPublisherListener(Supplier<PublisherListener> supplier);
}
