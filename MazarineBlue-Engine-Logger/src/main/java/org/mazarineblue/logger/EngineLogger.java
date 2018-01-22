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
package org.mazarineblue.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.logger.events.EngineLoggerEvent;
import org.mazarineblue.util.DateFactory;

/**
 * A {@code EngineLogger} keeps a log on the activities that where performed
 * though a {@code Processor} and any exceptions, which where thrown as an
 * result.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLogger
        extends ReflectionSubscriber<Event>
        implements ChainModifierListener, FeedExecutorListener, PublisherListener {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream printer = new PrintStream(output);
    private final EngineLoggerListenerService listener = new EngineLoggerListenerService();
    private final DateFactory dateFactory;

    public EngineLogger(DateFactory dateFactory) {
        this.dateFactory = dateFactory;
    }

    @Override
    public String toString() {
        return output.toString();
    }

    @Override
    public void openingFeed(Feed feed) {
        String format = "<feed><date>%s</date><type>%s</type><events>";
        printer.format(format, dateFactory.getCurrentDate(), feed.toString());
        listener.openingFeed(feed);
    }

    @Override
    public void closingFeed(Feed feed) {
        printer.append("</events></feed>");
        listener.closingFeed(feed);
    }

    @Override
    public void startPublishingEvent(Event event) {
        String format = "<event><date>%s</date><type>%s</type><message>%s</message>";
        printer.format(format, dateFactory.getCurrentDate(), event.getClass(), event.message());
        listener.startPublishingEvent(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        String format = "<exception><message>%s</message></exception>";
        printer.format(format, ex.getMessage());
        listener.exceptionThrown(event, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        printer.format("<responce>%s</responce></event>", event.responce());
        listener.endPublishedEvent(event);
    }

    @Override
    public void addedLink(Subscriber<Event> link) {
        String format = "<addedLink><date>%s</date><link>%s</link></addedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link);
        listener.addedLink(link);
    }

    @Override
    public void addedLink(Subscriber<Event> link, Subscriber<Event> after) {
        String format = "<addedLink><date>%s</date><link>%s</link><afterLink>%s</afterLink></addedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link, after);
        listener.addedLink(link, after);
    }

    @Override
    public void removedLink(Subscriber<Event> link) {
        String format = "<removedLink><date>%s</date><link>%s</link></removedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link);
        listener.removedLink(link);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see EngineLoggerEvent
     */
    @EventHandler
    public void eventHandler(EngineLoggerEvent<?> event) {
        listener.eventHandler(event);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
