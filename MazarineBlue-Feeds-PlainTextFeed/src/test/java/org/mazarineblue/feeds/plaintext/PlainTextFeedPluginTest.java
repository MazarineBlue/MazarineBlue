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
package org.mazarineblue.feeds.plaintext;

import java.io.File;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import static org.mazarineblue.plugins.FeedService.canProcess;
import static org.mazarineblue.plugins.FeedService.createFeed;
import static org.mazarineblue.plugins.FeedService.getSheetNames;
import org.mazarineblue.plugins.PluginLoader;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PlainTextFeedPluginTest {

    private MemoryFileSystem fs;
    private File file;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        file = new File("foo.txt");
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
    }

    @Test
    public void testCanProcess() {
        fs.mkfile(file, "");
        assertTrue(canProcess(fs, file));
    }

    @Test
    public void testReadSheetNames() {
        fs.mkfile(file, "");
        assertArrayEquals(new String[]{"Main"}, getSheetNames(fs, file));
    }

    @Test
    public void hasNext_EmptyFile_ReturnsFalse() {
        fs.mkfile(file, "");
        Feed feed = createFeed(fs, file);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_NoColumnsInFile_ReturnsFalse() {
        fs.mkfile(file, "|");
        Feed feed = createFeed(fs, file);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_NonEmptyFile_ReturnsTrue() {
        fs.mkfile(file, "| |");
        Feed feed = createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void next_NonEmptyFile_EventContainsEmptyValues() {
        fs.mkfile(file, "| |");

        Feed feed = createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("", e.getNamespace());
        assertEquals("", e.getKeyword());
        Object[] arguments = e.getArguments();
        assertArrayEquals(new Object[0], e.getArguments());
    }

    @Test
    public void hasNext_RecordWithPath_ReturnsTrue() {
        fs.mkfile(file, "| Namespace.Keyword |");
        Feed feed = createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void next_RecordWithPath_EventContainsNamespaceAndKeyword() {
        fs.mkfile(file, "| Namespace.Keyword |");

        Feed feed = createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("namespace", e.getNamespace());
        assertEquals("keyword", e.getKeyword());
        assertArrayEquals(new Object[0], e.getArguments());
    }

    @Test
    public void hasNext_RecordWithPathAndArguments_ReturnTrue() {
        fs.mkfile(file, "| Keyword | Argument 1 | Argument 2 |");
        Feed feed = createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void hasNext_RecordWithPathAndArguments_Y() {
        fs.mkfile(file, "| Keyword | Argument 1 | Argument 2 |");

        Feed feed = createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("", e.getNamespace());
        assertEquals("keyword", e.getKeyword());
        assertArrayEquals(new String[]{"Argument 1", "Argument 2"}, e.getArguments());
    }

    @Test
    public void testUsingProcessor() {
        PlainTextTable table = new PlainTextTable();
        table.addLine("Keyword 1", "Argument 1", "Argument 2");
        table.addLine("Keyword 2", "Argument 1", "Argument 2");
        Feed feed = new PlainTextFeed(table);

        ProcessorListenerSpy spy = new ProcessorListenerSpy(matchesAny(ExecuteInstructionLineEvent.class));
        Processor processor = Processor.newInstance();
        processor.setFeedExecutorListener(spy);
        processor.setPublisherListener(spy);
        processor.execute(feed);

        spy.openedFeeds().assertClasses(PlainTextFeed.class);
        spy.closedFeeds().assertClasses(PlainTextFeed.class);
        spy.startEvents().assertClasses(ExecuteInstructionLineEvent.class, ExecuteInstructionLineEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(ExecuteInstructionLineEvent.class, ExecuteInstructionLineEvent.class);
    }
}
