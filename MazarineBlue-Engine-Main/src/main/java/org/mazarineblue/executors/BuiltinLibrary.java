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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.parser.VariableInvokerSource;
import org.mazarineblue.parser.expressions.Expression;
import static org.mazarineblue.plugins.LibraryService.getLibraries;
import static org.mazarineblue.plugins.LibraryService.hasLibrary;
import org.mazarineblue.variablestore.VariableStoreSubscriber;

/**
 * The {@code BuiltinLibrary} containsSheet the instructions that a build into the
 * {@link DefaultProcessorFactory}.
 *
 * @see DefaultProcessorFactory
 * @see VariableStoreSubscriber
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BuiltinLibrary
        extends AbstractMainLibrary {

    private FileSystem fs = new DiskFileSystem();

    BuiltinLibrary() {
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SetFileSystemEvent
     */
    @EventHandler
    public void eventHandler(SetFileSystemEvent event) {
        fs = event.getFileSystem();
        // do not consume event, so other subscribers also can set the file system
    }
    
    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SaveInputStreamAs
     */
    @EventHandler
    public void eventHandler(CallFileSystemMethodEvent event) {
        event.invokeMethod(fs);
        event.setConsumed(true);
    }

    @PassInvoker
    @Keyword("Import library")
    @Parameters(min = 1)
    public void importLibrary(Invoker invoker, String namespace) {
        if (!hasLibrary(namespace))
            throw new LibraryNotFoundException(namespace);
        importLibraries(invoker, getLibraries(namespace));
    }

    private void importLibraries(Invoker invoker, List<Library> libraries) {
        libraries.stream()
                .map(AddLibraryEvent::new)
                .forEach(invoker::publish);
    }

    @Keyword("Comment")
    public void comment(Object... args) {
        /* A comment is implemented as an instruction. so that the framework is
         * kept simple. A comment can be implemented as a special case or as an
         * instruction that does noting.
         */
    }

    @Keyword("Expression is true")
    @PassInvoker
    public boolean expressionIsTrue(Invoker invoker, Expression expression) {
        return Objects.equals(TRUE, evaluateExpression(invoker, expression));
    }

    @Keyword("Expression is false")
    @PassInvoker
    public boolean expressionIsFalse(Invoker invoker, Expression expression) {
        return Objects.equals(FALSE, evaluateExpression(invoker, expression));
    }

    @Keyword("Evaluate expression")
    @PassInvoker
    public final Object evaluateExpression(Invoker invoker, Expression expression) {
        return expression.evaluate(new VariableInvokerSource(invoker));
    }

    @EventHandler
    public void eventHandler(EvaluateExpressionEvent event) {
        event.evaluate(new VariableInvokerSource(event.invoker()));
    }
}
