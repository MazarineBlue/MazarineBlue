/*
 * Copyright (c) 2015 Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
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
package org.mazarineblue.keyworddriven.links;

import org.mazarineblue.events.instructions.ExecuteInstructionLineEvent;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.librarymanager.Instruction;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
abstract class InstructionInvoker {

    public void invoke(InstructionLineEvent event) {
        if (skipInvokation(event))
            return;
        Instruction instruction = getInstruction(event);
        doInvoke(instruction, event);
    }

    protected boolean skipInvokation(InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        return line.isEmpty();
    }

    abstract protected Instruction getInstruction(InstructionLineEvent event);

    private void doInvoke(Instruction instruction, InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        Object[] parameters = line.getParameters();
        instruction.validate(parameters);
        if (event instanceof ExecuteInstructionLineEvent)
            instruction.invoke(parameters);
        event.setConsumed();
    }
}
