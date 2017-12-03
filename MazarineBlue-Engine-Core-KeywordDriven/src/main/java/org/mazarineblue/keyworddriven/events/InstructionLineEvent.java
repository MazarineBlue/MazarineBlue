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

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.util.GracefullConvertor;
import org.mazarineblue.utililities.ArgumentList;
import org.mazarineblue.utililities.TypeConvertor;

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
    private static final TypeConvertor TYPE_CONVERTOR = new TypeConvertor();

    private final InstructionLine line;

    public static Predicate<Event> anyKeywordMatch(String... keywords) {
        return e -> {
            if (!InstructionLineEvent.class.isAssignableFrom(e.getClass()))
                return false;
            String k = InstructionLineEvent.class.cast(e).getKeyword();
            return stream(keywords)
                    .map(GracefullConvertor::degraceMethod)
                    .anyMatch(k::equals);
        };
    }

    /**
     * Constructs an {@code InstructionLineEvent} with an specified path and
     * arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     */
    protected InstructionLineEvent(String path, Object... arguments) {
        this.line = new InstructionLine(new Path(path), copyOf(arguments, arguments.length));
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

    public final Object[] getArguments() {
        return line.getArguments();
    }

    /**
     * Test the arguments count.
     *
     * @param count the count to stay under.
     * @return {@code true} if there where less then specified.
     */
    public boolean areLessArgumentsThen(int count) {
        return line.getArguments().length < count;
    }

    /**
     * Test if arguments are incompatible with the specified types.
     *
     * @param types   the types to use for checking the parameters.
     * @param varArgs if {@code true}, collect the remaining arguments and turn
     *                then into an Object array.
     * @return {@code true} if there arguments are incompatible with the
     *         specified types.
     */
    public boolean haveIncompatibleArguments(Class<?>[] types, boolean varArgs) {
        return checkFirstArguments(types, line.getArguments(), varArgs) ? true
                : checkVariableArguments(types, line.getArguments(), varArgs);
    }

    private boolean checkFirstArguments(Class<?>[] types, Object[] actual, boolean varArgs) {
        int n = numberOfArgumentsOutOfVarArgs(types, varArgs);
        for (int i = 0; i < n; ++i)
            if (!TYPE_CONVERTOR.isConvertable(types[i], actual[i]))
                return true;
        return false;
    }

    private boolean checkVariableArguments(Class<?>[] types, Object[] actual, boolean varArgs) {
        if (!varArgs)
            return false;
        Class<?> arr = types[types.length - 1];
        Class<?> expected = arr.getComponentType();
        int n = line.getArguments().length;
        for (int i = numberOfArgumentsOutOfVarArgs(types, varArgs); i < n; ++i)
            if (!TYPE_CONVERTOR.isConvertable(expected, actual[i]))
                return true;
        return false;
    }

    private int numberOfArgumentsOutOfVarArgs(Class<?>[] types, boolean varArgs) {
        int n = line.getArguments().length;
        return n < types.length ? n
                : varArgs ? types.length - 1
                : types.length;
    }

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
