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
import static java.lang.reflect.Modifier.isPublic;
import java.util.Arrays;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.deepHashCode;
import static java.util.Arrays.stream;
import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.PrimativesNotAllowedByondMinimumBorderException;
import static org.mazarineblue.utilities.Primatives.isPrimative;

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
public class Instruction {

    private Method method;
    private final boolean beta; // Currently unused but intented for reports and such.
    private final boolean deprecated; // Currently unused but intented for reports and such.
    private final int minimumRequiredArguments;

    Instruction(Method method) {
        this(method, extractMinimumRequiredArguments(method));
    }

    Instruction(Method method, int minimumRequiredArguments) {
        this.method = method;
        beta = extractIsBeta(method);
        deprecated = extractIsDeprecated(method);
        this.minimumRequiredArguments = minimumRequiredArguments;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Instruction()">
    private static int extractMinimumRequiredArguments(Method method) {
        Parameters annotation = method.getAnnotation(Parameters.class);
        return annotation == null ? 0 : annotation.min();
    }

    private static boolean extractIsBeta(Method method) {
        return method.getAnnotationsByType(Beta.class).length > 0;
    }

    private static boolean extractIsDeprecated(Method method) {
        return method.getAnnotationsByType(Deprecated.class).length > 0;
    }
    // </editor-fold>

    @Override
    public String toString() {
        return "method=" + method.getDeclaringClass().getSimpleName() + '.' + method.getName()
                + ", minParameters=" + minimumRequiredArguments + ", beta=" + beta + ", deprecated=" + deprecated;
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    public Class<?> getVarArgsBaseType() {
        Class<?>[] types = method.getParameterTypes();
        Class<?> type = types[types.length - 1];
        return type.getComponentType();
    }

    Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public int getParameterCount() {
        return method.getParameterCount();
    }

    int minimumRequiredArguments() {
        return minimumRequiredArguments;
    }

    public boolean isWithInvokerAnnotation() {
        return method.getAnnotation(PassInvoker.class) != null;
    }

    public boolean isWithInvokerParameter() {
        if (method.getParameterCount() == 0)
            return false;
        Class<?>[] types = method.getParameterTypes();
        return Invoker.class.isAssignableFrom(types[0]);
    }

    boolean isWithinCallee(Object callee) {
        return stream(callee.getClass().getMethods())
                .anyMatch(m -> m.equals(method));
    }

    Object invoke(Object callee, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(callee, args);
    }

    void checkInstructionOnInitialisation(String namespace, String keyword) {
        if (isNonPublic(method.getDeclaringClass()) && !method.getDeclaringClass().isAnonymousClass())
            throw new LibraryInaccessibleException(namespace);
        if (isNonPublic(method))
            throw new InstructionInaccessibleException(namespace, keyword);
        if (containsPrimativesBeyondMinimumBorder(minimumRequiredArguments))
            throw new PrimativesNotAllowedByondMinimumBorderException(namespace, keyword);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for checkInstruction()">
    private static boolean isNonPublic(Method method) {
        return !isPublic(method.getModifiers());
    }

    private static boolean isNonPublic(Class<?> type) {
        return !isPublic(type.getModifiers());
    }

    private boolean containsPrimativesBeyondMinimumBorder(int minimumRequiredArguments) {
        Class<?>[] arr = getParameterTypesWithInvoker();
        for (int i = minimumRequiredArguments; i < arr.length; ++i)
            if (isPrimative(arr[i]))
                return true;
        return false;
    }

    private Class<?>[] getParameterTypesWithInvoker() {
        Class<?>[] types = method.getParameterTypes();
        if (isWithInvokerAnnotation())
            types = copyOfRange(types, 1, types.length);
        return types;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        return 7 * 11 * 11
                + 11 * Objects.hashCode(method)
                + deepHashCode(method.getParameterTypes());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.method.getName(), ((Instruction) obj).method.getName())
                && Arrays.equals(this.method.getParameterTypes(), ((Instruction) obj).method.getParameterTypes());
    }

    Class<?> getReturnType() {
        return method.getReturnType();
    }
}
