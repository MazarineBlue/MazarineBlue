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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.executors.listeners.SubscriberFactory;

class DefaultFeedExecutorFactory
        implements FeedExecutorFactory {

    private final FeedExecutorBuilder builder;
    private final List<SubscriberFactory> subscribers = new ArrayList<>();
    private final List<Supplier<Link>> linksAfterEventBus = new ArrayList<>();
    private final List<Supplier<Link>> links = new ArrayList<>();
    private final List<Supplier<FeedExecutorListener>> feedExecutorListeners = new ArrayList<>();
    private final List<Supplier<InterpreterListener>> interpreterListeners = new ArrayList<>();
    private final List<Supplier<ChainModifierListener>> chainModifierListeners = new ArrayList<>();
    private final List<Supplier<PublisherListener>> publisherListeners = new ArrayList<>();

    DefaultFeedExecutorFactory(FeedExecutorBuilder builder) {
        this.builder = new FeedExecutorBuilder(builder);
    }

    @Override
    public void addSubscriber(SubscriberFactory factory) {
        subscribers.add(factory);
    }

    @Override
    public void addLinkAfterEventBus(Supplier<Link> factory) {
        linksAfterEventBus.add(factory);
    }

    @Override
    public void addLink(Supplier<Link> factory) {
        links.add(factory);
    }

    @Override
    public void setChainModifierListener(Supplier<ChainModifierListener> supplier) {
        chainModifierListeners.add(supplier);
    }

    @Override
    public void setPublisherListener(Supplier<PublisherListener> supplier) {
        publisherListeners.add(supplier);
    }

    @Override
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier) {
        feedExecutorListeners.add(supplier);
    }

    @Override
    public void setInterpreterListener(Supplier<InterpreterListener> supplier) {
        interpreterListeners.add(supplier);
    }

    @Override
    public FeedExecutor create() {
        DefaultFeedExecutor feedExecutor = new DefaultFeedExecutor(builder);
        doFirst(feedExecutor);
        doLast(feedExecutor);
        return feedExecutor;
    }

    private void doFirst(DefaultFeedExecutor feedExecutor) {
        feedExecutorListeners.stream().forEach(supplier -> feedExecutor.setFeedExecutorListener(supplier.get()));
        interpreterListeners.stream().forEach(supplier -> feedExecutor.setInterpreterListener(supplier.get()));
        chainModifierListeners.stream().forEach(supplier -> feedExecutor.setChainModifierListener(supplier.get()));
        publisherListeners.stream().forEach(supplier -> feedExecutor.setPublisherListener(supplier.get()));
    }

    private void doLast(DefaultFeedExecutor feedExecutor) {
        subscribers.stream().forEach(f -> feedExecutor.addSubscriber(f.getEventType(), f.getFilter(), f.getSubscriber()));
        linksAfterEventBus.stream().forEach(supplier -> feedExecutor.addLinkAfterEventBus(supplier.get()));
        links.stream().forEach(supplier -> feedExecutor.addLink(supplier.get()));
    }
}
