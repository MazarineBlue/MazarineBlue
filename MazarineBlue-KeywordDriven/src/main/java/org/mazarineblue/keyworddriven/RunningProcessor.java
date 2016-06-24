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

import java.util.Date;
import java.util.Map;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.SourceChain;
import org.mazarineblue.datasources.SourceWrapper;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.events.StartSheetEvent;
import org.mazarineblue.events.instructions.ExecuteInstructionLineEvent;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.events.instructions.ValidateInstructionLineEvent;
import static org.mazarineblue.keyworddriven.Processor.convertException;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.exceptions.ConsumableException;
import org.mazarineblue.keyworddriven.exceptions.InterpreterAlReadyRunningException;
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
public class RunningProcessor
        implements RunningInterpreter {

    static public enum ProcessingType {

        EXECUTED,
        VALIDATED,
    }

    private final BlackboardSource blackboard = new BlackboardSource("Processor.BlackboardSource");
    private final SourceWrapper externalSource = new SourceWrapper();
    private final DataSource source = new SourceChain(blackboard, externalSource);

    private final ManagerFacade managerFacade;

    private final Date startDate;
    private State state = State.WAITING;
    private int nested = 0;
    private long sleep = 0;
    private InstructionLine previousLine;

    public RunningProcessor(Interpreter processor) {
        startDate = new Date();
        managerFacade = new ManagerFacade(processor);
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
    public void setSource(DataSource externalSource) {
        this.externalSource.setSource(externalSource);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void pause() {
        state = State.PAUSED;
    }

    @Override
    public void resume() {
        state = State.RUNNING;
    }

    @Override
    public void cancle() {
        state = State.CANCELED;
    }

    @Override
    public void setSleep(long delay) {
        this.sleep = delay;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void execute(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory,
                 Map<String, Object> presetVariables) {
        new ExecuteInstructionHelper(this)
                .doMain(feed, log, documentMediator, sheetFactory, presetVariables);
    }

    @Override
    public void executeNested(Feed feed, Log log, InterpreterContext context) {
        new ExecuteInstructionHelper(this)
                .doNested(feed, log, context);
    }

    private class ExecuteInstructionHelper
            extends InstructionHelper {

        public ExecuteInstructionHelper(RunningInterpreter executor) {
            super(executor);
        }

        @Override
        protected ProcessingType getProcessingType() {
            return ProcessingType.EXECUTED;
        }

        @Override
        protected Event createInstructionLineEvent(InstructionLine line, InterpreterContext context) {
            InstructionLineEvent event = new ExecuteInstructionLineEvent(line);
            event.setDataSource(source);
            event.setContext(context);
            return event;
        }

        @Override
        protected void sleep(Log log) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                log.warning(ex);
            }
        }

        @Override
        protected ConsumableException processException(Exception ex, Log log) {
            ConsumableException tex = convertException(ex);
            if (tex.isConsumed() == false) {
                log.error(ex);
                tex.setConsumed();
            }
            return tex;
        }

        @Override
        protected void doFinally(Feed feed, Log log, InterpreterContext context) {
            if (feed.hasNext())
                validateNested(feed, log, context);
        }
    }

    @Override
    public void validate(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory) {
        new ValidateInstructionHelper(this)
                .doMain(feed, log, documentMediator, sheetFactory, null);
    }

    @Override
    public void validateNested(Feed feed, Log log, InterpreterContext context) {
        new ValidateInstructionHelper(this)
                .doNested(feed, log, context);
    }

    private class ValidateInstructionHelper
            extends InstructionHelper {

        public ValidateInstructionHelper(RunningInterpreter executor) {
            super(executor);
        }

        @Override
        protected ProcessingType getProcessingType() {
            return ProcessingType.VALIDATED;
        }

        @Override
        protected Event createInstructionLineEvent(InstructionLine line, InterpreterContext context) {
            InstructionLineEvent event = new ValidateInstructionLineEvent(line);
            event.setDataSource(source);
            event.setContext(context);
            return event;
        }

        @Override
        protected void sleep(Log log) {
        }

        @Override
        protected ConsumableException processException(Exception ex, Log log) {
            ConsumableException tex = convertException(ex);
            if (tex.isConsumed() == false) {
                log.error(ex);
                tex.setConsumed();
            }
            return null;
        }

        @Override
        protected void doFinally(Feed feed, Log log, InterpreterContext context) {
        }
    }

    private abstract class InstructionHelper {

        private final InterpreterContext context;

        public InstructionHelper(RunningInterpreter executor) {
            context = createContext(blackboard, executor);
        }

        protected void doMain(Feed feed, Log log, DocumentMediator documentMediator, SheetFactory sheetFactory,
                              Map<String, Object> presetVariables) {
            if (state != State.WAITING)
                throw new InterpreterAlReadyRunningException();
            managerFacade.publish(new StartSheetEvent(null));
            try {
                state = State.RUNNING;
                context.set(feed, log, documentMediator, sheetFactory);
                blackboard.setup(presetVariables);
                doNested(feed, log, context);
            } finally {
                state = State.WAITING;
                blackboard.teardown();
                previousLine = null;
            }
            managerFacade.publish(new EndSheetEvent(null));
        }

        // <editor-fold defaultstate="collapsed" desc="Helper methods for doMain()">
        private InterpreterContext createContext(BlackboardSource blackboard, RunningInterpreter executor) {
            InterpreterContext c = new InterpreterContext();
            c.setBlackboard(blackboard);
            c.setExecutor(executor);
            return c;
        }
        // </editor-fold>

        public final void doNested(Feed feed, Log log, InterpreterContext context) {
            try {
                ++nested;
                incrementNestedInstruction(log, new Date());
                registerLibraries(context);
                doLines(feed, log, context);
            } finally {
                --nested;
                teardownLibraries();
                decrementNestedInstruction(log, new Date());
            }
        }

        // <editor-fold defaultstate="collapsed" desc="Helper methods for doNested()">
        private void incrementNestedInstruction(Log log, Date startDate) {
            if (nested != 1)
                log.incrementNestedInstruction(startDate);
        }

        private void decrementNestedInstruction(Log log, Date endDate) {
            if (nested != 0)
                log.decrementNestedInstruction(endDate);
        }

        private void registerLibraries(InterpreterContext context) {
            if (nested != 1)
                return;
            managerFacade.setupLibraries(context);
        }

        private void teardownLibraries() {
            if (nested == 0)
                managerFacade.teardownLibraries();
        }
        // </editor-fold>

        public final void doLines(Feed feed, Log log, InterpreterContext context) {
            try {
                while (feed.hasNext()) {
                    if (shouldAbort())
                        return;
                    InstructionLine line = feed.next();
                    doLine(line, log, context);
                    waitingForNonPausedState();
                }
            } finally {
                doFinally(feed, log, context);
            }
        }

        protected abstract void doFinally(Feed feed, Log log, InterpreterContext context);

        // <editor-fold defaultstate="collapsed" desc="Helper methods for doLines()">
        private boolean shouldAbort() {
            return state == State.CANCELED;
        }

        private void waitingForNonPausedState() {
            if (state == State.PAUSED)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    state = State.CANCELED;
                }
        }
        // </editor-fold>

        public final void doLine(InstructionLine line, Log log, InterpreterContext context) {
            if (previousLine != null && previousLine.isEmpty() == true && line.isEmpty() == true)
                return;
            try {
                context.setup(new Date(), line);
                log.next(line, source, context, getProcessingType());
                Event event = createInstructionLineEvent(line, context);
                fireLineEvent(event, log);
                sleep(log);
            } finally {
                log.done(new Date());
                context.reset();
                previousLine = line;
            }
        }

        // <editor-fold defaultstate="collapsed" desc="Helper methods for doLines()">
        private void fireLineEvent(Event event, Log log) {
            try {
                managerFacade.publish(event);
            } catch (RuntimeException ex) {
                ConsumableException iex = processException(ex, log);
                if (iex != null)
                    throw iex;
            }
        }
        // </editor-fold>

        protected abstract ProcessingType getProcessingType();

        protected abstract Event createInstructionLineEvent(InstructionLine line, InterpreterContext context);

        protected abstract void sleep(Log log);

        protected abstract ConsumableException processException(Exception ex, Log log);
    }
}
