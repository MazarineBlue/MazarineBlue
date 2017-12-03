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
package org.mazarineblue.libraries;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.GetTestResultEvent;
import org.mazarineblue.libraries.util.TestResourceNotFoundException;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;
import org.mazarineblue.utililities.Streams;

public class MainTestLibraryTest {

    static final int BUF_SIZE = 16384;
    static final String RESOURCE_POSTFIX = ".txt";

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutor executor;

    static void assertExecutorListenerCalls(String expectedPath, Object actualResult)
            throws IOException {
        String expected = getExpectedOutput(expectedPath);
        String actual = actualResult.toString();
        if (expected.equals(actual))
            return;
        System.out.println(expected);
        System.out.println("*** ************************************************************************ ***");
        System.out.println(actual);
        throw new ComparisonFailure("", (String) expected, (String) actual);
    }

    private static String getExpectedOutput(String expectedPath)
            throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE);
            Streams.copy(new FileInputStream(getFile(expectedPath)), out);
            return out.toString();
        } catch (TestResourceNotFoundException ex) {
            return "";
        }
    }

    private static File getFile(String expectedPath) {
        ClassLoader classLoader = MainTestLibraryTest.class.getClassLoader();
        URL resource = classLoader.getResource(expectedPath);
        if (resource == null)
            throw new TestResourceNotFoundException(expectedPath);
        return new File(resource.getFile());
    }

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        FeedExecutorFactory factory = TestFeedExecutorFactory.newInstance(fs, output);
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(ExceptionThrownEvent.class));
        executor = factory.create();
    }

    @After
    public void teardown() {
        fs = null;
        output = null;
        executor = null;
    }

    @Test
    public void testSystem_Empty() {
        GetTestResultEvent e = new GetTestResultEvent();
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                new ExecuteInstructionLineEvent("Test system"),
                new ExecuteInstructionLineEvent("End test system"),
                new ExecuteInstructionLineEvent("Run tests"),
                e);
        executor.execute(feed);
        output.throwFirstException();
        assertEquals(0, e.getTests());
        assertEquals(0, e.getFailedTests());
        assertFalse(executor.containsErrors());
    }
}
