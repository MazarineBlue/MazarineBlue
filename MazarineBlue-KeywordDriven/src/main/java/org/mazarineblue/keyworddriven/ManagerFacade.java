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
package org.mazarineblue.keyworddriven;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.eventbus.SimpleEventService;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.librarymanager.LibraryManager;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.links.ConvertLineParametersLink;
import org.mazarineblue.keyworddriven.links.EventServiceLink;
import org.mazarineblue.keyworddriven.links.LibraryDispatcherLink;
import org.mazarineblue.keyworddriven.links.Link;
import org.mazarineblue.keyworddriven.links.ProcedureDispatcherLink;
import org.mazarineblue.keyworddriven.proceduremanager.ProcedureManager;
import org.mazarineblue.parser.variable.VariableParser;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ManagerFacade {

    private final LibraryManager libraryManager;
    private final ProcedureManager procedureManager;
    private final Chain chain;
    private final EventService<Event> eventService;

    public ManagerFacade(FeedBuilder feedBuilder) {
        procedureManager = new ProcedureManager(feedBuilder);
        eventService = new SimpleEventService<>(Event.class);
        libraryManager = new LibraryManager(eventService);
        chain = createChain(procedureManager, libraryManager, eventService);
    }

    public ManagerFacade(Interpreter processor) {
        procedureManager = new ProcedureManager(processor.procedures());
        eventService = new SimpleEventService<>(Event.class);
        libraryManager = new LibraryManager(processor.libraries(), eventService);
        chain = createChain(procedureManager, libraryManager, eventService);
    }

    static private Chain createChain(
            ProcedureManager procedureManager,
            LibraryManager libraryManager,
            EventService<Event> eventService) {
        Link[] links = new Link[4];
        links[0] = new ConvertLineParametersLink(new VariableParser());
        links[1] = new ProcedureDispatcherLink(procedureManager);
        links[2] = new LibraryDispatcherLink(libraryManager);
        links[3] = new EventServiceLink(eventService);

        Chain chain = new Chain(links);
        chain.insertNewLinksAfter(links[1]);
        return chain;
    }

    LibraryManager libraries() {
        return libraryManager;
    }

    void setupLibraries(InterpreterContext context) {
            libraryManager.setup(context);
    }

    void teardownLibraries() {
            libraryManager.teardown();
    }

    public ProcedureManager procedures() {
        return procedureManager;
    }

    public Chain chain() {
        return chain;
    }

    public void publish(Event event) {
        chain.publish(event);
    }

    EventService<Event> eventService() {
        return eventService;
    }
}
