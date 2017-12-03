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
import org.mazarineblue.logger.EngineLogger;
import org.mazarineblue.logger.events.EngineLoggerEvent;
import org.mazarineblue.plugins.FeedService;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.util.DateFactory;
import org.mazarineblue.util.FileTransformer;
import org.mazarineblue.variablestore.VariableStoreLibrary;

public final class DefaultFeedExecutor
        implements FeedExecutor {

    private final File base;
    private final FileSystem fs;
    private final FeedExecutorOutput output;
    private final FileTransformer fileTransformer;
    private final EngineLogger logger;

    private final EventBusLink eventBus;
    private final Interpreter interpreter;
    private boolean containsErrors = false;

    private final BuiltinLibrary commonLibrary;

    DefaultFeedExecutor(FeedExecutorBuilder builder) {
        this.base = builder.getBaseFile();
        this.fs = builder.getFileSystem();
        this.output = builder.getOutput();
        this.fileTransformer = builder.getFileTransformer();

        DateFactory dateFactory = DateFactory.newInstance();
        logger = new EngineLogger(dateFactory);

        eventBus = new EventBusLink();
        commonLibrary = new BuiltinLibrary();
        VariableStoreLibrary variableStore = new VariableStoreLibrary();
        eventBus.subscribe(Event.class, null, new LibraryRegistry(commonLibrary, variableStore));
        eventBus.subscribe(EngineLoggerEvent.class, null, logger);

        interpreter = Interpreter.newInstance();
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
        output.start();
        if (!fs.exists(file))
            reportMissingFile(file);
        else
            doExecute(file, sheet);
        output.stop();
    }

    private void reportMissingFile(File file) {
        containsErrors = true;
        output.reportFileMissing(file);
    }

    private void doExecute(File file, String sheet) {
        Feed feed = createFeed(file, sheet);
        if (feed == null)
            return;
        String[] sheets = FeedService.getSheetNames(fs, file);
        commonLibrary.addAvailableSheets(file, sheets);
        if (doExecute(feed))
            writeLog(fileTransformer.getLogfile(file.getParentFile(), file.getName()));
        commonLibrary.removeAvailableSheets(file, sheets);
    }

    private Feed createFeed(File file, String sheet) {
        try {
            output.reportProcessingFeed(file);
            return FeedService.createFeed(fs, file, sheet != null ? sheet : FeedService.getFirstSheetName(fs, file));
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
        if (doExecute(feed)) {
            output.reportProcessingFeed(feed);
            writeLog(fileTransformer.getLogfile(base.getParentFile(), base.getName()));
        }
    }

    private boolean doExecute(Feed feed) {
        try {
            interpreter.execute(feed);
            interpreter.close();
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
