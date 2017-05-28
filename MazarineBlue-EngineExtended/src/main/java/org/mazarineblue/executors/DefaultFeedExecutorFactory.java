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
import org.mazarineblue.executors.listeners.ChainModifierListenerFactory;
import org.mazarineblue.executors.listeners.FeedExecutorListenerFactory;
import org.mazarineblue.executors.listeners.InterpreterListenerFactory;
import org.mazarineblue.executors.listeners.LinkFactory;
import org.mazarineblue.executors.listeners.PublisherListenerFactory;
import org.mazarineblue.executors.listeners.SubscriberFactory;

class DefaultFeedExecutorFactory
        implements FeedExecutorFactory {

    private final FeedExecutorBuilder builder;
    private final List<SubscriberFactory> subscribers = new ArrayList<>();
    private final List<LinkFactory> linksAfterEventBus = new ArrayList<>();
    private final List<LinkFactory> links = new ArrayList<>();
    private final List<FeedExecutorListenerFactory> feedExecutorListeners = new ArrayList<>();
    private final List<InterpreterListenerFactory> interpreterListeners = new ArrayList<>();
    private final List<ChainModifierListenerFactory> chainModifierListeners = new ArrayList<>();
    private final List<PublisherListenerFactory> publisherListeners = new ArrayList<>();

    DefaultFeedExecutorFactory(FeedExecutorBuilder builder) {
        this.builder = new FeedExecutorBuilder(builder);
    }

    @Override
    public void addSubscriber(SubscriberFactory factory) {
        subscribers.add(factory);
    }

    @Override
    public void addLinkAfterEventBus(LinkFactory factory) {
        linksAfterEventBus.add(factory);
    }

    @Override
    public void addLink(LinkFactory factory) {
        links.add(factory);
    }

    @Override
    public void setChainModifierListener(ChainModifierListenerFactory factory) {
        chainModifierListeners.add(factory);
    }

    @Override
    public void setPublisherListener(PublisherListenerFactory factory) {
        publisherListeners.add(factory);
    }

    @Override
    public void setFeedExecutorListener(FeedExecutorListenerFactory factory) {
        feedExecutorListeners.add(factory);
    }

    @Override
    public void setInterpreterListener(InterpreterListenerFactory factory) {
        interpreterListeners.add(factory);
    }

    @Override
    public FeedExecutor create() {
        DefaultFeedExecutor feedExecutor = new DefaultFeedExecutor(builder);
        doFirst(feedExecutor);
        doLast(feedExecutor);
        return feedExecutor;
    }

    private void doFirst(DefaultFeedExecutor feedExecutor) {
        feedExecutorListeners.stream().forEach(factory -> feedExecutor.setFeedExecutorListener(factory.create()));
        interpreterListeners.stream().forEach(factory -> feedExecutor.setInterpreterListener(factory.create()));
        chainModifierListeners.stream().forEach(factory -> feedExecutor.setChainModifierListener(factory.create()));
        publisherListeners.stream().forEach(factory -> feedExecutor.setPublisherListener(factory.create()));
    }

    private void doLast(DefaultFeedExecutor feedExecutor) {
        subscribers.stream().forEach(f -> feedExecutor.addSubscriber(f.getEventType(), f.getFilter(), f.getSubscriber()));
        linksAfterEventBus.stream().forEach(factory -> feedExecutor.addLinkAfterEventBus(factory.create()));
        links.stream().forEach(factory -> feedExecutor.addLink(factory.create()));
    }
}
