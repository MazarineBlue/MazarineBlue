/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.executors.util;

import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorBuilder;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.FeedExecutorOutput;
import org.mazarineblue.executors.listeners.ChainModifierListenerFactory;
import org.mazarineblue.executors.listeners.FeedExecutorListenerFactory;
import org.mazarineblue.executors.listeners.InterpreterListenerFactory;
import org.mazarineblue.executors.listeners.LinkFactory;
import org.mazarineblue.executors.listeners.PublisherListenerFactory;
import org.mazarineblue.executors.listeners.SubscriberFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;

public class TestFeedExecutorFactory
        implements FeedExecutorFactory {

    public static FeedExecutorFactory getDefaultInstance() {
        return getDefaultInstance(new MemoryFileSystem());
    }

    public static FeedExecutorFactory getDefaultInstance(FileSystem fs) {
        return getDefaultInstance(fs, FeedExecutorOutput.getDummy());
    }

    public static FeedExecutorFactory getDefaultInstance(FileSystem fs, FeedExecutorOutput output) {
        return FeedExecutorFactory.getDefaultInstance(new FeedExecutorBuilder()
                .setBaseFile("feed.txt")
                .setFileSystem(fs)
                .setOutput(output)
                .setFileTransformer(TestFileTransformer.getDefaultInstance()));
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
    public void addLinkAfterEventBus(LinkFactory link) {
        feedExecutor.addLinkAfterEventBus(link.create());
    }

    @Override
    public void addLink(LinkFactory link) {
        feedExecutor.addLink(link.create());
    }

    @Override
    public void setChainModifierListener(ChainModifierListenerFactory listener) {
        feedExecutor.setChainModifierListener(listener.create());
    }

    @Override
    public void setPublisherListener(PublisherListenerFactory listener) {
        feedExecutor.setPublisherListener(listener.create());
    }

    @Override
    public void setFeedExecutorListener(FeedExecutorListenerFactory listener) {
        feedExecutor.setFeedExecutorListener(listener.create());
    }

    @Override
    public void setInterpreterListener(InterpreterListenerFactory listener) {
        feedExecutor.setInterpreterListener(listener.create());
    }
}
