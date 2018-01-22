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
package org.mazarineblue.executors;

import java.io.File;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.mazarineblue.engine.nestedinstructions.NestedInstructionExecutor;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.executors.util.FileTransformer;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.logger.EngineLogger;
import org.mazarineblue.parser.ExpressionBuilderParser;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.expressions.Expression;
import org.mazarineblue.plugins.FeedService;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.util.DateFactory;
import org.mazarineblue.variablestore.VariableStoreSubscriber;
import org.mazarineblue.variablestore.subscribers.VariableParserSubscriber;

public final class DefaultExecutor
        implements Executor {

    private final File base;
    private final FileSystem fs;
    private final ExecutorListener listener;
    private final FileTransformer fileTransformer;
    private static final String GLOBAL = "global";

    private final Processor processor;
    private boolean containsErrors = false;

    private final EngineLogger logger;
    private final VariableParserSubscriber variableParser;
    private final LibraryRegistry libraryRegistry;

    private final BuiltinFeedLibrary feedLibrary;

    DefaultExecutor(ExecutorBuilder builder) {
        this.base = builder.getBaseFile();
        this.fs = builder.getFileSystem();
        this.listener = builder.getListener();
        this.fileTransformer = builder.getFileTransformer();
        FunctionRegistry functionRegistry = new FunctionRegistry();

        feedLibrary = new BuiltinFeedLibrary();
        libraryRegistry = new LibraryRegistry(feedLibrary,
                                              new BuiltinLibrary(),
                                              new BuiltinVariableStoreLibrary(GLOBAL),
                                              new BuiltinFunctionsLibrary(functionRegistry));
        Parser<String, Expression> parser = new ExpressionBuilderParser();
        libraryRegistry.eventHandler(new RegisterConversionRuleEvent<>(Date.class, String.class, Convertors::toString));
        libraryRegistry.eventHandler(new RegisterConversionRuleEvent<>(String.class, Date.class, Convertors::toDate));
        libraryRegistry.eventHandler(new RegisterConversionRuleEvent<>(String.class, Duration.class, Convertors::toDuration));
        libraryRegistry.eventHandler(new RegisterConversionRuleEvent<>(String.class, Expression.class, parser::parse));
        libraryRegistry.eventHandler(new RegisterConversionRuleEvent<>(String.class, TimeUnit.class, Convertors::toTimeUnit));

        VariableStoreSubscriber variableStore = new VariableStoreSubscriber(GLOBAL);
        variableParser = new VariableParserSubscriber(variableStore);
        logger = new EngineLogger(DateFactory.newInstance());

        processor = Processor.newInstance();
        processor.setChainModifierListener(logger);
        processor.setFeedExecutorListener(logger);
        processor.setPublisherListener(logger);

        processor.addLink(new FunctionSubscriber(functionRegistry)); // This must stay at the top
        processor.addLink(libraryRegistry); // This must stay at the top
        processor.addLink(logger);
        processor.addLink(variableStore);
        processor.addLink(new NestedInstructionExecutor());
        processor.addLink(variableParser); // This must stay at the bottom
    }

    @Override
    public void addLink(Subscriber<Event> link) {
        processor.addLink(link);
    }

    @Override
    public void addLinkAfterVariableParser(Subscriber<Event> link) {
        processor.addLink(link, variableParser);
    }

    @Override
    public void addLinkAfterLibraryRegistry(Subscriber<Event> link) {
        processor.addLink(link, libraryRegistry);
    }

    @Override
    public void execute(File file, String sheet) {
        listener.start();
        if (!fs.exists(file))
            reportMissingFile(file);
        else
            doExecute(file, sheet);
        listener.stop();
    }

    private void reportMissingFile(File file) {
        containsErrors = true;
        listener.reportFileMissing(file);
    }

    private void doExecute(File file, String sheet) {
        Feed feed = createFeed(file, sheet);
        if (feed == null)
            return;
        String[] sheets = FeedService.getSheetNames(fs, file);
        feedLibrary.addAvailableSheets(file, sheets);
        if (doExecute(feed))
            writeLog(fileTransformer.getLogfile(file.getParentFile(), file.getName()));
        feedLibrary.removeAvailableSheets(file, sheets);
    }

    private Feed createFeed(File file, String sheet) {
        try {
            listener.reportProcessingFeed(file);
            return FeedService.createFeed(fs, file, sheet != null ? sheet : FeedService.getFirstSheetName(fs, file));
        } catch (FileNotSupportedException ex) {
            containsErrors = true;
            listener.reportFileNotSupported(file);
        } catch (FileUnreadableException ex) {
            containsErrors = true;
            listener.reportUnreadableFile(file);
        } catch (RuntimeException ex) {
            containsErrors = true;
            listener.reportException(ex);
        }
        return null;
    }

    @Override
    public void execute(Feed feed) {
        if (doExecute(feed)) {
            listener.reportProcessingFeed(feed);
            writeLog(fileTransformer.getLogfile(base.getParentFile(), base.getName()));
        }
    }

    private boolean doExecute(Feed feed) {
        try {
            processor.execute(feed);
            processor.close();
            return true;
        } catch (Exception ex) {
            containsErrors = true;
            listener.reportException(ex);
            return false;
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void writeLog(File file) {
        try {
            fs.mkfile(file, logger.toString());
        } catch (Exception ex) {
            containsErrors = true;
            listener.reportUnwritableFile(file);
        }
    }

    @Override
    public boolean containsErrors() {
        return containsErrors;
    }

    @Override
    public void setChainModifierListener(ChainModifierListener listener) {
        processor.setChainModifierListener(listener);
    }

    @Override
    public void setFeedExecutorListener(FeedExecutorListener listener) {
        processor.setFeedExecutorListener(listener);
    }

    @Override
    public void setPublisherListener(PublisherListener listener) {
        processor.setPublisherListener(listener);
    }
}
