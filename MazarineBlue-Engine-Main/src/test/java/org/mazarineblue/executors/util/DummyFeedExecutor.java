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

import java.io.File;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.executors.Executor;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DummyFeedExecutor
        implements Executor {

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
    public void addLinkAfterVariableParser(Subscriber<Event> subscriber) {
        // A dummy does nothing
    }

    @Override
    public void addLinkAfterLibraryRegistry(Subscriber<Event> link) {
        // A dummy does nothing
    }

    @Override
    public void addLink(Subscriber<Event> link) {
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
}
