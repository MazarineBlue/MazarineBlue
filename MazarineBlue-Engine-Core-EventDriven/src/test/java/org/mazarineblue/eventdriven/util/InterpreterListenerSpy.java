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
package org.mazarineblue.eventdriven.util;

import static org.mazarineblue.eventbus.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListenerAdapter;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InterpreterListenerSpy
        extends InterpreterListenerAdapter {

    private final FeedExecutorListener feedExecuteListener;
    private final ChainModifierListener chainModifiedListener;

    public InterpreterListenerSpy() {
        this(new FeedExecutorListenerSpy(matchesNoneAutoConsumable()), new ChainModifierListenerSpy());
    }

    public InterpreterListenerSpy(FeedExecutorListener feedExecuteListener,
                                  ChainModifierListener chainModifiedListener) {
        super(feedExecuteListener, chainModifiedListener);
        this.feedExecuteListener = feedExecuteListener;
        this.chainModifiedListener = chainModifiedListener;
    }

    @Override
    public String toString() {
        return feedExecuteListener.toString() + ", linksCounted=" + chainModifiedListener.toString();
    }

    public int countOpeningFeed() {
        return ((FeedExecutorListenerSpy) getFeedExecutorListener()).countOpeningFeed();
    }

    public int countClosingFeed() {
        return ((FeedExecutorListenerSpy) getFeedExecutorListener()).countClosingFeed();
    }

    public int countStartingEvents() {
        return ((PublisherListenerSpy) getFeedExecutorListener()).countStartEvents();
    }

    public int countExceptions() {
        return ((PublisherListenerSpy) getFeedExecutorListener()).countExceptions();
    }

    public int countEndingEvents() {
        return ((PublisherListenerSpy) getFeedExecutorListener()).countEndEvents();
    }

    public int countLinks() {
        return ((ChainModifierListenerSpy) getChainModifierListener()).countLinks();
    }
}
