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
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.executors.ExecutorListener;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestFeedExecutorFactory
        implements ExecutorFactory {

    private final Executor feedExecutor;

    public static ExecutorFactory newInstance() {
        return TestFeedExecutorFactory.newInstance(new MemoryFileSystem());
    }

    public static ExecutorFactory newInstance(FileSystem fs) {
        return TestFeedExecutorFactory.newInstance(fs, ExecutorListener.getDummy());
    }

    public static ExecutorFactory newInstance(ExecutorListener output) {
        return TestFeedExecutorFactory.newInstance(new MemoryFileSystem(), output);
    }

    public static ExecutorFactory newInstance(FileSystem fs, ExecutorListener output) {
        return ExecutorFactory.newInstance(new ExecutorBuilder()
                .setBaseFile("feed.txt")
                .setFileSystem(fs)
                .setOutput(output)
                .setFileTransformer(TestFileTransformer.newInstance()));
    }

    public TestFeedExecutorFactory(Executor feedExecutor) {
        this.feedExecutor = feedExecutor;
    }

    @Override
    public Executor create() {
        return feedExecutor;
    }

    @Override
    public void addLinkAfterVariableParser(Supplier<Subscriber<Event>> link) {
        feedExecutor.addLinkAfterVariableParser(link.get());
    }

    @Override
    public void addLinkAfterEventNotifier(Supplier<Subscriber<Event>> link) {
        feedExecutor.addLinkAfterLibraryRegistry(link.get());
    }

    @Override
    public void addLink(Supplier<Subscriber<Event>> link) {
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
}
