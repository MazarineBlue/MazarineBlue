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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.PrimativesNotAllowedByondMinimumBorderException;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.utililities.Primatives;
import org.mazarineblue.utililities.exceptions.UnknownIssueException;

/**
 * An {@code Instruction} is an java method with the annotation {@link Keyword}
 * within a {@link Library}. Optional annotations include {@link Beta},
 * {@link Parameters} and {@link PassInvoker}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Beta
 * @see Keyword
 * @see Library
 * @see Parameters
 * @see PassInvoker
 */
class Instruction {

    private static final Primatives PRIMATIVES = Primatives.getDefaultInstance();

    private Method method;
    private final boolean beta; // Currently unused but intented for reports and such.
    private final boolean deprecated; // Currently unused but intented for reports and such.
    private final int minimumRequiredArguments;

    Instruction(Method method) {
        this(method, minimumRequiredArguments(method));
    }

    Instruction(Method method, int minimumRequiredArguments) {
        this.method = method;
        beta = instructionIsBeta(method);
        deprecated = instructionIsDeprecated(method);
        this.minimumRequiredArguments = minimumRequiredArguments;
    }

    @Override
    public String toString() {
        return "method=" + method.getDeclaringClass().getSimpleName() + '.' + method.getName()
                + ", minARgs=" + minimumRequiredArguments + ", beta=" + beta + ", deprecated=" + deprecated;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Instruction()">
    private static int minimumRequiredArguments(Method method) {
        Parameters annotation = method.getAnnotation(Parameters.class);
        return annotation == null ? 0 : annotation.min();
    }

    private static boolean instructionIsBeta(Method method) {
        return method.getAnnotationsByType(Beta.class).length > 0;
    }

    private static boolean instructionIsDeprecated(Method method) {
        return method.getAnnotationsByType(Deprecated.class).length > 0;
    }
    // </editor-fold>

    void checkInstructionOnInitializion(String namespace, String keyword) {
        if (isNonPublic(method.getDeclaringClass()) && !method.getDeclaringClass().isAnonymousClass())
            throw new LibraryInaccessibleException(namespace);
        if (isNonPublic(method))
            throw new InstructionInaccessibleException(namespace, keyword);
        if (containsPrimativesBeyondMinimumBorder(method, minimumRequiredArguments))
            throw new PrimativesNotAllowedByondMinimumBorderException(namespace, keyword);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for checkInstruction()">
    private static boolean isNonPublic(Method method) {
        return !Modifier.isPublic(method.getModifiers());
    }

    private static boolean isNonPublic(Class<?> aClass) {
        return !Modifier.isPublic(aClass.getModifiers());
    }

    private static boolean containsPrimativesBeyondMinimumBorder(Method method, int minimumRequiredArguments) {
        Class<?>[] arr = getParameterTypes(method);
        for (int i = minimumRequiredArguments; i < arr.length; ++i)
            if (PRIMATIVES.isPrimative(arr[i]))
                return true;
        return false;
    }
    // </editor-fold>

    void validate(ValidateInstructionLineEvent event) {
        if (event.areLessArgumentsThen(minimumRequiredArguments))
            event.setTooFewArguments();
        else if (event.haveIncompatibleArguments(getParameterTypes(method))) {
            event.setArgumentsAreIncompatible();
            event.haveIncompatibleArguments(getParameterTypes(method));
        }
    }

    void checkBeforeExecution(Object callee, ExecuteInstructionLineEvent event) {
        if (!arrayContains(callee.getClass().getMethods(), method))
            throw new InstructionInaccessibleException(event.getNamespace(), event.getKeyword());
        if (event.areLessArgumentsThen(minimumRequiredArguments))
            throw new ToFewArgumentsException(event);
        if (event.haveIncompatibleArguments(getParameterTypes(method)))
            throw new ArgumentsAreIncompatibleException(event);
    }

    private boolean arrayContains(Object[] arr, Object obj) {
        for (Object item : arr)
            if (item.equals(obj))
                return true;
        return false;
    }

    private static Class<?>[] getParameterTypes(Method method) {
        Class<?>[] types = method.getParameterTypes();
        if (invokerObjectRequsted(method))
            types = Arrays.copyOfRange(types, 1, types.length);
        return types;
    }

    private static boolean invokerObjectRequsted(Method method) {
        return method.getAnnotation(PassInvoker.class) != null;
    }

    void execute(Object callee, ExecuteInstructionLineEvent event) {
        try {
            Object[] arguments = Arrays.copyOf(getParameters(event), theMaximumArguments());
            Object result = method.invoke(callee, arguments);
            event.setResult(result);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new UnknownIssueException(ex);
        }
    }

    private Object[] getParameters(ExecuteInstructionLineEvent event) {
        return invokerObjectRequsted(method)
                ? insertInFront(event.getArguments(), event.getInvoker())
                : event.getArguments();
    }

    private Object[] insertInFront(Object[] right, Object... left) {
        Object[] dst = increaseArray(right, left.length, left.length);
        System.arraycopy(left, 0, dst, 0, left.length);
        return dst;
    }

    private Object[] increaseArray(Object[] src, int increment, int offset) {
        Object[] dst = new Object[src.length + increment];
        System.arraycopy(src, 0, dst, offset, src.length);
        return dst;
    }

    private int theMaximumArguments() {
        return method.getParameterCount();
    }

    @Override
    public int hashCode() {
        return 7 * 11 * 11
                + 11 * Objects.hashCode(method)
                + Arrays.deepHashCode(method.getParameterTypes());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.method.getName(), ((Instruction) obj).method.getName())
                && Arrays.equals(this.method.getParameterTypes(), ((Instruction) obj).method.getParameterTypes());
    }
}
