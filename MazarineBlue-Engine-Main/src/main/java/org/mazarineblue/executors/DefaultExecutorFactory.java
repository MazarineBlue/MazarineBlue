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
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

class DefaultExecutorFactory
        implements ExecutorFactory {

    private final ExecutorBuilder builder;
    private final List<Supplier<Subscriber<Event>>> links = new ArrayList<>();
    private final List<Supplier<Subscriber<Event>>> linkAfterVariableParser = new ArrayList<>();
    private final List<Supplier<Subscriber<Event>>> linksAfterLibraryRegistry = new ArrayList<>();
    private Supplier<FeedExecutorListener> feedExecutorListener;
    private Supplier<ChainModifierListener> chainModifierListener;
    private Supplier<PublisherListener> publisherListener;

    DefaultExecutorFactory(ExecutorBuilder builder) {
        this.builder = new ExecutorBuilder(builder);
    }

    @Override
    public void addLink(Supplier<Subscriber<Event>> factory) {
        links.add(factory);
    }

    @Override
    public void addLinkAfterVariableParser(Supplier<Subscriber<Event>> factory) {
        linkAfterVariableParser.add(factory);
    }

    @Override
    public void addLinkAfterEventNotifier(Supplier<Subscriber<Event>> factory) {
        linksAfterLibraryRegistry.add(factory);
    }

    @Override
    public void setChainModifierListener(Supplier<ChainModifierListener> supplier) {
        chainModifierListener = supplier;
    }

    @Override
    public void setPublisherListener(Supplier<PublisherListener> supplier) {
        publisherListener = supplier;
    }

    @Override
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier) {
        feedExecutorListener = supplier;
    }

    @Override
    public Executor create() {
        DefaultExecutor feedExecutor = new DefaultExecutor(builder);
        init(feedExecutor);
        return feedExecutor;
    }

    private void init(DefaultExecutor feedExecutor) {
        links.stream().forEach(supplier -> feedExecutor.addLink(supplier.get()));
        linkAfterVariableParser.stream().forEach(supplier -> feedExecutor.addLinkAfterVariableParser(supplier.get()));
        linksAfterLibraryRegistry.stream().forEach(supplier -> feedExecutor.addLinkAfterLibraryRegistry(supplier.get()));
        if (chainModifierListener != null)
            feedExecutor.setChainModifierListener(chainModifierListener.get());
        if (feedExecutorListener != null)
            feedExecutor.setFeedExecutorListener(feedExecutorListener.get());
        if (publisherListener != null)
            feedExecutor.setPublisherListener(publisherListener.get());
    }
}
