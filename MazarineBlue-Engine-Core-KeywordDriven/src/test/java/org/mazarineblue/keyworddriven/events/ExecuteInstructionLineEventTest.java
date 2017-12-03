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
package org.mazarineblue.keyworddriven.events;

import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExecuteInstructionLineEventTest
        extends TestHashCodeAndEquals<ExecuteInstructionLineEvent> {

    private class TestExecuteInstructionLineEvent
            extends ExecuteInstructionLineEvent {

        private TestExecuteInstructionLineEvent(String path) {
            super(path);
        }

        @Override
        public void setException(RuntimeException exception) {
            super.setException(exception);
        }
    }

    @Override
    protected ExecuteInstructionLineEvent getObject() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("foo", "foo");
        e.setResult(2);
        return e;
    }

    @Override
    protected ExecuteInstructionLineEvent getDifferentObject() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("foo", "oof");
        e.setResult(1);
        return e;
    }
}
