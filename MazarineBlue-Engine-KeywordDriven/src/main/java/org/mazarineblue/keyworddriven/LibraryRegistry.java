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
package org.mazarineblue.keyworddriven;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.ClosingProcessorEvent;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ContainsConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.keyworddriven.events.IsAbleToProcessInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.InstructionTargetException;
import org.mazarineblue.keyworddriven.exceptions.InvokerIsMissingException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.MethodNotInCallerException;
import org.mazarineblue.keyworddriven.exceptions.MultipleInstructionsFoundException;
import org.mazarineblue.keyworddriven.exceptions.ResultTypeIsIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.utilities.TypeConvertor;
import org.mazarineblue.utilities.exceptions.UnknownIssueException;

/**
 * An {@code LibraryRegistry} is a holder of {@link Library Libraries}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library
 */
public class LibraryRegistry
        extends ReflectionSubscriber<Event> {

    private final TypeConvertor convertor = TypeConvertor.newDefaultInstance();
    private final List<Library> libraries;

    /**
     * Constructs the object contains any number of libraries registered.
     *
     * @param libs the libraries to register.
     */
    public LibraryRegistry(Library... libs) {
        libraries = new ArrayList<>(asList(libs));
    }

    @Override
    public String toString() {
        return "size=" + libraries.size();
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ClosingProcessorEvent
     */
    @EventHandler
    public void eventHandler(ClosingProcessorEvent event) {
        Invoker invoker = event.invoker();
        new ArrayList<>(libraries).stream().forEach(lib -> lib.doTeardown(invoker));
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see RegisterConversionRuleEvent
     */
    @EventHandler
    public void eventHandler(ContainsConversionRuleEvent<?,?> event) {
        event.setConversionMethod(convertor);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see RegisterConversionRuleEvent
     */
    @EventHandler
    public void eventHandler(RegisterConversionRuleEvent<?,?> event) {
        event.registerMethod(convertor);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ContainsLibrariesEvent
     */
    @EventHandler
    public void countLibrary(ContainsLibrariesEvent event) {
        event.contains(libraries);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CountLibrariesEvent
     */
    @EventHandler
    public void countLibrary(CountLibrariesEvent event) {
        event.addToCount(libraries);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see FetchLibrariesEvent
     */
    @EventHandler
    public void eventHandler(FetchLibrariesEvent event) {
        libraries.stream().forEach(event::addLibrary);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see AddLibraryEvent
     */
    @EventHandler
    public void addLibrary(AddLibraryEvent event) {
        Library lib = event.getLibrary();
        lib.doSetup(event.invoker());
        libraries.add(lib);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see RemoveLibraryEvent
     */
    @EventHandler
    public void removeLibrary(RemoveLibraryEvent event) {
        Library lib = event.getLibrary();
        if (libraryIsNotFound(lib))
            throw new LibraryNotFoundException(lib.namespace());
        libraries.remove(lib);
        lib.doTeardown(event.invoker());
        event.setConsumed(true);
    }

    private boolean libraryIsNotFound(Library library) {
        return !libraries.contains(library);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ValidateInstructionLineEvent
     */
    @EventHandler
    public void eventHandler(ValidateInstructionLineEvent event) {
        new ValidateInstructionTask(convertor).doInstruction(event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ExecuteInstructionLineEvent
     */
    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        new ExecuteInstructionTask(convertor).doInstruction(event);
    }

    private abstract static class InstructionTask<E extends InstructionLineEvent> {

        void doInstruction(E event) {
            doInstruction(onLibraries(containingInstruction(event), event.invoker()), event);
        }

        private Predicate<Library> containingInstruction(InstructionLineEvent event) {
            return containingInstruction(event.getNamespace(), event.getKeyword());
        }

        public Predicate<Library> containingInstruction(String namespace, String keyword) {
            return lib -> lib.containsInstruction(namespace, keyword);
        }

        private List<Library> onLibraries(Predicate<Library> condition, Invoker invoker) {
            FetchLibrariesEvent e = new FetchLibrariesEvent(condition);
            invoker.publish(e);
            return e.getLibraries();
        }

        private void doInstruction(List<Library> libraries, E event) {
            if (libraries.isEmpty())
                if (isAbleToExecute(event))
                    return;
                else
                    doInstructionsNotFound(event);
            else if (libraries.size() > 1)
                doMultipleInstructionsFound(event);
            else
                doInstruction(libraries.get(0), event);
            event.setConsumed(true);
        }

        private boolean isAbleToExecute(E event) {
            IsAbleToProcessInstructionLineEvent e = new IsAbleToProcessInstructionLineEvent(event.getPath(), event.getArguments());
            event.invoker().publish(e);
            return e.getResult();
        }

        protected abstract void doInstructionsNotFound(E event);

        protected abstract void doMultipleInstructionsFound(E event);

        protected abstract void doInstruction(Library library, E event);
    }

    private static class InstructionConvertor {

        protected final Instruction instruction;
        private final TypeConvertor convertor;

        private InstructionConvertor(Instruction instruction, TypeConvertor convertor) {
            this.instruction = instruction;
            this.convertor = convertor;
        }

        protected Object[] convertArguments(Object[] args) {
            convertFirstArguments(args);
            convertVariableArguments(args);
            return args;
        }

        private void convertFirstArguments(Object[] args) {
            int n = numberOfArgumentsOutOfVarArgs(args);
            Class<?>[] types = instruction.getParameterTypes();
            for (int i = instruction.isWithInvokerAnnotation() ? 1 : 0; i < n; ++i)
                if (args[i] != null)
                    args[i] = convertor.convert(args[i], types[i]);
        }

        private int numberOfArgumentsOutOfVarArgs(Object[] args) {
            Class<?>[] types = instruction.getParameterTypes();
            int n = args.length;
            return n < types.length ? n
                    : instruction.isVarArgs() ? types.length - 1
                    : types.length;
        }

        private void convertVariableArguments(Object[] args) {
            if (!instruction.isVarArgs())
                return;
            Object[] varArgs = (Object[]) args[args.length - 1];
            Class<?> expected = instruction.getVarArgsBaseType();
            for (int i = 0; i < varArgs.length; ++i)
                varArgs[i] = convertor.convert(varArgs[i], expected);
        }
    }

    private static class ValidateInstructionTask
            extends InstructionTask<ValidateInstructionLineEvent> {

        protected final TypeConvertor convertor;

        ValidateInstructionTask(TypeConvertor convertor) {
            this.convertor = convertor;
        }

        @Override
        protected void doInstructionsNotFound(ValidateInstructionLineEvent event) {
            event.setInstructionsNotFound();
        }

        @Override
        protected void doMultipleInstructionsFound(ValidateInstructionLineEvent event) {
            event.setMultipleInstructionsFound();
        }

        @Override
        protected void doInstruction(Library library, ValidateInstructionLineEvent event) {
            ValidateInstructionTaskHelper helper = new ValidateInstructionTaskHelper(convertor, library, event);
            helper.validate();
            library.doValidate(event);
        }
    }

    private static class ValidateInstructionTaskHelper
            extends InstructionConvertor {

        private final ValidateInstructionLineEvent event;

        private ValidateInstructionTaskHelper(TypeConvertor convertor, Library library, ValidateInstructionLineEvent event) {
            super(library.getInstruction(event.getKeyword()), convertor);
            this.event = event;
        }

        @Override
        public String toString() {
            return event.toString();
        }

        private void validate() {
            if (event.countArguments() < instruction.minimumRequiredArguments())
                event.setTooFewArguments();
            else
                tryValidate();
        }

        private void tryValidate() {
            try {
                convertArguments(event.getArguments(instruction));
            } catch (RuntimeException ex) {
                event.setArgumentsAreIncompatible();
            }
        }
    }

    private static class ExecuteInstructionTask
            extends InstructionTask<ExecuteInstructionLineEvent> {

        protected final TypeConvertor convertor;

        ExecuteInstructionTask(TypeConvertor convertor) {
            this.convertor = convertor;
        }

        @Override
        protected void doInstructionsNotFound(ExecuteInstructionLineEvent event) {
            throw new InstructionNotFoundException(event);
        }

        @Override
        protected void doMultipleInstructionsFound(ExecuteInstructionLineEvent event) {
            throw new MultipleInstructionsFoundException(event);
        }

        @Override
        protected void doInstruction(Library library, ExecuteInstructionLineEvent event) {
            try {
                ExecuteInstructionTaskHelper helper = new ExecuteInstructionTaskHelper(convertor, library, event);
                helper.checkBeforeExecution(library.getCaller());
                library.doBeforeExecution(event);
                helper.execute(library.getCaller());
                library.doAfterExecution(event);
            } catch (RuntimeException ex) {
                library.doWhenExceptionThrown(event, ex);
            }
        }
    }

    private static class ExecuteInstructionTaskHelper
            extends InstructionConvertor {

        private final ExecuteInstructionLineEvent event;

        private ExecuteInstructionTaskHelper(TypeConvertor convertor, Library library, ExecuteInstructionLineEvent event) {
            super(library.getInstruction(event.getKeyword()), convertor);
            this.event = event;
        }

        @Override
        public String toString() {
            return event.toString();
        }

        private void checkBeforeExecution(Object callee) {
            if (!instruction.isWithinCallee(callee))
                throw new MethodNotInCallerException(event.getNamespace(), event.getKeyword());
            if (event.countArguments() < instruction.minimumRequiredArguments())
                throw new ToFewArgumentsException(event);
            if (event.haveIncompatibleResultType(instruction.getReturnType()))
                throw new ResultTypeIsIncompatibleException(event);
            if (!isArgumentsConvertable())
                throw new ArgumentsAreIncompatibleException(event, fetchArgumentsConversionException());
            if (instruction.isWithInvokerAnnotation() && !instruction.isWithInvokerParameter())
                throw new InvokerIsMissingException();
        }

        private boolean isArgumentsConvertable() {
            try {
                convertArguments(event.getArguments(instruction));
                return true;
            } catch (RuntimeException ex) {
                return false;
            }
        }

        private RuntimeException fetchArgumentsConversionException() {
            try {
                convertArguments(event.getArguments(instruction));
                return null;
            } catch (RuntimeException ex) {
                return ex;
            }
        }
        
        private void execute(Object callee) {
            try {
                Object[] args = convertArguments(event.getArguments(instruction));
                Object result = instruction.invoke(callee, args);
                event.setResult(result);
            } catch (InvocationTargetException ex) {
                if (ex.getCause() instanceof RuntimeException)
                    throw (RuntimeException) ex.getCause();
                throw new InstructionTargetException(event.getNamespace(), event.getKeyword(), ex.getCause());
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                throw new UnknownIssueException(ex);
            }
        }
    }

    @Override
    protected void uncatchedEventHandler(Event event) {
        for (Library library : libraries) {
            library.eventHandler(event);
            if (event.isConsumed())
                return;
        }
    }

    @Override
    public int hashCode() {
        return 7 * 83
                + Objects.hashCode(this.libraries);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.libraries, ((LibraryRegistry) obj).libraries);
    }
}
