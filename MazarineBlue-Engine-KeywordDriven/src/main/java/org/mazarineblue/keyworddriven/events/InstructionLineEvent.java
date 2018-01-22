/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.keyworddriven.events;

import static java.lang.System.arraycopy;
import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Instruction;
import org.mazarineblue.keyworddriven.util.GracefullConvertor;
import org.mazarineblue.utilities.ArgumentList;

/**
 * An {@code InstructionLineEvent} is an used to perform some kind of action
 * with an {@link InstructionLine}, such as executing of validating.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see InstructionLine
 * @see ExecuteInstructionLineEvent
 * @see ValidateInstructionLineEvent
 */
public abstract class InstructionLineEvent
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    private final InstructionLine line;

    /**
     * Indicates if the event contains any of the specified keywords.
     *
     * @param keywords the keywords to test for.
     * @return {@code true} if the event contains one of the keywords.
     */
    public static Predicate<Event> matchesAnyKeywords(String... keywords) {
        return e -> matchesAnyKeywordsHelper(e, keywords);
    }

    /**
     * Indicates if the event contains none of the specified keywords.
     *
     * @param keywords the keywords to test for.
     * @return {@code true} if the event contains none of the keywords.
     */
    public static Predicate<Event> matchesNoneKeywords(String... keywords) {
        return e -> !matchesAnyKeywordsHelper(e, keywords);
    }

    private static boolean matchesAnyKeywordsHelper(Event e, String... keywords) {
        if (!InstructionLineEvent.class.isAssignableFrom(e.getClass()))
            return false;
        String k = ((InstructionLineEvent) e).getKeyword();
        return stream(keywords)
                .map(GracefullConvertor::degraceMethod)
                .anyMatch(k::equals);
    }

    /**
     * Constructs an {@code InstructionLineEvent} with an specified path and
     * arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     */
    protected InstructionLineEvent(String path, Object... arguments) {
        Object[] arr = arguments == null ? new Object[0] : copyOf(arguments, arguments.length);
        this.line = new InstructionLine(new Path(path), arr);
    }

    @Override
    public String toString() {
        return line.toString();
    }

    @Override
    public String message() {
        return "line={" + line + '}';
    }

    public final String getPath() {
        return line.getPath();
    }

    public final String getNamespace() {
        return line.getNamespace();
    }

    public final String getKeyword() {
        return line.getKeyword();
    }

    protected ArgumentList getArgumentList() {
        return line.getArgumentList();
    }

    public Object getArgument(int start) {
        return line.getArgument(start);
    }

    public final Object[] getArguments() {
        return line.getArguments();
    }

    public Object[] getArguments(int start) {
        return line.getArguments(start);
    }

    public Object[] getArguments(int start, int stop) {
        return line.getArguments(start, stop);
    }

    public void setArgument(int index, Object argument) {
        line.setArgument(index, argument);
    }

    public void setArguments(Object... arguments) {
        line.setArguments(arguments);
    }

    public int countArguments() {
        return line.getArguments().length;
    }

    public int indexOf(Predicate<Object> condition) {
        return line.indexOf(condition);
    }

    public int lastIndexOf(Predicate<Object> condition) {
        return line.indexOf(condition);
    }

    public Object[] getArguments(Instruction instruction) {
        Object[] src = getArgumentWithInvokerIfRequired(instruction);
        Object[] dst = new Object[instruction.getParameterCount()];
        convertAndCopyArguments(src, dst, instruction);
        return dst;
    }

    //<editor-fold defaultstate="collapsed" desc="Helper for getArguments()">
    private Object[] getArgumentWithInvokerIfRequired(Instruction instruction) {
        return instruction.isWithInvokerAnnotation()
                ? insertInvoker(line.getArguments(), invoker())
                : line.getArguments();
    }

    private Object[] insertInvoker(Object[] right, Object... left) {
        Object[] dst = increaseArray(right, left.length, left.length);
        arraycopy(left, 0, dst, 0, left.length);
        return dst;
    }

    private Object[] increaseArray(Object[] src, int increment, int offset) {
        Object[] dst = new Object[src.length + increment];
        arraycopy(src, 0, dst, offset, src.length);
        return dst;
    }

    private void convertAndCopyArguments(Object[] src, Object[] dst, Instruction instruction) {
        arraycopy(src, 0, dst, 0, getNonVarArgsLength(instruction, src));
        if (instruction.isVarArgs())
            dst[dst.length - 1] = getVarArg(instruction, src);
    }

    private int getNonVarArgsLength(Instruction instruction, Object[] src) {
        int n = instruction.getParameterCount();
        return src.length < n ? src.length : n;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    private Object getVarArg(Instruction instruction, Object[] parameters) {
        int from = instruction.getParameterCount() - 1;
        int length = parameters.length - from;
        if (length < 0)
            return newInstance(instruction.getVarArgsBaseType(), 0);
        Object dst = newInstance(instruction.getVarArgsBaseType(), length);
        arraycopy(parameters, from, dst, 0, length);
        return dst;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        return 5 * 17
                + Objects.hashCode(line);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.line, ((InstructionLineEvent) obj).line);
    }
}
