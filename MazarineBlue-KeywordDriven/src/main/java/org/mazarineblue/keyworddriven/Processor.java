/*
 * Copyright (c) 2015 Alex de Kruijff
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

import java.util.Map;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.exceptions.ConsumableException;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.librarymanager.LibraryManager;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.proceduremanager.ProcedureManager;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Processor
        implements Interpreter {

    protected final ManagerFacade managerFacade;

    static public ConsumableException convertException(Throwable ex) {
        return convertException(ex.getMessage(), ex);
    }

    @SuppressWarnings({"AssignmentToMethodParameter", "ThrowableResultIgnored"})
    static public ConsumableException convertException(String msg, Throwable ex) {
        if (ex.getCause() != null)
            ex = ex.getCause();
        return ex instanceof ConsumableException
                ? (ConsumableException) ex
                : new ConsumableException(msg, ex);
    }

    public Processor(FeedBuilder feedBuilder) {
        managerFacade = new ManagerFacade(feedBuilder);
    }

    @Override
    public LibraryManager libraries() {
        return managerFacade.libraries();
    }

    @Override
    public ProcedureManager procedures() {
        return managerFacade.procedures();
    }

    @Override
    public Chain chain() {
        return managerFacade.chain();
    }

    @Override
    public void publish(Event event) {
        managerFacade.publish(event);
    }

    @Override
    public void execute(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory,
                        Map<String, Object> presetVariables) {
        init().execute(feed, log, documentMediator, sheetFactory, presetVariables);
    }

    @Override
    public void validate(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory) {
        init().validate(feed, log, documentMediator, sheetFactory);
    }

    public RunningInterpreter init() {
        return new RunningProcessor(this);
    }
}
