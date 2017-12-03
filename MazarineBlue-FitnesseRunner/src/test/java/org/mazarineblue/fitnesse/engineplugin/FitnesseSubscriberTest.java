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
package org.mazarineblue.fitnesse.engineplugin;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;
import org.mazarineblue.variablestore.events.SetVariableEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@SuppressWarnings("NestedAssignment")
@RunWith(HierarchicalContextRunner.class)
public class FitnesseSubscriberTest {

    @SuppressWarnings("PublicInnerClass")
    public class Main {

        private Feed feed;
        private Interpreter interpreter;
        private FitnesseSubscriber subscriber;
        private LinkSpy link;
        private Event event;

        @Before
        public void setup() {
            feed = new MemoryFeed(1);
            interpreter = Interpreter.newInstance();
            interpreter.addLink(link = new LinkSpy());
            interpreter.addLink(new EventBusLink(null, null, subscriber = new FitnesseSubscriber()));
        }

        @After
        public void teardown() {
            feed = null;
            interpreter = null;
            subscriber = null;
            link = null;
            event = null;
        }

        @Test
        public void createFixture() {
            interpreter.execute(new MemoryFeed(event = new CreateFitnesseEvent("actor", "first fixture")));
            assertEquals(new NewInstanceEvent("actor", "first fixture"), link.next());
            assertTrue(event.isConsumed());
        }

        @Test
        public void createFixture2() {
            interpreter.execute(new MemoryFeed(new CreateFitnesseEvent("actor", "first fixture")));
            interpreter.execute(new MemoryFeed(event = new CreateFitnesseEvent("actor", "second fixture")));
            assertEquals(new NewInstanceEvent("actor", "first fixture"), link.next());
            assertEquals(new NewInstanceEvent("actor", "second fixture"), link.next());
            assertTrue(event.isConsumed());
        }

        @Test
        public void call() {
            event = new CallFitnesseEvent("scriptTableActor", "method", "arg1", "arg2");
            interpreter.execute(new MemoryFeed(event));
            assertEquals(new ExecuteInstructionLineEvent("method", "arg1", "arg2"), link.next());
            assertTrue(event.isConsumed());
        }

        @Test
        public void assign() {
            event = new AssignFitnesseEvent("symbol", "some value");
            interpreter.execute(new MemoryFeed(event));
            assertEquals(new SetVariableEvent("symbol", "some value"), link.next());
            assertTrue(event.isConsumed());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode
            extends TestHashCodeAndEquals<FitnesseSubscriber> {

        @Override
        protected FitnesseSubscriber getObject() {
            return new FitnesseSubscriber();
        }

        @Override
        protected FitnesseSubscriber getDifferentObject() {
            FitnesseSubscriber subscriber = new FitnesseSubscriber();
            Interpreter interpreter = Interpreter.newInstance();
            interpreter.addLink(new EventBusLink(null, null, subscriber));
            interpreter.execute(new MemoryFeed(new CreateFitnesseEvent("instance", "fixture")));
            return subscriber;
        }
    }
}
