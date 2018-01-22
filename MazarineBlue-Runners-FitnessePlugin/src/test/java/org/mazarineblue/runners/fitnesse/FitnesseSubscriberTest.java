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
package org.mazarineblue.runners.fitnesse;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.subscribers.SubscriberSpy;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.runners.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@SuppressWarnings("NestedAssignment")
public class FitnesseSubscriberTest {

    private Feed feed;
    private Processor processor;
    private FitnesseSubscriber subscriber;
    private SubscriberSpy<Event> spy;
    private Event event;

    @Before
    public void setup() {
        feed = new MemoryFeed(1);
        spy = new SubscriberSpy<>(e -> true);
        subscriber = new FitnesseSubscriber();
        processor = Processor.newInstance();
        processor.addLink(spy);
        processor.addLink(subscriber);
    }

    @After
    public void teardown() {
        feed = null;
        processor = null;
        subscriber = null;
        spy = null;
        event = null;
    }

    @Test
    public void createFixture() {
        processor.execute(new MemoryFeed(event = new CreateFitnesseEvent("actor", "first fixture")));
        spy.assertEvents(new NewInstanceEvent("actor", "first fixture"));
        assertTrue(event.isConsumed());
    }

    @Test
    public void createFixture2() {
        processor.execute(new MemoryFeed(new CreateFitnesseEvent("actor", "first fixture")));
        processor.execute(new MemoryFeed(event = new CreateFitnesseEvent("actor", "second fixture")));
        spy.assertEvents(new NewInstanceEvent("actor", "first fixture"),
                          new NewInstanceEvent("actor", "second fixture"));
        assertTrue(event.isConsumed());
    }

    @Test
    public void call() {
        event = new CallFitnesseEvent("scriptTableActor", "method", "arg1", "arg2");
        processor.execute(new MemoryFeed(event));
        spy.assertEvents(new ExecuteInstructionLineEvent("method", "arg1", "arg2"));
        assertTrue(event.isConsumed());
    }

    @Test
    public void assign() {
        event = new AssignFitnesseEvent("symbol", "some value");
        processor.execute(new MemoryFeed(event));
        spy.assertEvents(new SetVariableEvent("symbol", "some value"));
        assertTrue(event.isConsumed());
    }
}
