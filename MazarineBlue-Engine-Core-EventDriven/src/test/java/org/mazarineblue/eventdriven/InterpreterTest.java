/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.util.Arrays.asList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import static org.mazarineblue.eventbus.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventbus.events.AbstractEvent;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.exceptions.EventNotConsumedException;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.ChainModifierListenerSpy;
import org.mazarineblue.eventdriven.util.FeedExecutorListenerSpy;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InterpreterTest {

    private Interpreter interpreter;
    private ChainModifierListenerSpy chainModifiedListenerSpy;
    private FeedExecutorListenerSpy feedExecuteListenerSpy;

    @Before
    public void setup() {
        interpreter = Interpreter.newInstance();
        chainModifiedListenerSpy = new ChainModifierListenerSpy();
        feedExecuteListenerSpy = new FeedExecutorListenerSpy(matchesNoneAutoConsumable());
        interpreter.setChainModifierListener(chainModifiedListenerSpy);
        interpreter.setFeedExecutorListener(feedExecuteListenerSpy);
    }

    @After
    public void teardown() {
        interpreter = null;
    }

    @Test
    public void execute_EmptyChain_DoesNothing()
            throws Exception {
        LinkSpy link = new LinkSpy(matchesNoneAutoConsumable());
        interpreter.addLink(link);
        interpreter.execute(new MemoryFeed(new TestEvent()));
        interpreter.close();
        assertEquals(1, link.size());

        List<Class<? extends AbstractEvent>> firedEvents = asList(TestEvent.class);
        assertEquals(1, chainModifiedListenerSpy.countLinks());
        assertEquals(1, feedExecuteListenerSpy.countOpeningFeed());
        assertEquals(1, feedExecuteListenerSpy.countClosingFeed());
        assertEquals(firedEvents, feedExecuteListenerSpy.getStartEventTypes());
        assertEquals(0, feedExecuteListenerSpy.countExceptions());
        assertEquals(firedEvents, feedExecuteListenerSpy.getEndEventTypes());
    }

    @Test(expected = EventNotConsumedException.class)
    public void execute_UnconsumedExceptionThrowerLink1_ThrowsException()
            throws Exception {
        interpreter.addLink(new UnconsumedExceptionThrowerLink());
        interpreter.execute(new MemoryFeed(new TestEvent()));
        interpreter.close();
    }

    @Test
    public void execute_UnconsumedExceptionThrowerLink1_DoesNothing() {
        Event e = new TestEvent();
        interpreter.addLink(new UnconsumedExceptionThrowerLink());
        interpreter.addLink(new ConsumeEventsLink());
        interpreter.execute(new MemoryFeed(e));
        try {
            interpreter.close();
        } catch (Exception ex) {
            Logger.getLogger(InterpreterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(e.isConsumed());

        List<Class<? extends AbstractEvent>> firedEvents = asList(TestEvent.class);
        assertEquals(2, chainModifiedListenerSpy.countLinks());
        assertEquals(1, feedExecuteListenerSpy.countOpeningFeed());
        assertEquals(1, feedExecuteListenerSpy.countClosingFeed());
        assertEquals(firedEvents, feedExecuteListenerSpy.getStartEventTypes());
        assertEquals(0, feedExecuteListenerSpy.countExceptions());
        assertEquals(firedEvents, feedExecuteListenerSpy.getEndEventTypes());
    }

    @Test
    public void execute_ExceptionThrown() {
        MemoryFeed feed = new MemoryFeed(new TestEvent());
        boolean exceptionThrown = false;

        interpreter.addLink(new ExceptionThrowingLink());
        try {
            interpreter.execute(feed);
        } catch (RuntimeException ex) {
            exceptionThrown = true;
        }

        List<Class<? extends AbstractEvent>> firedEvents = asList(TestEvent.class, ExceptionThrownEvent.class);
        List<Class<?>> endEventTypes = feedExecuteListenerSpy.getEndEventTypes();
        assertEquals(1, chainModifiedListenerSpy.countLinks());
        assertEquals(1, feedExecuteListenerSpy.countOpeningFeed());
        assertEquals(1, feedExecuteListenerSpy.countClosingFeed());
        assertEquals(firedEvents, feedExecuteListenerSpy.getStartEventTypes());
        assertEquals(2, feedExecuteListenerSpy.countExceptions());
        assertEquals(firedEvents, feedExecuteListenerSpy.getEndEventTypes());
        assertTrue(exceptionThrown);
    }
}
