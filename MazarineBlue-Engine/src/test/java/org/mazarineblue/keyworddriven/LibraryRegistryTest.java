/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventbus.link.SubscribeEvent;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryExternalCaller;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class LibraryRegistryTest {

    @SuppressWarnings("PublicInnerClass")
    public class Main {

        private MemoryFeed feed;
        private Interpreter interpreter;

        @Before
        public void setup() {
            feed = new MemoryFeed(4);
            feed.add(new SubscribeEvent(new LibraryRegistry()));
            interpreter = Interpreter.getDefaultInstance();
            interpreter.addLink(new EventBusLink());
        }

        @After
        public void teardown() {
            feed = null;
            interpreter = null;
        }

        @Test
        public void addLibrary_Accepted() {
            InvokerEvent addEvent = new AddLibraryEvent(new TestLibraryExternalCaller(Library.NO_NAMESPACE));
            CountLibrariesEvent countEvent = new CountLibrariesEvent();
            feed.add(addEvent);
            feed.add(countEvent);
            interpreter.execute(feed);
            assertTrue(addEvent.isConsumed());
            assertEquals(1, countEvent.getCount());
        }

        @Test(expected = LibraryNotFoundException.class)
        public void removeLibrary_LibraryNotAdded_ExceptionThrown() {
            feed.add(new RemoveLibraryEvent(new TestLibraryExternalCaller(Library.NO_NAMESPACE)));
            feed.add(new CountLibrariesEvent());
            interpreter.execute(feed);
        }

        @Test
        public void removeLibrary_Accepted() {
            Library library = new TestLibraryExternalCaller(Library.NO_NAMESPACE);
            RemoveLibraryEvent removeEvent = new RemoveLibraryEvent(library);
            CountLibrariesEvent countEvent = new CountLibrariesEvent();
            feed.add(new AddLibraryEvent(library));
            feed.add(removeEvent);
            feed.add(countEvent);
            interpreter.execute(feed);
            assertEquals(0, countEvent.getCount());
            assertTrue(removeEvent.isConsumed());
            assertFalse(countEvent.isConsumed());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode {

        private LibraryRegistry a;

        @Before
        public void setup() {
            a = new LibraryRegistry();
        }

        @After
        public void teardown() {
            a = null;
        }

        @Test
        @SuppressWarnings("ObjectEqualsNull")
        public void equals_Null() {
            assertFalse(a.equals(null));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_DifferentClass() {
            assertFalse(a.equals(""));
        }

        @Test
        public void hashCode_DifferentContent() {
            LibraryRegistry b = new LibraryRegistry();
            b.addLibrary(new TestLibrary("foo"));
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentContent() {
            LibraryRegistry b = new LibraryRegistry();
            b.addLibrary(new TestLibrary("foo"));
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_IdenticalContent() {
            LibraryRegistry b = new LibraryRegistry();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_IdenticalContent() {
            LibraryRegistry b = new LibraryRegistry();
            assertEquals(a, b);
        }
    }
}
