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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.DeclaringMethodClassNotEqualToCalleeException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceMethod;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceNamespace;

/**
 * A {@code library} is group of {@link Instruction Instructions} and listens
 * to {@code Events}.
 * <p>
 * Instructions are defined within the library class. An instruction can be
 * declared using one or more {@code @Keyword} annotations. The minimal
 * required amount of arguments can be specified using the {@code @Parameters}
 * annotation. The maximum amount of parameters is extracted from the method
 * signature. When the annotation {@code @PassInvoker} is used then the first
 * parameter must be an {@code Invoker} parameter.
 * <p>
 * An library can also be added to the event bus, though the use of the
 * {@code AddLibraryEvent}. Any method with an {@code @EventHandler} annotation
 * will receive events. The only parameter allowed must be an {@code Event} of
 * child.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Instruction
 */
public abstract class Library
        extends ReflectionSubscriber<Event> {

    public static final String NO_NAMESPACE = "";

    private final String namespace;
    private final Map<String, Instruction> keywords = new HashMap<>(16);

    public static Predicate<Library> matchesAny(Class<?>... types) {
        return lib -> stream(types)
                .anyMatch(t -> t.isAssignableFrom(lib.getClass()));
    }

    public static Predicate<Library> matchesNone(Class<?>... types) {
        return lib -> stream(types)
                .noneMatch(t -> t.isAssignableFrom(lib.getClass()));
    }

    public static Predicate<Library> matchesAnyNamespace(String... namespaces) {
        return lib -> stream(namespaces).anyMatch(lib.namespace()::equals);
    }

    public static Predicate<Library> matchesNoneNamespace(String... namespaces) {
        return lib -> stream(namespaces).noneMatch(lib.namespace()::equals);
    }

    /**
     * Creates a object that contains each instruction within.
     *
     * @param namespace the namespace to resolve conflicts between identical keywords.
     */
    public Library(String namespace) {
        this.namespace = degraceNamespace(namespace);
        registerInstructions();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Library()">
    private void registerInstructions() {
        getAllMethods().stream().forEach(this::registerMethod);
    }

    private List<Method> getAllMethods() {
        List<Method> list = new ArrayList<>(16);
        for (Method method : getClass().getDeclaredMethods())
            if (libraryIsTheDeclaringClass(method))
                list.add(method);
        return list;
    }

    private boolean libraryIsTheDeclaringClass(Method method) {
        return Library.class.isAssignableFrom(method.getDeclaringClass());
    }

    private void registerMethod(Method method) {
        for (Keyword keyword : getAllKeywords(method))
            registerInstruction(keyword.value(), method);
    }

    private static Keyword[] getAllKeywords(Method method) {
        return method.getAnnotationsByType(Keyword.class);
    }

    private void registerInstruction(String keyword, Method method) {
        Instruction instruction = new Instruction(method);
        instruction.checkInstructionOnInitialisation(namespace, keyword);
        registerInstruction(keyword, instruction);
    }
    // </editor-fold>

    /**
     * This method allows to perform some kind of action <i>after</i> the
     * creation of a library.
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doSetup(Invoker invoker) {
        // Do nothing by default.
    }

    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doTeardown(Invoker invoker) {
        // Do nothing by default.
    }

    /**
     * Registers instruction that is not a member of this class. Executing an
     * instruction requires that is a member of the callee. Therefor mixed
     * libraries, where some instruction method live in side the library and
     * others live outside, will not work.
     *
     * @param keyword                  the keyword to register the method under.
     * @param method                   the method to register under the specified keyword.
     * @param minimumRequiredArguments the minimum required arguments of this method.
     * @see #getCaller()
     */
    public final void registerInstruction(String keyword, Method method, int minimumRequiredArguments) {
        if (!method.getDeclaringClass().equals(getCaller().getClass()))
            throw new DeclaringMethodClassNotEqualToCalleeException(method, getCaller());
        Instruction instruction = new Instruction(method, minimumRequiredArguments);
        instruction.checkInstructionOnInitialisation(namespace, keyword);
        registerInstruction(keyword, instruction);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Library() and registerInstruction()">
    private void registerInstruction(String keyword, Instruction instruction) {
        String key = degraceMethod(keyword);
        if (keywords.containsKey(key))
            throw new KeywordConflictException(key);
        keywords.put(key, instruction);
    }
    // </editor-fold>

    @Override
    public String toString() {
        String keys = Arrays.toString(orderedKeywords().toArray());
        return namespace.isEmpty() ? keys : namespace + ' ' + keys;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for toString()">
    private Set<String> orderedKeywords() {
        return new TreeSet<>(keywords.keySet());
    }
    // </editor-fold>

    public String namespace() {
        return namespace;
    }

    /**
     * Returns the caller that is used to receive a method call.
     *
     * @return this library by default.
     */
    protected Object getCaller() {
        return this;
    }

    boolean containsInstruction(String namespace, String keyword) {
        return namespaceMatches(namespace) && libraryContainsInstruction(keyword);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for fetchLibraries()">
    private boolean namespaceMatches(String namespace) {
        return namespace.isEmpty() || this.namespace.equals(namespace);
    }

    private boolean libraryContainsInstruction(String keyword) {
        return keywords.containsKey(keyword);
    }
    // </editor-fold>

    boolean containsInstruction(String keyword) {
        return keywords.containsKey(keyword);
    }

    Instruction getInstruction( String keyword) {
        return keywords.get(keyword);
    }

    /**
     * This method allows for library specific validation of an instruction
     * line.
     *
     * @param event the event with the line to be validated by this library.
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doValidate(ValidateInstructionLineEvent event) {
        // Do nothing by default.
    }

    /**
     * This method allows to perform some kind of action <i>before</i> the
     * execution of an instruction line. An good example is the throwing of
     * runtime exception, if the instruction does not comply to
     * {@code doValidate(ValidateInstructionLineEvent)}.
     *
     * @param event the event that will cause the call on the instruction.
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        // Do nothing by default.
    }

    /**
     * This method allows to perform some kind of action <i>after</i> the
     * execution of instruction line. An good example is reporting.
     *
     * @param event the event that will cause the call on the instruction
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        // Do nothing by default.
    }

    /**
     * This method allows to perform some kind of action <i>when</i> the
     * execution of instruction line resulted in an exception. By default this
     * throws the exception.
     *
     * @param event the event that will cause the call on the instruction
     * @param ex the exception thrown
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void doWhenExceptionThrown(ExecuteInstructionLineEvent event, RuntimeException ex) {
       throw ex;
    }

    @Override
    public int hashCode() {
        return 7 * 47 * 47
                + 47 * Objects.hashCode(namespace)
                + Objects.hashCode(keywords);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.namespace, ((Library) obj).namespace)
                && Objects.equals(this.keywords, ((Library) obj).keywords);
    }
}
