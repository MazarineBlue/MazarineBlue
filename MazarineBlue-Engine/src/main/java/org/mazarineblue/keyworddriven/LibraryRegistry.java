/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.MultipleInstructionsFoundException;

/**
 * An {@code LibraryRegistry} is a holder of {@link Library Libraries}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library
 */
public class LibraryRegistry
        extends ReflectionSubscriber<Event> {

    private final List<Library> libraries;

    /**
     * Constructs the object contains any number of libraries registered.
     *
     * @param libs the libraries to register.
     */
    public LibraryRegistry(Library... libs) {
        libraries = new ArrayList<>(Arrays.asList(libs));
    }

    @Override
    public String toString() {
        return "LibraryRegistry{" + "size=" + libraries.size() + '}';
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CountLibrariesEvent
     */
    @EventHandler
    public void countLibrary(CountLibrariesEvent event) {
        event.addToCount(libraries);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see AddLibraryEvent
     */
    @EventHandler
    public void addLibrary(AddLibraryEvent event) {
        libraries.add(event.getLibrary());
        event.setConsumed(true);
    }

    /**
     * Adds a {@code Library} to this registry.
     *
     * @param library the library to add.
     */
    public void addLibrary(Library library) {
        libraries.add(library);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see RemoveLibraryEvent
     */
    @EventHandler
    public void removeLibrary(RemoveLibraryEvent event) {
        Library lib = event.getLibrary();
        if (libraryIsNotFound(lib))
            throw new LibraryNotFoundException(lib);
        libraries.remove(lib);
        event.setConsumed(true);
    }

    private boolean libraryIsNotFound(Library library) {
        return !libraries.contains(library);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ValidateInstructionLineEvent
     */
    @EventHandler
    public void validateInstruction(ValidateInstructionLineEvent event) {
        new ValidateInstructionTask().doInstruction(event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ExecuteInstructionLineEvent
     */
    @EventHandler
    public void executeInstruction(ExecuteInstructionLineEvent event) {
        new ExecuteInstructionTask().doInstruction(event);
    }

    private abstract class InstructionTask<E extends InstructionLineEvent> {

        void doInstruction(E event) {
            FetchLibrariesEvent e = new FetchLibrariesEvent(lib -> filter(lib, event, Library.class));
            eventHandler(e);
            doInstruction(e.getLibraries(), event);
            event.setConsumed(true);
        }

        private boolean filter(Library lib, InstructionLineEvent e, Class<Library> type) {
            return lib.containsInstruction(e.getNamespace(), e.getKeyword()) && Library.class.isAssignableFrom(type);
        }

        private void doInstruction(List<Library> libraries, E event) {
            if (libraries.isEmpty())
                doInstructionsNotFound(event);
            else if (libraries.size() > 1)
                doMultipleInstructionsFound(event);
            else
                doInstruction(libraries.get(0), event);
        }

        protected abstract void doInstructionsNotFound(E event);

        protected abstract void doMultipleInstructionsFound(E event);

        protected abstract void doInstruction(Library library, E event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
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

    private class ValidateInstructionTask
            extends InstructionTask<ValidateInstructionLineEvent> {

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
            library.validateInstruction(event);
        }

    }

    private class ExecuteInstructionTask
            extends InstructionTask<ExecuteInstructionLineEvent> {

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
            library.executeInstruction(event);
        }
    }

    @Override
    public int hashCode() {
        return 7 * 83
                + Objects.hashCode(this.libraries);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.libraries, ((LibraryRegistry) obj).libraries);
    }
}
