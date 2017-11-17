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
package org.mazarineblue.procedures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.eventdriven.Interpreter;

class ProcedureRegistry
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Procedure> procedures = new HashMap<>(4);
    private final Interpreter interpreter;

    ProcedureRegistry(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    boolean contains(String name) {
        return procedures.containsKey(name);
    }

    void put(String name, Procedure procedure) {
        procedures.put(name, procedure);
    }

    void call(String name) {
        Procedure procedure = procedures.get(name);
        interpreter.execute(procedure);
    }

    @Override
    public int hashCode() {
        return 5 * 97 * 97
                + 97 * Objects.hashCode(this.procedures)
                + Objects.hashCode(this.interpreter);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.procedures, ((ProcedureRegistry) obj).procedures)
                && Objects.equals(this.interpreter, ((ProcedureRegistry) obj).interpreter);
    }
}
