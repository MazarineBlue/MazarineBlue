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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedExecutorListenerSpy
        extends PublisherListenerSpy
        implements FeedExecutorListener {

    private final List<Feed> openedFeeds = new ArrayList<>();
    private final List<Feed> closedFeeds = new ArrayList<>();

    public FeedExecutorListenerSpy() {
    }

    public FeedExecutorListenerSpy(Predicate<Event> recordEvents) {
        super(recordEvents);
    }

    @Override
    public String toString() {
        return "feeds{" + "opened=" + openedFeeds.size() + ", closed=" + closedFeeds.size() + "}, " + super.toString();
    }

    @Override
    public void openingFeed(Feed feed) {
        openedFeeds.add(feed);
    }

    @Override
    public void closingFeed(Feed feed) {
        closedFeeds.add(feed);
    }

    public int countOpeningFeed() {
        return openedFeeds.size();
    }

    public int countClosingFeed() {
        return closedFeeds.size();
    }
}
