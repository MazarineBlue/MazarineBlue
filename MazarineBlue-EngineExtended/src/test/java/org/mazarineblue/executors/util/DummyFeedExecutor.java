/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.executors.util;

import java.io.File;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.executors.FeedExecutor;

public class DummyFeedExecutor
        implements FeedExecutor {

    @Override
    public void execute(File file, String sheet) {
        // A dummy does nothing
    }

    @Override
    public void execute(Feed feed) {
        // A dummy does nothing
    }

    @Override
    public boolean containsErrors() {
        return false;
    }

    @Override
    public void addSubscriber(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        // A dummy does nothing
    }

    @Override
    public void addLinkAfterEventBus(Link link) {
        // A dummy does nothing
    }

    @Override
    public void addLink(Link link) {
        // A dummy does nothing
    }

    @Override
    public void setChainModifierListener(ChainModifierListener listener) {
        // A dummy does nothing
    }

    @Override
    public void setPublisherListener(PublisherListener listener) {
        // A dummy does nothing
    }

    @Override
    public void setFeedExecutorListener(FeedExecutorListener listener) {
        // A dummy does nothing
    }

    @Override
    public void setInterpreterListener(InterpreterListener listener) {
        // A dummy does nothing
    }
}
