/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.keyworddriven.util;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

public class LibrarySpy
        extends Library {

    private int setup, teardown;
    private final List<ExecuteInstructionLineEvent> beforeExecutions = new ArrayList<>();
    private final List<ExecuteInstructionLineEvent> afterExecutions = new ArrayList<>();
    private final List<ValidateInstructionLineEvent> validations = new ArrayList<>();

    public LibrarySpy(String namespace) {
        super(namespace);
    }

    @Override
    public void doSetup(Invoker invoker) {
        ++setup;
    }

    public int countSetup() {
        return setup;
    }

    @Override
    public void doTeardown(Invoker invoker) {
        ++teardown;
    }

    public int countTeardown() {
        return teardown;
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        beforeExecutions.add(event);
    }

    public List<ExecuteInstructionLineEvent> getBeforeExecutions() {
        return beforeExecutions;
    }

    public int countBeforeExecutions() {
        return beforeExecutions.size();
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        afterExecutions.add(event);
    }

    public int countAfterExecutions() {
        return afterExecutions.size();
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
        validations.add(event);
    }

    public int countValidations() {
        return validations.size();
    }
}
