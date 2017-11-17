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
package org.mazarineblue.logs;

import java.io.ByteArrayOutputStream;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.AbstractEvent;
import org.mazarineblue.eventbus.link.SubscribeEvent;
import org.mazarineblue.eventbus.link.UnsubscribeEvent;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.function.Condition;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.logs.util.TestFilterDummy;
import org.mazarineblue.logs.util.TestSubscriberDummy;
import org.mazarineblue.utililities.util.TestTimestamp;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PrintLinkTest {

    private ByteArrayOutputStream output;
    private PrintLink link;

    private void assertToStringEquals(String expected, Event e) {
        link.eventHandler(e);
        Assert.assertEquals(expected + System.lineSeparator(), output.toString());
    }

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
        link = new PrintLink(output, new TestTimestamp());
    }

    @Test
    public void exceptionThrownEvent() {
        Event e = new ExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2");
        RuntimeException ex = new RuntimeException("bla", new RuntimeException());
        assertToStringEquals("1 | ExceptionThrownEvent: bla", new ExceptionThrownEvent(e, ex));
    }

    @Test
    public void addLibraryEvent() {
        assertToStringEquals("1 | AddLibraryEvent: dummy [name1, name2]", new AddLibraryEvent(new TestLibrary()));
    }

    @Test
    public void removeLibraryEvent() {
        assertToStringEquals("1 | RemoveLibraryEvent: dummy [name1, name2]",
                             new RemoveLibraryEvent(new TestLibrary()));
    }

    @Test
    public void fetchLibrariesEvent() {
        Condition<Library> matcher = lib -> true;
        assertToStringEquals("1 | FetchLibrariesEvent: matcher=" + matcher, new FetchLibrariesEvent(matcher));
    }

    @Test
    public void countLibrariesEvent() {
        assertToStringEquals("1 | CountLibrariesEvent: ", new CountLibrariesEvent());
    }

    @Test
    public void commentInstructionLineEvent() {
        assertToStringEquals("1 | CommentInstructionLineEvent: ., arg1, arg2",
                             new CommentInstructionLineEvent("arg1", "arg2"));
    }

    @Test
    public void executeInstructionLineEvent() {
        assertToStringEquals("1 | ExecuteInstructionLineEvent: namespace.keyword, arg1, arg2",
                             new ExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2"));
    }

    @Test
    public void validateInstructionLineEvent() {
        assertToStringEquals("1 | ValidateInstructionLineEvent: namespace.keyword, arg1, arg2",
                             new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2"));
    }

    @Test
    public void subscribeEvent_NullInput() {
        SubscribeEvent e = new SubscribeEvent(new TestSubscriberDummy("mySubscriber"));
        assertToStringEquals("1 | SubscribeEvent: type=Event, filter={null}, subscriber={mySubscriber}", e);
    }

    @Test
    public void subscribeEvent_SpecifiedInput() {
        SubscribeEvent e = new SubscribeEvent(new TestSubscriberDummy("mySubscriber"));
        e.setFilter(new TestFilterDummy("myFilter"));
        e.setType(AbstractEvent.class);
        assertToStringEquals("1 | SubscribeEvent: type=AbstractEvent, filter={myFilter}, subscriber={mySubscriber}", e);
    }

    @Test
    public void unsubscribeEvent_NullInput() {
        UnsubscribeEvent e = new UnsubscribeEvent(new TestSubscriberDummy("mySubscriber"));
        assertToStringEquals("1 | UnsubscribeEvent: type=Event, filter={null}, subscriber={mySubscriber}", e);
    }

    @Test
    public void unsubscribeEvent_SpecifiedInput() {
        UnsubscribeEvent e = new UnsubscribeEvent(new TestSubscriberDummy("mySubscriber"));
        e.setFilter(new TestFilterDummy("myFilter"));
        e.setType(AbstractEvent.class);
        assertToStringEquals("1 | UnsubscribeEvent: type=AbstractEvent, filter={myFilter}, subscriber={mySubscriber}", e);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(link.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(link.equals(""));
    }

    @Test
    public void hashCode_DifferentTimestampFormat() {
        PrintLink other = new PrintLink(output, new TestTimestamp(""));
        assertNotEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_DifferentTimestampFormat() {
        assertNotEquals(link, new PrintLink(output, new TestTimestamp("")));
    }

    @Test
    public void hashCode_IdenticalContent() {
        PrintLink other = new PrintLink(output, new TestTimestamp());
        assertEquals(link.hashCode(), other.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        PrintLink other = new PrintLink(output, new TestTimestamp());
        assertEquals(link, other);
    }
}
