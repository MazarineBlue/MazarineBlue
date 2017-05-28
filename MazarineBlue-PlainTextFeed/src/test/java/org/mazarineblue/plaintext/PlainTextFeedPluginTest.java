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
package org.mazarineblue.plaintext;

import java.io.File;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.util.FeedExecutorListenerSpy;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.plugins.FeedService;
import org.mazarineblue.plugins.PluginLoader;

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
        assertTrue(FeedService.canProcess(fs, file));
    }

    @Test
    public void testReadSheetNames() {
        fs.mkfile(file, "");
        assertArrayEquals(new String[]{"Main"}, FeedService.readSheetNames(fs, file));
    }

    @Test
    public void hasNext_EmptyFile_ReturnsFalse() {
        fs.mkfile(file, "");
        Feed feed = FeedService.createFeed(fs, file);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_NoColumnsInFile_ReturnsFalse() {
        fs.mkfile(file, "|");
        Feed feed = FeedService.createFeed(fs, file);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_NonEmptyFile_ReturnsTrue() {
        fs.mkfile(file, "| |");
        Feed feed = FeedService.createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void next_NonEmptyFile_EventContainsEmptyValues() {
        fs.mkfile(file, "| |");

        Feed feed = FeedService.createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("", e.getNamespace());
        assertEquals("", e.getKeyword());
        Object[] arguments = e.getArguments();
        assertArrayEquals(new Object[0], e.getArguments());
    }

    @Test
    public void hasNext_RecordWithPath_ReturnsTrue() {
        fs.mkfile(file, "| Namespace.Keyword |");
        Feed feed = FeedService.createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void next_RecordWithPath_EventContainsNamespaceAndKeyword() {
        fs.mkfile(file, "| Namespace.Keyword |");

        Feed feed = FeedService.createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("namespace", e.getNamespace());
        assertEquals("Keyword", e.getKeyword());
        assertArrayEquals(new Object[0], e.getArguments());
    }

    @Test
    public void hasNext_RecordWithPathAndArguments_ReturnTrue() {
        fs.mkfile(file, "| Keyword | Argument 1 | Argument 2 |");
        Feed feed = FeedService.createFeed(fs, file);
        assertTrue(feed.hasNext());
    }

    @Test
    public void hasNext_RecordWithPathAndArguments_Y() {
        fs.mkfile(file, "| Keyword | Argument 1 | Argument 2 |");

        Feed feed = FeedService.createFeed(fs, file);
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();

        assertEquals("", e.getNamespace());
        assertEquals("Keyword", e.getKeyword());
        assertArrayEquals(new String[]{"Argument 1", "Argument 2"}, e.getArguments());
    }

    @Test
    public void testUsingInterpreter() {
        PlainTextTable table = new PlainTextTable();
        table.addLine("Keyword 1", "Argument 1", "Argument 2");
        table.addLine("Keyword 2", "Argument 1", "Argument 2");
        Feed feed = new PlainTextFeed(table);

        FeedExecutorListenerSpy feedExecuteListenerSpy = new FeedExecutorListenerSpy();
        Interpreter interpreter = Interpreter.getDefaultInstance();
        interpreter.setFeedExecutorListener(feedExecuteListenerSpy);
        interpreter.execute(feed);

        assertEquals(1, feedExecuteListenerSpy.countOpeningFeed());
        assertEquals(1, feedExecuteListenerSpy.countClosingFeed());
        assertEquals(2, feedExecuteListenerSpy.countStartEvents());
        assertEquals(0, feedExecuteListenerSpy.countExceptions());
        assertEquals(2, feedExecuteListenerSpy.countEndEvents());
    }
}
