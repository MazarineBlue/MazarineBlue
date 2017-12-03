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
package org.mazarineblue.executors.util;

import java.util.function.Supplier;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorBuilder;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.FeedExecutorOutput;
import org.mazarineblue.executors.listeners.SubscriberFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestFeedExecutorFactory
        implements FeedExecutorFactory {

    public static FeedExecutorFactory newInstance() {
        return TestFeedExecutorFactory.newInstance(new MemoryFileSystem());
    }

    public static FeedExecutorFactory newInstance(FileSystem fs) {
        return TestFeedExecutorFactory.newInstance(fs, FeedExecutorOutput.getDummy());
    }

    public static FeedExecutorFactory newInstance(FeedExecutorOutput output) {
        return TestFeedExecutorFactory.newInstance(new MemoryFileSystem(), output);
    }

    public static FeedExecutorFactory newInstance(FileSystem fs, FeedExecutorOutput output) {
        return FeedExecutorFactory.newInstance(new FeedExecutorBuilder()
                .setBaseFile("feed.txt")
                .setFileSystem(fs)
                .setOutput(output)
                .setFileTransformer(TestFileTransformer.newInstance()));
    }

    private final FeedExecutor feedExecutor;

    public TestFeedExecutorFactory(FeedExecutor feedExecutor) {
        this.feedExecutor = feedExecutor;
    }

    @Override
    public FeedExecutor create() {
        return feedExecutor;
    }

    @Override
    public void addSubscriber(SubscriberFactory factory) {
        feedExecutor.addSubscriber(factory.getEventType(), factory.getFilter(), factory.getSubscriber());
    }

    @Override
    public void addLinkAfterEventBus(Supplier<Link> link) {
        feedExecutor.addLinkAfterEventBus(link.get());
    }

    @Override
    public void addLink(Supplier<Link> link) {
        feedExecutor.addLink(link.get());
    }

    @Override
    public void setChainModifierListener(Supplier<ChainModifierListener> supplier) {
        feedExecutor.setChainModifierListener(supplier.get());
    }

    @Override
    public void setPublisherListener(Supplier<PublisherListener> supplier) {
        feedExecutor.setPublisherListener(supplier.get());
    }

    @Override
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier) {
        feedExecutor.setFeedExecutorListener(supplier.get());
    }

    @Override
    public void setInterpreterListener(Supplier<InterpreterListener> supplier) {
        feedExecutor.setInterpreterListener(supplier.get());
    }
}
