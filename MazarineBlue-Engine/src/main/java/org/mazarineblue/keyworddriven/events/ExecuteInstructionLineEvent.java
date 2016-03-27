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

import java.util.Objects;
import org.mazarineblue.keyworddriven.Library;

/**
 * An {@code ExecuteInstructionLineEvent} is used to call an instruction within
 * a {@link Library}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library
 */
public class ExecuteInstructionLineEvent
        extends InstructionLineEvent {

    private Object result;

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
        String str = result == null ? "" : result.toString();
        return "line={" + super.toString() + "}, result={" + str + '}';
    }

    @Override
    public String responce() {
        return hasException() ? "exception=" + getException().getMessage()
                : result != null ? "result={" + result.toString() + '}'
                : "";
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        return 3 * 37
                + Objects.hashCode(result);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && super.equals(obj)
                && Objects.equals(this.result, ((ExecuteInstructionLineEvent) obj).result);
    }
}
