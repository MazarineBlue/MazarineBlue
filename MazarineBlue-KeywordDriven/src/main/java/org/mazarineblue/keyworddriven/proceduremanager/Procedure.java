/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.keyworddriven.proceduremanager;

import java.util.Collection;
import java.util.Collections;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Procedure {

    static private final String[] empty = new String[0];
    private final String[] parameters;
    private final Collection<InstructionLine> instructions;

    public Procedure(String[] parameters, Collection<InstructionLine> instructions) {
        this.parameters = parameters == null ? empty : parameters;
        this.instructions = Collections.unmodifiableCollection(instructions);
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    Collection<InstructionLine> getInstructions() {
        return instructions;
    }

    public void copyParameters(DataSource source, InstructionLine line) {
        Object[] param = line.getParameters();
        copyParameters(source, param);
    }

    public final void copyParameters(DataSource source, Object[] values) {
        for (int i = 0; i < parameters.length; ++i) {
            Object value = values.length <= i ? "" : values[i];
            source.setData(parameters[i], value);
        }
    }
}
