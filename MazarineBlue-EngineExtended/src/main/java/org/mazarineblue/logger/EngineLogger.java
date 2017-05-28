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
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.logger.events.EngineLoggerEvent;
import org.mazarineblue.util.DateFactory;
import org.mazarineblue.utililities.exceptions.NeverThrownException;

/**
 * A {@code EngineLogger} keeps a log on the activities that where performed
 * though a {@code Interpreter} and any exceptions, which where thrown as an
 * result.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLogger
        extends ReflectionSubscriber<Event>
        implements InterpreterListener {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream printer = new PrintStream(output);
    private final EngineLoggerListenerGroup listener = new EngineLoggerListenerGroup();
    private final DateFactory dateFactory;

    public EngineLogger(DateFactory dateFactory) {
        this.dateFactory = dateFactory;
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

    private boolean foundException;

    @Override
    public void startPublishingEvent(Event event) {
        foundException = false;
        String format = "<event><date>%s</date><type>%s</type><message>%s</message>";
        printer.format(format, dateFactory.getCurrentDate(), event.getClass(), event.toString());
        listener.startPublishingEvent(event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        String format = "<exception><date>%s</date><message>%s</message></exception>";
        printer.format(format, dateFactory.getCurrentDate(), ex.getMessage());
        listener.exceptionThrown(event, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        printer.append(foundException ? "</exception></event>" : "</event>");
        listener.endPublishedEvent(event);
    }

    @Override
    public void addedLink(Link link) {
        String format = "<addedLink><date>%s</date><link>%s</link></addedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link);
        listener.addedLink(link);
    }

    @Override
    public void addedLink(Link link, Link after) {
        String format = "<addedLink><date>%s</date><link>%s</link><afterLink>%s</afterLink></addedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link, after);
        listener.addedLink(link, after);
    }

    @Override
    public void removedLink(Link link) {
        String format = "<removedLink><date>%s</date><link>%s</link></removedLink>";
        printer.format(format, dateFactory.getCurrentDate(), link);
        listener.removedLink(link);
    }

    @EventHandler
    public void eventHandler(EngineLoggerEvent e) {
        listener.eventHandler(e);
    }

    @Override
    protected void uncatchedEventHandler(Event event) {
        throw new NeverThrownException();
    }

    public String getString() {
        return output.toString();
    }
}
