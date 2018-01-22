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
package org.mazarineblue.executors.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.ExecutorListener;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedExecutorOutputSpy
        implements ExecutorListener {

    private boolean printHelp = false;
    private final List<String> processingFiles;
    private final List<Feed> processingFeeds;
    private final List<String> missingFiles;
    private final List<String> notSupportedFileFormats;
    private final List<String> unreadableFiles;
    private final List<String> unwritableFiles;
    private final List<Exception> thrownExceptions;

    public FeedExecutorOutputSpy() {
        this(16);
    }

    public FeedExecutorOutputSpy(int initialCapacity) {
        missingFiles = new ArrayList<>(initialCapacity);
        processingFiles = new ArrayList<>(initialCapacity);
        processingFeeds = new ArrayList<>(initialCapacity);
        notSupportedFileFormats = new ArrayList<>(initialCapacity);
        unreadableFiles = new ArrayList<>(initialCapacity);
        unwritableFiles = new ArrayList<>(initialCapacity);
        thrownExceptions = new ArrayList<>(initialCapacity);
    }

    public void clear() {
        missingFiles.clear();
        processingFiles.clear();
        processingFeeds.clear();
        notSupportedFileFormats.clear();
        unreadableFiles.clear();
        unwritableFiles.clear();
        thrownExceptions.clear();
    }

    @Override
    public void start() {
        // We do not need to spy on this event.
    }

    @Override
    public void stop() {
        // We do not need to spy on this event.
    }

    @Override
    public void printHelp() {
        printHelp = true;
    }

    public boolean helpPrinted() {
        return printHelp;
    }

    @Override
    public void reportProcessingFeed(File file) {
        processingFiles.add(file.toString());
    }

    public String[] getProcessingFiles() {
        return processingFiles.toArray(new String[processingFiles.size()]);
    }

    @Override
    public void reportProcessingFeed(Feed feed) {
        processingFeeds.add(feed);
    }

    public Feed[] getProcessingFeeds() {
        return processingFeeds.toArray(new Feed[processingFeeds.size()]);
    }

    @Override
    public void reportFileMissing(File file) {
        missingFiles.add(file.toString());
    }

    public String[] getMissingFiles() {
        return missingFiles.toArray(new String[missingFiles.size()]);
    }

    @Override
    public void reportFileNotSupported(File file) {
        notSupportedFileFormats.add(file.toString());
    }

    public String[] getFilesNotSupported() {
        return notSupportedFileFormats.toArray(new String[notSupportedFileFormats.size()]);
    }

    @Override
    public void reportUnreadableFile(File file) {
        unreadableFiles.add(file.toString());
    }

    public String[] getUnreadableFiles() {
        return unreadableFiles.toArray(new String[unreadableFiles.size()]);
    }

    @Override
    public void reportUnwritableFile(File file) {
        unwritableFiles.add(file.toString());
    }

    public String[] getUnwritableFiles() {
        return unwritableFiles.toArray(new String[unwritableFiles.size()]);
    }

    @Override
    public void reportException(Exception ex) {
        thrownExceptions.add(ex);
    }

    public Exception[] getThrownExceptions() {
        return thrownExceptions.toArray(new Exception[thrownExceptions.size()]);
    }

    public Class<?>[] getThrownExceptionClasses() {
        return thrownExceptions.stream()
                .map(ex -> ex.getClass())
                .toArray(t -> new Class<?>[thrownExceptions.size()]);
    }

    public void throwFirstException() {
        thrownExceptions.stream().findFirst().ifPresent(ex -> {
            throw (RuntimeException) ex;
        });
    }
}
