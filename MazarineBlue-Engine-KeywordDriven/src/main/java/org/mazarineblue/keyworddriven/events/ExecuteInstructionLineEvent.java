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

import static java.lang.String.format;
import java.util.Objects;
import org.mazarineblue.keyworddriven.Library;
import static org.mazarineblue.utilities.Primatives.convertPrimative;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * An {@code ExecuteInstructionLineEvent} is used to call an instruction within
 * a {@link Library}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library
 */
public class ExecuteInstructionLineEvent
        extends InstructionLineEvent
        implements InstructionLineResultContainerEvent {

    private static final long serialVersionUID = 1L;

    private transient Object result;
    private Class<?> expectedResultType;

    /**
     * Constructs an {@code ExecuteInstructionLineEvent} with an specified path
     * and arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the call to the instruction.
     */
    public ExecuteInstructionLineEvent(String path, Object... arguments) {
        super(path, arguments);
    }

    @Override
    public String toString() {
        return format("line={%s}, result={%s}, error={%s}", super.toString(),
                      result == null ? "" : result.toString(), hasException() ? getException().getMessage() : "");
    }

    @Override
    public String responce() {
        return hasException() ? "error={" + getException().getMessage() + '}'
                : result != null ? "result={" + result.toString() + '}'
                : "";
    }

    /**
     * Instructs MazarineBlue to throw an exception, if the instruction does
     * not return the specified type.
     *
     * @param type the instruction must return.
     */
    public void setExpectResultType(Class<?> type) {
        expectedResultType = convertPrimative(type);
    }

    public boolean haveIncompatibleResultType(Class<?> actualReturnType) {
        return expectedResultType == null ? false : !expectedResultType.equals(convertPrimative(actualReturnType));
    }

    public ExecuteInstructionLineEvent setResult(Object result) {
        this.result = result;
        return this;
    }

    /**
     * Returns the result of the instruction.
     *
     * @return {@code null} if the instruction has not executed.
     */
    public Object getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        return 3 * 37 * 37
                + 37 * super.hashCode()
                + Objects.hashCode(result);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj) && Objects.equals(this.result, ((ExecuteInstructionLineEvent) obj).result);
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        result = ((ExecuteInstructionLineEvent) other).result;
    }
}
