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
package org.mazarineblue.keyworddriven;

import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.logs.LogBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.keyworddriven.util.old.DummyDocumentMediator;
import org.mazarineblue.keyworddriven.util.old.TestFeedBuilder;
import org.mazarineblue.keyworddriven.util.old.TestLog;
import org.mazarineblue.keyworddriven.util.old.TestLogBuilder;
import org.mazarineblue.keyworddriven.util.old.TestSheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ProcessorTest
        extends LibraryTestTemplate {

    static public InterpreterContext createExecutorContext(Feed feed) {
        FeedBuilder feedBuilder = new TestFeedBuilder();
        LogBuilder logBuilder = new TestLogBuilder();
        Interpreter interpeter = new Processor(feedBuilder);
        RunningInterpreter executor = new RunningProcessor(interpeter);
        BlackboardSource blackboard = new BlackboardSource("Processor.BlackboardSource");
        DocumentMediator documentMediator = new DummyDocumentMediator();
        SheetFactory sheetFactory = new TestSheetFactory();
        Log log = new TestLog();

        InterpreterContext context = new InterpreterContext(executor, blackboard);
        context.set(feed, log, documentMediator, sheetFactory);
        return context;
    }
}
