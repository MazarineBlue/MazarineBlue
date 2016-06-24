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
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.librarymanager.LibraryManager;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.proceduremanager.ProcedureManager;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Interpreter {    

    public LibraryManager libraries();

    public ProcedureManager procedures();

    /**
     * The ChainManager object allows manipulation of the chain-of-command.
     *
     * The behavior of this interpreter can changed by adding or removing links
     * to or from the chain-of-command. Events are first send though the
     * chain-of-command and when consumed never are passed to the next link of
     * the libraries.
     *
     * @return the manager of the chain-of-command.
     */
    public Chain chain();

    /**
     * Fired events are send to the libraries that are registered with the
     * executor.
     *
     * Event fired are first send through the chain-of-command and then send to
     * the libraries.
     *
     * @param event the event to publish to chain-of-command followed by the
     * libraries.
     */
    public void publish(Event event);

    /**
     * Executes the instructions of the specified feed and reports to the
     * specified log.
     *
     * The interpreter switches to validation up on the first instruction that
     * thought an exception.
     *
     * @param feed holding all the instruction to be executed
     * @param log the reporting instance
     * @param documentMediator
     * @param sheetFactory
     */
    default public void execute(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory) {
        execute(feed, log, documentMediator, sheetFactory, null);
    }

    public void execute(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory,
                        Map<String, Object> presetVariables);

    /**
     * Validates all the instruction of the specified feed and report to the
     * specified log.
     *
     * @param feed holding all the instruction to be validated
     * @param log the reporting instance
     * @param documentMediator
     * @param sheetFactory
     */
    public void validate(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory);
}
