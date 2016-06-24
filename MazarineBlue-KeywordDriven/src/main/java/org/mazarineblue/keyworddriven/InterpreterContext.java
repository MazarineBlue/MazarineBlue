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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.proceduremanager.Procedure;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.keyworddriven.util.HasContext;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InterpreterContext
        implements HasContext {

    private RunningInterpreter executor;
    private Feed feed;
    private Log log;
    private BlackboardSource blackboard;
    private DocumentMediator documentMediator;
    private SheetFactory sheetFactory;
    private Date startDate;
    private final List<InstructionLine> lines = new ArrayList<>();

    InterpreterContext() {
    }

    InterpreterContext(RunningInterpreter executor, BlackboardSource blackboard) {
        this.executor = executor;
        this.blackboard = blackboard;
    }

    private InterpreterContext(RunningInterpreter executor, Feed feed, Log log,
                               BlackboardSource blackboard,
                               DocumentMediator documentMediator,
                               SheetFactory sheetFactory) {
        this.executor = executor;
        this.feed = feed;
        this.log = log;
        this.blackboard = blackboard;
        this.documentMediator = documentMediator;
        this.sheetFactory = sheetFactory;
    }

    void reset() {
        this.startDate = null;
    }

    void setup(Date startDate, InstructionLine line) {
        this.startDate = startDate;
        lines.clear();
        add(line);
    }

    @Override
    public InterpreterContext context() {
        return this;
    }

    @Override
    public RunningInterpreter executor() {
        return executor;
    }

    @Override
    public Feed feed() {
        return feed;
    }

    @Override
    public Log log() {
        return log;
    }

    @Override
    public BlackboardSource blackboard() {
        return blackboard;
    }

    @Override
    public DocumentMediator documentMediator() {
        return documentMediator;
    }

    @Override
    public SheetFactory sheetFactory() {
        return sheetFactory;
    }

    public Date startDate() {
        return startDate;
    }

    public void setExecutor(RunningInterpreter executor) {
        this.executor = executor;
    }

    public void setBlackboard(BlackboardSource blackboard) {
        this.blackboard = blackboard;
    }

    void set(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory) {
        this.feed = feed;
        this.log = log;
        this.documentMediator = documentMediator;
        this.sheetFactory = sheetFactory;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setDocumentMediator(DocumentMediator documentMediator) {
        this.documentMediator = documentMediator;
    }

    public void setSheetFactory(SheetFactory sheetFactory) {
        this.sheetFactory = sheetFactory;
    }

    public final void add(InstructionLine line) {
        lines.add(line);
    }

    public InstructionLine getUnprocessedLine() {
        return lines.get(0);
    }

    public InstructionLine[] getLines() {
        InstructionLine[] arr = new InstructionLine[lines.size()];
        return lines.toArray(arr);
    }

    public void executeNested(InstructionLine line, Procedure procedure, Feed feed) {
        blackboard.pushSource("ProcedureStackSource");
        procedure.copyParameters(blackboard, line);
        executor.executeNested(feed, log, this);
        blackboard.popSource("ProcedureStackSource");
    }
}
