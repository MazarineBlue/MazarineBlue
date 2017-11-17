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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.AbstractEvent;
import org.mazarineblue.eventbus.events.TestEvent;
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
import org.mazarineblue.logs.util.TestExecuteInstructionLineEvent;
import org.mazarineblue.logs.util.TestFilterDummy;
import org.mazarineblue.logs.util.TestSubscriberDummy;
import org.mazarineblue.utililities.Timestamp;
import org.mazarineblue.utililities.util.TestTimestamp;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class XmlLogBuilderLinkTest {

    @SuppressWarnings("PublicInnerClass")
    public class Main {

        private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<?xml-stylesheet type=\"text/xsl\" href=\"log.xsl\"?>";
        private XmlLogBuilderLink link;

        private String getExpected(Event e, String status, String message, String responce, int start, int end, int diff) {
            String xml = "<event>";
            xml += String.format("<name>%s</name>", e.getClass().getCanonicalName());
            xml += String.format("<status>%s</status>", status);
            xml += String.format("<message>%s</message>", message);
            xml += String.format("<responce>%s</responce>", responce);
            xml += String.format("<startDate>%s</startDate>", start);
            xml += String.format("<endDate>%s</endDate>", end);
            xml += String.format("<elapsedTime>%d</elapsedTime>", diff);
            return xml + "</event>";
        }

        private void assertEquals(String expected, Event e) {
            link.eventHandler(e);
            Assert.assertEquals(HEADER + expected, link.toXml());
        }

        @Before
        public void setup() {
            link = new XmlLogBuilderLink(new TestTimestamp());
        }

        @Test
        public void exceptionThrownEvent() {
            Event event = new ExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            RuntimeException ex = new RuntimeException("foo", new RuntimeException());
            ExceptionThrownEvent e = new ExceptionThrownEvent(event, ex);

            String expected = getExpected(e, "OK", "foo", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void addLibraryEvent() {
            Event e = new AddLibraryEvent(new TestLibrary());
            String expected = getExpected(e, "OK", "dummy [name1, name2]", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void removeLibraryEvent() {
            Event e = new RemoveLibraryEvent(new TestLibrary());
            String expected = getExpected(e, "OK", "dummy [name1, name2]", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void fetchLibrariesEvent() {
            Condition<Library> matcher = lib -> true;
            FetchLibrariesEvent e = new FetchLibrariesEvent(matcher);
            String expected = getExpected(e, "OK", "matcher=" + matcher, "count=0", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void fetchLibrariesEvent_AddedLibrary() {
            Condition<Library> matcher = lib -> true;
            FetchLibrariesEvent e = new FetchLibrariesEvent(matcher);
            e.addLibrary(new TestLibrary());
            String expected = getExpected(e, "OK", "matcher=" + matcher, "count=1", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void countLibrariesEvent() {
            CountLibrariesEvent e = new CountLibrariesEvent();
            String expected = getExpected(e, "OK", "", "count=0", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void commentInstructionLineEvent() {
            CommentInstructionLineEvent e = new CommentInstructionLineEvent("arg1", "arg2");
            String expected = getExpected(e, "OK", "., arg1, arg2", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void executeInstructionLineEvent() {
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            String expected = getExpected(e, "OK", "namespace.keyword, arg1, arg2", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void executeInstructionLineEvent_ObjectResult() {
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setResult("foo");
            String expected = getExpected(e, "OK", "namespace.keyword, arg1, arg2", "result=foo", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void executeInstructionLineEvent_ErrorResult() {
            TestExecuteInstructionLineEvent e = new TestExecuteInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setException(new RuntimeException("foo"));
            String expected = getExpected(e, "ERROR", "namespace.keyword, arg1, arg2", "exception=foo", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            String expected = getExpected(e, "OK", "namespace.keyword, arg1, arg2", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent_SetInstructionNotFound() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setInstructionsNotFound();
            String expected = getExpected(e, "WARNING", "namespace.keyword, arg1, arg2", "Instruction not found", 1, 2,
                                          -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent_MultipleInstructionsFound() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setMultipleInstructionsFound();
            String expected = getExpected(e, "WARNING", "namespace.keyword, arg1, arg2", "Multiple instructions found",
                                          1, 2,
                                          -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent_SetToFewArguments() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setToFewArguments();
            String expected = getExpected(e, "WARNING", "namespace.keyword, arg1, arg2", "To few arguments", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent_SetArgumentsAreIncompatible() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setArgumentsAreIncompatible();
            String expected = getExpected(e, "WARNING", "namespace.keyword, arg1, arg2", "Argument are incmopatible", 1,
                                          2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void validateInstructionLineEvent_SetCustomFlags() {
            ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2");
            e.setUserErrorFlags(1);
            String expected = getExpected(e, "WARNING", "namespace.keyword, arg1, arg2", "Custom error", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void subscribeEvent_NullInput() {
            SubscribeEvent e = new SubscribeEvent(new TestSubscriberDummy("mySubscriber"));
            String expected = getExpected(e, "OK", "type=Event, filter={null}, subscriber={mySubscriber}", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void subscribeEvent_SpecifiedInput() {
            SubscribeEvent e = new SubscribeEvent(new TestSubscriberDummy("mySubscriber"));
            e.setFilter(new TestFilterDummy("myFilter"));
            e.setType(AbstractEvent.class);
            String expected = getExpected(e, "OK", "type=AbstractEvent, filter={myFilter}, subscriber={mySubscriber}",
                                          "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void unsubscribeEvent_NullInput() {
            UnsubscribeEvent e = new UnsubscribeEvent(new TestSubscriberDummy("mySubscriber"));
            String expected = getExpected(e, "OK", "type=Event, filter={null}, subscriber={mySubscriber}", "", 1, 2, -1);
            assertEquals(expected, e);
        }

        @Test
        public void unsubscribeEvent_SpecifiedInput() {
            UnsubscribeEvent e = new UnsubscribeEvent(new TestSubscriberDummy("mySubscriber"));
            e.setFilter(new TestFilterDummy("myFilter"));
            e.setType(AbstractEvent.class);
            String expected = getExpected(e, "OK", "type=AbstractEvent, filter={myFilter}, subscriber={mySubscriber}",
                                          "", 1, 2, -1);
            assertEquals(expected, e);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode {

        private Timestamp timestamp;
        private XmlLogBuilderLink a;

        @Before
        public void setup() {
            timestamp = new Timestamp();
            a = new XmlLogBuilderLink(timestamp);
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
        public void hashCode_DifferentTimestamp() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(new Timestamp(""));
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentTimestamp() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(new Timestamp(""));
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_DifferentLogContent() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(timestamp);
            b.eventHandler(new TestEvent());
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentLogContent() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(timestamp);
            b.eventHandler(new TestEvent());
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_IdenticalContent() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(timestamp);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_IdenticalContent() {
            XmlLogBuilderLink b = new XmlLogBuilderLink(timestamp);
            assertEquals(a, b);
        }
    }
}
