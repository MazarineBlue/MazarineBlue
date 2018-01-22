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
package org.mazarineblue.eventdriven.util.listeners;

import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.util.TestList;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ProcessorListenerSpy
        implements ChainModifierListener, FeedExecutorListener, PublisherListener {

    private Predicate<Event> recordEvents;

    private final TestList<Subscriber<Event>> links = new TestList<>();
    private final TestList<Feed> openedFeeds = new TestList<>();
    private final TestList<Feed> closedFeeds = new TestList<>();
    private final TestList<Event> startEvents = new TestList<>();
    private final TestList<Exception> exceptions = new TestList<>();
    private final TestList<Event> endEvents = new TestList<>();

    public ProcessorListenerSpy() {
        this(matchesNoneAutoConsumable());
    }

    public ProcessorListenerSpy(Predicate<Event> recordEvents) {
        this.recordEvents = recordEvents;
    }

    @Override
    public String toString() {
        return new StringBuilder(80)
                .append("links=").append(links.size()).append(", ")
                .append("feeds{opened=").append(openedFeeds.size())
                .append(", closed=").append(closedFeeds.size())
                .append("}, ")
                .append("events{start=").append(startEvents.size())
                .append(", exceptions=").append(exceptions.size())
                .append(", end=").append(endEvents.size())
                .append('}')
                .toString();
    }

    @Override
    public void addedLink(Subscriber<Event> link) {
        links.add(link);
    }

    @Override
    public void addedLink(Subscriber<Event> link, Subscriber<Event> after) {
        links.add(link);
    }

    @Override
    public void removedLink(Subscriber<Event> link) {
        links.remove(link);
    }

    public TestList<Subscriber<Event>> links() {
        return links;
    }

    @Override
    public void openingFeed(Feed feed) {
        openedFeeds.add(feed);
    }

    @Override
    public void closingFeed(Feed feed) {
        closedFeeds.add(feed);
    }

    public TestList<Feed> openedFeeds() {
        return openedFeeds;
    }

    public TestList<Feed> closedFeeds() {
        return closedFeeds;
    }

    @Override
    public void startPublishingEvent(Event event) {
        if (recordEvents.test(event))
            startEvents.add(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        exceptions.add(ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        if (recordEvents.test(event))
            endEvents.add(event);
    }

    public TestList<Event> startEvents() {
        return startEvents;
    }

    public TestList<Exception> exceptions() {
        return exceptions;
    }

    public TestList<Event> endEvents() {
        return endEvents;
    }
}
