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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.exceptions.UnbalancedScopeException;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import static org.mazarineblue.plugins.FeedService.createFeed;
import static org.mazarineblue.plugins.FeedService.getSheetNames;
import static org.mazarineblue.plugins.LibraryService.getLibraries;
import static org.mazarineblue.plugins.LibraryService.hasLibrary;
import org.mazarineblue.utililities.exceptions.NeverThrownException;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

/**
 * The {@code BuiltinLibrary} containsSheet the instructions that a build into the
 * {@link DefaultInterpreterFactory}.
 *
 * @see DefaultInterpreterFactory
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BuiltinLibrary
        extends Library {

    private final Deque<SheetOptions> sheetStack = new ArrayDeque<>();
    private FileSystem fs = new DiskFileSystem();

    BuiltinLibrary() {
        super("org.mazarineblue");
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SetFileSystemEvent
     */
    @EventHandler
    public void eventHandler(SetFileSystemEvent event) {
        fs = event.getFileSystem();
    }

    @PassInvoker
    @Keyword("Import library")
    @Parameters(min = 1)
    public void importLibrary(Invoker invoker, String namespace) {
        if (!hasLibrary(namespace))
            throw new LibraryNotFoundException(namespace);
        List<Library> libraries = getLibraries(namespace);
        setupLibraries(invoker, libraries);
        importLibraries(invoker, libraries);
    }

    private void setupLibraries(Invoker invoker, List<Library> libraries) {
        libraries.stream().forEach(lib -> lib.doSetup(invoker));
    }

    private void importLibraries(Invoker invoker, List<Library> libraries) {
        libraries.stream()
                .map(AddLibraryEvent::new)
                .forEach(invoker::publish);
    }

    @Keyword("Comment")
    public void comment(Object... args) {
        // A comment does nothing and theirfore is implementing by doing nothing
    }

    @PassInvoker
    @Keyword("Import feed")
    @Parameters(min = 1)
    public void importFeed(Invoker invoker, String file, String sheet) {
        File f = new File(file);
        String[] sheets = getSheetNames(fs, f);
        addAvailableSheets(f, sheets);
        doImportSheet(invoker, sheet);
        removeAvailableSheets(f, sheets);
    }

    @PassInvoker
    @Keyword("Call feed")
    @Parameters(min = 1)
    public void callFeed(Invoker invoker, String file, String sheet) {
        File f = new File(file);
        String[] sheets = getSheetNames(fs, f);
        addAvailableSheets(f, sheets);
        doCallSheet(invoker, sheet);
        removeAvailableSheets(f, sheets);
    }

    @PassInvoker
    @Keyword("Import sheet")
    @Parameters(min = 1)
    public void importSheet(Invoker invoker, String sheet) {
        validateAvailablilitySheet(sheet);
        doImportSheet(invoker, sheet);
    }

    @PassInvoker
    @Keyword("Call sheet")
    @Parameters(min = 1)
    public void callSheet(Invoker invoker, String sheet) {
        validateAvailablilitySheet(sheet);
        doCallSheet(invoker, sheet);
    }

    private void validateAvailablilitySheet(String sheet) {
        SheetOptions peek = sheetStack.peek();
        if (peek == null)
            throw new UnbalancedScopeException(sheet);
        if (!peek.containsSheet(sheet))
            throw new UnbalancedScopeException(sheet);
    }

    void addAvailableSheets(File file, String[] sheets) {
        sheetStack.push(new SheetOptions(file, sheets));
    }

    private void doImportSheet(Invoker invoker, String sheet) {
        File f = sheetStack.peek().getFile();
        Feed feed = sheet == null ? createFeed(fs, f) : createFeed(fs, f, sheet);
        invoker.interpreter().execute(feed);
    }

    private void doCallSheet(Invoker invoker, String sheet) {
        File f = sheetStack.peek().getFile();
        Feed feed = sheet == null ? createFeed(fs, f) : createFeed(fs, f, sheet);
        invoker.publish(new StartVariableScopeEvent());
        invoker.interpreter().execute(feed);
        invoker.publish(new EndVariableScopeEvent());
    }

    void removeAvailableSheets(File file, String[] sheets) {
        SheetOptions expected = new SheetOptions(file, sheets);
        SheetOptions actual = sheetStack.pop();
        if (!expected.equals(actual))
            throw new NeverThrownException();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
