/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.BaseMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.events.library.FetchLibrariesEvent;
import org.mazarineblue.events.library.FetchLibraryEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.Interpreter;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.Link;
import org.mazarineblue.keyworddriven.logs.LogBuilder;
import org.mazarineblue.keyworddriven.util.old.DummyDocumentMediator;
import org.mazarineblue.keyworddriven.util.old.TestFeedBuilder;
import org.mazarineblue.keyworddriven.util.old.TestLog;
import org.mazarineblue.keyworddriven.util.old.TestLogBuilder;
import org.mazarineblue.keyworddriven.util.old.TestSheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LibraryTestTemplate {

    private Interpreter executor;
    protected Input input;
    protected Output output;
    protected Chain chain;
    protected Libraries libraries;
    protected Events events;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected class Input {

        private final FeedBuilder feedBuilder;
        private final Collection<InstructionLine> collection;
        private Feed feed;
        private int lineCount;
        private final TestSheetFactory sheetFactory = new TestSheetFactory();

        public Input(FeedBuilder feedBuilder) {
            this.feedBuilder = feedBuilder;
            collection = new ArrayList<>();
            feed = feedBuilder.createFeed(collection);
            lineCount = 0;
        }

        public void addInstruction(String namespace, String keyword) {
            addInstruction(namespace, keyword, new Object[0]);
        }

        public void addInstruction(String namespace, String keyword,
                                   Object... parameters) {
            String lineIdentifier = "line-" + lineCount;
            collection.add(new InstructionLine(lineIdentifier, namespace, keyword, parameters));
            ++lineCount;
        }

        private void clearCollection() {
            collection.clear();
            feed = feedBuilder.createFeed(collection);
        }

        public void putSource(String key, DataSource value) {
            sheetFactory.put(key, value);
        }
    }

    protected class Output {

        private final TestLog log = new TestLog();
        private final ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
        private final DocumentMediator documentMediator = new DummyDocumentMediator();
        private final PrintStream printStream, originStream;

        public Output() {
            originStream = System.out;
            printStream = new PrintStream(arrayOutput);
            System.setOut(printStream);
        }

        private void close() {
            System.setOut(originStream);
        }

        public void assertSystemOut(String expected) {
            Assert.assertEquals(expected, arrayOutput.toString().trim());
        }

        public void assertLines(int lines) {
            Assert.assertEquals(lines, log.getLines());
        }

        public void assertExecuted(int executed) {
            Assert.assertEquals(executed, log.getExecuted());
        }

        public void assertValidated(int validate) {
            Assert.assertEquals(validate, log.getValidated());
        }

        public void assertInfo(int info) {
            Assert.assertEquals(info, log.getInfo());
        }

        public void assertWarning(int warning) {
            Assert.assertEquals(warning, log.getWarning());
        }

        public void assertError(int error) {
            Assert.assertEquals(error, log.getError());
        }
    }

    protected class Chain {

        private final int chainBaseCount;

        public Chain() {
            this.chainBaseCount = executor.chain().size();
        }

        public void insertLink(Link link) {
            executor.chain().insert(link);
        }

        public void removeLink(Link link) {
            executor.chain().insert(link);
        }

        public void assertLinkCount(int diff) {
            Assert.assertEquals(chainBaseCount + diff,
                                executor.chain().size());
        }
    }

    protected class Libraries {

        public void registerLibrary(Library library) {
            executor.libraries().register(library);
        }

        public <T extends Library> T fetch(Class<T> clazz) {
            FetchLibraryEvent<T> event = new FetchLibraryEvent(clazz);
            executor.publish(event);
            return event.getLibrary();
        }

        public <T extends Library> List<T> fetchAll(Class<T> clazz) {
            FetchLibrariesEvent<T> event = new FetchLibrariesEvent(clazz);
            executor.publish(event);
            return event.getLibraries();
        }
    }

    protected class Events {

        private final CatchEventsLibrary catchEventsLibrary;

        protected Events() {
            catchEventsLibrary = new CatchEventsLibrary();
            executor.libraries().register(catchEventsLibrary);
        }

        private void close() {
            executor.libraries().unregister(catchEventsLibrary);
        }

        public <T extends Event> List<T> fetchFiredEvents(Class<T> clazz) {
            return catchEventsLibrary.fetchEvents(clazz);
        }
    }

    public void librarySetup(Interpreter executor) {
        FeedBuilder feedBuilder = new TestFeedBuilder();
        LogBuilder logBuilder = new TestLogBuilder();
        this.executor = executor;
        input = new Input(feedBuilder);
        output = new Output();
        chain = new Chain();
        libraries = new Libraries();
        events = new Events();
    }

    @After
    public void libraryTeardown() {
        output.close();
        events.close();

        executor = null;
        input = null;
        output = null;
        chain = null;
        libraries = null;
        events = null;
    }

    protected void execute() {
        try {
            executor.execute(input.feed, output.log, output.documentMediator, input.sheetFactory);
        } finally {
            input.clearCollection();
        }
    }

    protected void validate() {
        try {
            executor.validate(input.feed, output.log, output.documentMediator, input.sheetFactory);
        } finally {
            input.clearCollection();
        }
    }

    protected void expectCause(Class<? extends Throwable> type) {
        BaseMatcher coreMatcher = new IsInstanceOf(type);
        thrown.expectCause(coreMatcher);
    }
}
