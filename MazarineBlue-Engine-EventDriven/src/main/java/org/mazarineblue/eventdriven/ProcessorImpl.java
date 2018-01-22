/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventdriven;

import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventnotifier.Event;

/**
 * An {@code Processor} is a concrete implementation of {@link Processor}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Processor
 * @see ProcessorFactory
 */
class ProcessorImpl
        extends ChainManager
        implements Processor {

    private FeedExecutorListener feedExecuteListener = FeedExecutorListener.getDummy();

    @Override
    public void execute(Feed feed) {
        feedExecuteListener.openingFeed(feed);
        try {
            processFeed(feed);
        } finally {
            feedExecuteListener.closingFeed(feed);
            feed.reset();
        }
    }

    private void processFeed(Feed feed) {
        Invoker invoker = new InvokerImpl(this, getChain());
        while (feed.hasNext() && !Thread.interrupted()) {
            Event e = feed.next();
            setInvoker(e, invoker);
            processEvent(e);
            feed.done(e);
        }
    }

    private void setInvoker(Event e, Invoker invoker) {
        if (e instanceof InvokerEvent)
            ((InvokerEvent) e).setInvoker(invoker);
    }

    private void processEvent(Event e) {
        try {
            publish(e);
        } catch (RuntimeException ex) {
            setException(e, ex);
            publishExceptionThrown(e, ex);
        }
    }

    @Override
    public void close() {
        ClosingProcessorEvent e = new ClosingProcessorEvent();
        e.setInvoker(new InvokerImpl(this, getChain()));
        publish(e);
    }

    private void publish(Event event) {
        getChain().publish(event);
    }

    private void setException(Event event, RuntimeException ex) {
        if (event instanceof InvokerEvent)
            ((InvokerEvent) event).setException(ex);
    }

    private void publishExceptionThrown(Event event, RuntimeException ex) {
        ExceptionThrownEvent e = new ExceptionThrownEvent(event, ex);
        publish(e);
        if (!e.isConsumed())
            throw ex;
    }

    @Override
    public final void setFeedExecutorListener(FeedExecutorListener listener) {
        this.feedExecuteListener = listener;
    }
}
