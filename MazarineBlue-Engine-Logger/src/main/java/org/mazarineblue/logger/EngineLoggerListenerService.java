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
package org.mazarineblue.logger;

import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.ChainModifierListenerList;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListenerList;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventdriven.listeners.PublisherListenerList;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.logger.events.AddChainModifierListenerEvent;
import org.mazarineblue.logger.events.AddFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.AddPublisherListenerEvent;
import org.mazarineblue.logger.events.RemoveChainModifierListenerEvent;
import org.mazarineblue.logger.events.RemoveFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.RemovePublisherListenerEvent;

/**
 * A {@code EngineLoggerListenerService} is a {@link Subscriber} that allows
 * another {@code Subscriber} to add and remove listeners to listen to
 * {@link Processor} loggable events, using a {@link Event}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLoggerListenerService
        extends ReflectionSubscriber<Event>
        implements ChainModifierListener, FeedExecutorListener, PublisherListener {

    private final FeedExecutorListenerList feedExecutorListeners = new FeedExecutorListenerList();
    private final PublisherListenerList publisherListener = new PublisherListenerList();
    private final ChainModifierListenerList chainModifiedListener = new ChainModifierListenerList();

    private final WrapperCounter<FeedExecutorListener, FeedExecutorListenerFilter> wrappedFeedExecutorListeners = new WrapperCounter<>();
    private final WrapperCounter<PublisherListener, PublisherListenerFilter> wrappedPublisherListeners = new WrapperCounter<>();
    private final WrapperCounter<ChainModifierListener, ChainModifierListenerFilter> wrappedChainModifierListeners = new WrapperCounter<>();

    @Override
    public void openingFeed(Feed feed) {
        feedExecutorListeners.openingFeed(feed);
    }

    @Override
    public void closingFeed(Feed feed) {
        feedExecutorListeners.closingFeed(feed);
    }

    @Override
    public void startPublishingEvent(Event event) {
        publisherListener.startPublishingEvent(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        publisherListener.exceptionThrown(event, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        publisherListener.endPublishedEvent(event);
    }

    @Override
    public void addedLink(Subscriber<Event> link) {
        chainModifiedListener.addedLink(link);
    }

    @Override
    public void addedLink(Subscriber<Event> link, Subscriber<Event> after) {
        chainModifiedListener.addedLink(link, after);
    }

    @Override
    public void removedLink(Subscriber<Event> link) {
        chainModifiedListener.removedLink(link);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SubscribeEvent
     */
    @EventHandler
    public void eventHandler(AddFeedExecutorListenerEvent event) {
        FeedExecutorListener listener = event.getListener();
        feedExecutorListeners.addListener(
                wrappedFeedExecutorListeners.add(listener, new FeedExecutorListenerFilter(listener)));
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     */
    @EventHandler
    public void eventHandler(RemoveFeedExecutorListenerEvent event) {
        feedExecutorListeners.removeListener(wrappedFeedExecutorListeners.remove(event.getListener()));
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     */
    @EventHandler
    public void eventHandler(AddPublisherListenerEvent event) {
        PublisherListener listener = event.getListener();
        publisherListener.addListener(
                wrappedPublisherListeners.add(listener,
                                              new PublisherListenerFilter(listener,
                                                                          AddPublisherListenerEvent.class,
                                                                          RemovePublisherListenerEvent.class)));
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SubscribeEvent
     */
    @EventHandler
    public void eventHandler(RemovePublisherListenerEvent event) {
        publisherListener.removeListener(wrappedPublisherListeners.remove(event.getListener()));
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SubscribeEvent
     */
    @EventHandler
    public void eventHandler(AddChainModifierListenerEvent event) {
        ChainModifierListener listener = event.getListener();
        chainModifiedListener.addListener(
                wrappedChainModifierListeners.add(listener, new ChainModifierListenerFilter(listener)));
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SubscribeEvent
     */
    @EventHandler
    public void eventHandler(RemoveChainModifierListenerEvent event) {
        chainModifiedListener.removeListener(wrappedChainModifierListeners.remove(event.getListener()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
