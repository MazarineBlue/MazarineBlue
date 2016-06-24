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
package org.mazarineblue.keyworddriven.librarymanager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.events.GetDataSourceEvent;
import org.mazarineblue.events.library.FetchLibrariesEvent;
import org.mazarineblue.events.library.FetchLibraryEvent;
import org.mazarineblue.keyworddriven.Beta;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.RunningInterpreter;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.logs.LogBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Library
        implements ReflectionSubscriber<Event> {

    private final String namespace;
    private final Map<String, Instruction> keywords = new HashMap<>(4);
    private EventService<Event> events;
    private InterpreterContext context;
    private LogBuilder logBuilder;

    protected Library(Library library) {
        this(library.namespace, library);
    }

    protected Library(String namespace, Library library) {
        this(namespace);
        setEvents(library.events);
        setContext(library.context);
    }

    protected Library(String namespace) {
        this.namespace = namespace;
        registerInstructions(getClass());
    }

    @Override
    public String toString() {
        return "Library{" + "namespace=" + namespace + ", class=" + getClass() + '}';
    }

    public void setLogBuilder(LogBuilder logBuilder) {
        this.logBuilder = logBuilder;
    }

    /**
     * Registers the EventService of the calling interpreter with this library.
     *
     * @param service the EventService to set
     */
    protected void setEvents(EventService service) {
        if (this.events != null)
            this.events.unsubscribe(Event.class, null, this);
        this.events = service;
        subscribingMyEventHandlers();
    }

    private void subscribingMyEventHandlers() {
        if (this.events != null)
            this.events.subscribe(Event.class, null, this);
    }

    public final void setup(Library library) {
        setContext(library.context);
        setup();
    }

    final void setContext(InterpreterContext context) {
        this.context = context;
    }

    private <T> void registerInstructions(Class<T> instructionClass) {
        for (Method method : instructionClass.getMethods()) {
            if (Library.class.isAssignableFrom(method.getDeclaringClass()) == false)
                continue;
            registersKeywords(method);
        }
    }

    private void registersKeywords(Method method) {
        for (Keyword keyword : method.getAnnotationsByType(Keyword.class)) {
            Instruction instruction = new Instruction(this, keyword, method);
            if (method.getAnnotationsByType(Beta.class).length > 0)
                instruction.setBeta(true);
            if (method.getAnnotationsByType(Deprecated.class).length > 0)
                instruction.setDeprecated(true);
            setInstruction(instruction);
        }
    }

    final void setInstruction(Instruction instruction) {
        String keyword = instruction.getKeyword();
        if (keywords.containsKey(keyword))
            throw new KeywordConflictException(keyword);
        keywords.put(keyword, instruction);
    }

    private boolean equalMethods(Method expected, Method actual) {
        if (expected.getName().equals(actual.getName()) == false)
            return false;

        Class[] expectedClazz = expected.getParameterTypes();
        Class[] actualClazz = actual.getParameterTypes();
        if (expectedClazz.length != actualClazz.length)
            return false;
        for (int i = 0; i < expectedClazz.length; ++i)
            if (expectedClazz[i].isAssignableFrom(actualClazz[i]) == false)
                return false;
        return true;
    }

    public DataSource getDataSource() {
        return getDataSource(null);
    }

    public final DataSource getDataSource(String name) {
        GetDataSourceEvent event = new GetDataSourceEvent();
        events.publish(event);
        return event.get(DataSource.class);
    }

    /**
     * Return the namespace of this library.
     *
     * @return the namespace of this library
     */
    public final String getNamespace() {
        return namespace;
    }

    /**
     * Returns all the keywords that this library contains instructions for.
     */
    public final Set<String> getKeywords() {
        return keywords.keySet(); //.toArray(new String[keywords.size()])
    }

    /**
     * Returns all the instruction that this library hold.
     */
    public final Collection<Instruction> getInstructions() {
        return keywords.values();
    }

    /**
     * Returns the instruction registered under the specified keyword.
     */
    public final Instruction getInstruction(String keyword) {
        return keywords.get(keyword);
    }

    /**
     * Fired events are send to the libraries that are registered with the
     * executor.
     *
     * Event fired are first send through the chain-of-command and then send to
     * the libraries.
     */
    public void publish(Event event) {
        executor().publish(event);
    }

    /**
     *
     */
    public final InterpreterContext context() {
        return context;
    }

    /**
     * Return the interpreter when executing or validating a feed.
     *
     * @return null when no instructions are executing or validating.
     */
    public final RunningInterpreter executor() {
        return context == null ? null : context.executor();
    }

    /**
     * Returns the feed holding the instructions.
     *
     * @return null when no instructions are executing or validating.
     */
    public final Feed feed() {
        return context == null ? null : context.feed();
    }

    /**
     * Returns the log when executing or validating a feed.
     *
     * @return null when no instructions are executing or validating.
     */
    public final Log log() {
        return context == null ? logBuilder.createLog() : context.log();
    }

    /**
     * Returns the variable store of the interpreter.
     *
     * @return null when no instructions are executing or validating.
     */
    public final BlackboardSource blackboard() {
        return context == null ? null : context.blackboard();
    }

    public final SheetFactory sheetFactory() {
        return context == null ? null : context.sheetFactory();
    }

    public final DocumentMediator documentMediator() {
        return context == null ? null : context.documentMediator();
    }

    protected abstract void setup();

    protected abstract void teardown();

    @EventHandler
    public void eventHandler(FetchLibraryEvent event) {
        if (event.containsLibraryClass(getClass()) == false)
            return;
        event.setLibrary(this);
    }

    @EventHandler
    public void eventHandler(FetchLibrariesEvent event) {
        if (event.containsLibraryClass(getClass()) == false)
            return;
        event.addLibrary(this);
    }
}
