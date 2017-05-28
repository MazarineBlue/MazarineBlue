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
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.libraries.DefaultLibrary;
import org.mazarineblue.logger.EngineLogger;
import org.mazarineblue.logger.events.EngineLoggerEvent;
import org.mazarineblue.plugins.FeedService;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.util.DateFactory;
import org.mazarineblue.util.FileTransformer;
import org.mazarineblue.variablestore.ScopedVariableStoreLibrary;
import org.mazarineblue.variablestore.events.VariableStoreEvent;

public final class DefaultFeedExecutor
        implements FeedExecutor {

    private final File base;
    private final FileSystem fs;
    private final FeedExecutorOutput output;
    private final FileTransformer fileTransformer;
    private final DateFactory dateFactory;
    private final EngineLogger logger;

    private final EventBusLink eventBus;
    private final Interpreter interpreter;
    private boolean containsErrors = false;

    DefaultFeedExecutor(FeedExecutorBuilder builder) {
        this.base = builder.getBaseFile();
        this.fs = builder.getFs();
        this.output = builder.getOutput();
        this.fileTransformer = builder.getFileTransformer();

        dateFactory = DateFactory.getDefaultInstance();
        logger = new EngineLogger(dateFactory);

        eventBus = new EventBusLink();
        eventBus.subscribe(KeywordDrivenEvent.class, null, new LibraryRegistry(new DefaultLibrary()));
        eventBus.subscribe(EngineLoggerEvent.class, null, logger);
        eventBus.subscribe(VariableStoreEvent.class, null, new ScopedVariableStoreLibrary());

        interpreter = Interpreter.getDefaultInstance();
        interpreter.setInterpreterListener(logger);
        interpreter.addLink(eventBus);
    }

    @Override
    public void addSubscriber(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        eventBus.subscribe(eventType, filter, subscriber);
    }

    @Override
    public void addLinkAfterEventBus(Link link) {
        interpreter.addLink(link, eventBus);
    }

    @Override
    public void addLink(Link link) {
        interpreter.addLink(link);
    }

    @Override
    public void execute(File file, String sheet) {
        if (!fs.exists(file)) {
            containsErrors = true;
            output.reportFileMissing(file);
            containsErrors = true;
            return;
        }
        Feed feed = createFeed(file, sheet);
        if (feed != null && doExecute(feed))
            writeLog(fileTransformer.getLogfile(file.getParentFile(), file.getName()));
    }

    private Feed createFeed(File file, String sheet) {
        try {
            output.reportProcessingFeed(file);
            return FeedService.createFeed(fs, file, sheet);
        } catch (FileNotSupportedException ex) {
            containsErrors = true;
            output.reportFileNotSupported(file);
        } catch (FileUnreadableException ex) {
            containsErrors = true;
            output.reportUnreadableFile(file);
        } catch (RuntimeException ex) {
            containsErrors = true;
            output.reportException(ex);
        }
        return null;
    }

    @Override
    public void execute(Feed feed) {
        if (doExecute(feed))
            writeLog(fileTransformer.getLogfile(base.getParentFile(), base.getName()));
    }

    private boolean doExecute(Feed feed) {
        try {
            output.reportProcessingFeed(feed);
            interpreter.execute(feed);
            return true;
        } catch (Exception ex) {
            containsErrors = true;
            output.reportException(ex);
            return false;
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void writeLog(File file) {
        try {
            fs.mkfile(file, logger.toString());
        } catch (Exception ex) {
            containsErrors = true;
            output.reportUnwritableFile(file);
        }
    }

    @Override
    public boolean containsErrors() {
        return containsErrors;
    }

    @Override
    public void setFeedExecutorListener(FeedExecutorListener listener) {
        interpreter.setFeedExecutorListener(listener);
    }

    @Override
    public void setInterpreterListener(InterpreterListener listener) {
        interpreter.setInterpreterListener(listener);
    }

    @Override
    public void setChainModifierListener(ChainModifierListener listener) {
        interpreter.setChainModifierListener(listener);
    }

    @Override
    public void setPublisherListener(PublisherListener listener) {
        interpreter.setPublisherListener(listener);
    }
}
