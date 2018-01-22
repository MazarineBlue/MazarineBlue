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

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.eventnotifier.subscribers.PerformActionSubscriber;
import org.mazarineblue.util.TestDateFactory;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLoggerTest {

    private EngineLogger logger;
    private Processor processor;

    @Before
    public void setup() {
        logger = new EngineLogger(new TestDateFactory());
        processor = Processor.newInstance();
        processor.setChainModifierListener(logger);
        processor.setFeedExecutorListener(logger);
        processor.setPublisherListener(logger);
    }

    @After
    public void teardown() {
        logger = null;
        processor = null;
    }

    @Test
    public void testLogger_SingleException_ReturnsSuccesfullLog() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"), new TestInvokerEvent("c", "d"));
        processor.execute(feed);
        String expectedFormat = ""
                + "<feed>"
                + "<date>1</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>2</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<responce>b</responce>"
                + "</event>"
                + "<event>" + "<date>3</date>" + "<type>[^<]+</type>"
                + "<message>c</message>"
                + "<responce>d</responce>"
                + "</event>"
                + "</events>"
                + "</feed>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_DoubleEvent_ReturnsSuccesfullLog() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"), new TestInvokerEvent("c", "d"));
        processor.execute(feed);
        String expectedFormat = ""
                + "<feed>"
                + "<date>1</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>2</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<responce>b</responce>"
                + "</event>"
                + "<event>" + "<date>3</date>" + "<type>[^<]+</type>"
                + "<message>c</message>"
                + "<responce>d</responce>"
                + "</event>"
                + "</events>"
                + "</feed>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_AddLink_ReturnsSuccesfullLog() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"));
        ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class));
        processor.addLink(after);
        processor.execute(feed);
        String expectedFormat = ""
                + "<addedLink>" + "<date>1</date>" + "<link>[^<]+</link>" + "</addedLink>"
                + "<feed>"
                + "<date>2</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>3</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<responce>b</responce>"
                + "</event>"
                + "</events>"
                + "</feed>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_AddLinkAfter_ReturnsSuccesfullLog() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"));
        ConsumeEventsSubscriber<Event> before = new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class));
        ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class));
        processor.addLink(after);
        processor.addLink(before, after);
        processor.execute(feed);
        String expectedFormat = ""
                + "<addedLink>" + "<date>1</date>" + "<link>[^<]+</link>" + "</addedLink>"
                + "<addedLink>" + "<date>2</date>" + "<link>[^<]+</link>" + "<afterLink>[^<]+</afterLink>" + "</addedLink>"
                + "<feed>"
                + "<date>3</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>4</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<responce>b</responce>"
                + "</event>"
                + "</events>"
                + "</feed>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_RemoveLink_LogShowsException() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"));
        ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class));
        processor.addLink(after);
        processor.execute(feed);
        processor.removeLink(after);
        String expectedFormat = ""
                + "<addedLink>" + "<date>1</date>" + "<link>[^<]+</link>" + "</addedLink>"
                + "<feed>"
                + "<date>2</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>3</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<responce>b</responce>"
                + "</event>"
                + "</events>"
                + "</feed>"
                + "<removedLink>" + "<date>4</date>" + "<link>[^<]+</link>" + "</removedLink>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_EventExecutesFeed_ReturnsSuccesfullLog() {
        MemoryFeed intialFeed = new MemoryFeed(new TestInvokerEvent("a", "b"), new TestInvokerEvent("c", "d"));
        MemoryFeed nestedFeed = new MemoryFeed(new TestInvokerEvent("e", "f"), new TestInvokerEvent("g", "h"));
        processor.addLink(new PerformActionSubscriber(t -> processor.execute(nestedFeed),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(intialFeed);
        String expectedFormat = ""
                + "<addedLink>" + "<date>1</date>" + "<link>[^<]+</link>" + "</addedLink>"
                + "<feed>"
                + "<date>2</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>3</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<feed>"
                + "<date>4</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>5</date>" + "<type>[^<]+</type>"
                + "<message>e</message>"
                + "<responce>f</responce>"
                + "</event>"
                + "<event>" + "<date>6</date>" + "<type>[^<]+</type>"
                + "<message>g</message>"
                + "<responce>h</responce>"
                + "</event>"
                + "</events>"
                + "</feed>"
                + "<responce>b</responce>"
                + "</event>"
                + "<event>" + "<date>7</date>" + "<type>[^<]+</type>"
                + "<message>c</message>"
                + "<responce>d</responce>"
                + "</event>"
                + "</events>"
                + "</feed>";
        assertString(expectedFormat, logger.toString());
    }

    @Test
    public void testLogger_SimulateEventThrowException_LogShowsException() {
        MemoryFeed feed = new MemoryFeed(new TestInvokerEvent("a", "b"));
        ExceptionThrowingSubscriber<Event> before = new ExceptionThrowingSubscriber(t-> true, e -> new RuntimeException());
        ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class));
        processor.addLink(after);
        processor.addLink(before, after);
        processor.execute(feed);
        processor.removeLink(after);
        String expectedFormat = ""
                + "<addedLink>" + "<date>1</date>" + "<link>[^<]+</link>" + "</addedLink>"
                + "<addedLink>" + "<date>2</date>" + "<link>[^<]+</link>" + "<afterLink>[^<]+</afterLink>" + "</addedLink>"
                + "<feed>"
                + "<date>3</date>" + "<type>[^<]+</type>"
                + "<events>"
                + "<event>" + "<date>4</date>" + "<type>[^<]+</type>"
                + "<message>a</message>"
                + "<exception>" + "<message>[^<]*</message>" + "</exception>"
                + "<responce>b</responce>"
                + "</event>"
                + "<event>" + "<date>5</date>" + "<type>[^<]+</type>"
                + "<message>[^<]+</message>"
                + "<responce></responce>"
                + "</event>"
                + "</events>"
                + "</feed>"
                + "<removedLink>" + "<date>6</date>" + "<link>[^<]+</link>" + "</removedLink>";
        assertString(expectedFormat, logger.toString());
    }

    private static void assertString(String expectedFormat, String actual) {
        boolean matches = actual.matches(expectedFormat);
        if (!matches)
            fail("Log did not match regular expression.");
    }
}
