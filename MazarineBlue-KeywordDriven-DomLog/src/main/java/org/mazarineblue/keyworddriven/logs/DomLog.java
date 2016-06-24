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
package org.mazarineblue.keyworddriven.logs;

import java.io.IOException;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.exceptions.SubscriberTargetException;
import org.mazarineblue.events.log.LogDecrementNestedLevelEvent;
import org.mazarineblue.events.log.LogIncrementNestedLevelEvent;
import org.mazarineblue.events.log.LogInstructionLineEvent;
import org.mazarineblue.events.log.LogStatusEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import static org.mazarineblue.keyworddriven.Processor.convertException;
import org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.logs.dom.CollectionElement;
import org.mazarineblue.keyworddriven.logs.dom.Composite;
import org.mazarineblue.keyworddriven.logs.dom.LineElement;
import org.mazarineblue.keyworddriven.logs.dom.Status;
import org.mazarineblue.keyworddriven.logs.dom.StatusLeaf;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DomLog
        implements Log {

    private final Chain chain;
    private final Deque<Composite> stack = new LinkedList<>();
    private final CollectionElement root;
    private LineElement currentLineElement;

    public DomLog(Chain chain, Date startDate) {
        this.chain = chain;
        root = new CollectionElement(startDate);
        stack.add(root);
    }

    public void visitor(LogVisitor visitor)
            throws IOException {
        root.accept(visitor);
    }

    @Override
    public void next(InstructionLine line, DataSource dataSource, InterpreterContext context,
                     ProcessingType processingType) {
        currentLineElement = new LineElement(line, context.startDate(), processingType);
        if (line.isComment() == false)
            stack.peek().add(currentLineElement);
        stack.push(currentLineElement);
    }

    private void publish(Event event) {
        try {
            chain.publish(event);
        } catch (SubscriberTargetException ex) {
            throw convertException(ex);
        }
    }

    @Override
    public void setActualParameters(Object[] actualParameters) {
        currentLineElement.setActualParameters(actualParameters);
    }

    @Override
    public void done(Date endDate) {
        currentLineElement = null;
        Composite element = stack.pop();
        element.done(endDate);
        if (stack.isEmpty())
            root.done(endDate);
        else
            stack.peek().done(endDate);
        publish(new LogInstructionLineEvent(element));
    }

    @Override
    public void incrementNestedInstruction(Date startDate) {
        CollectionElement collection = new CollectionElement(startDate);
        stack.peek().add(collection);
        stack.push(collection);
        publish(new LogIncrementNestedLevelEvent(collection));
    }

    @Override
    public void decrementNestedInstruction(Date endDate) {
        Composite element = stack.pop();
        element.done(endDate);
        publish(new LogDecrementNestedLevelEvent(element));
    }

    @Override
    public void info(String message) {
        StatusLeaf leaf = stack.peek().add(Status.INFO, message);
        publish(new LogStatusEvent(leaf));
    }

    protected InstructionLine getCurrentLine() {
        return currentLineElement == null ? null : currentLineElement.getInstructionLine();
    }

    @Override
    public void info(Exception ex) {
        StatusLeaf leaf = stack.peek().add(Status.INFO, ex);
        publish(new LogStatusEvent(leaf));
    }

    @Override
    public void warning(String message) {
        StatusLeaf leaf = stack.peek().add(Status.WARNING, message);
        publish(new LogStatusEvent(leaf));
    }

    @Override
    public void warning(Exception ex) {
        StatusLeaf leaf = stack.peek().add(Status.WARNING, ex);
        publish(new LogStatusEvent(leaf));
    }

    @Override
    public void error(String message) {
        StatusLeaf leaf = stack.peek().add(Status.ERROR, message);
        publish(new LogStatusEvent(leaf));
    }

    @Override
    public void error(Throwable ex) {
        StatusLeaf leaf = stack.peek().add(Status.ERROR, ex);
        publish(new LogStatusEvent(leaf));
    }
}
