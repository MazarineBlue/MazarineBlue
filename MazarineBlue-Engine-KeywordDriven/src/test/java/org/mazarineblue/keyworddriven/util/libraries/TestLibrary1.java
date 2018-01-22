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
package org.mazarineblue.keyworddriven.util.libraries;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.Converted;
import org.mazarineblue.keyworddriven.util.Output;
import org.mazarineblue.keyworddriven.util.exceptions.TestException;
import org.mazarineblue.keyworddriven.util.exceptions.TestRuntimeException;

public class TestLibrary1
        extends AbstractTestLibrary {

    private String methodName;
    private Object[] arguments;
    private boolean doSetup, doTeardown, doValidate, doBeforeExecution, doAfterExecution, doWhenExceptionThrown;

    public String getCalledMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public boolean isDoSetup() {
        return doSetup;
    }

    public boolean isDoTeardown() {
        return doTeardown;
    }

    public boolean isDoValidate() {
        return doValidate;
    }

    public boolean isDoBeforeExecution() {
        return doBeforeExecution;
    }

    public boolean isDoAfterExecution() {
        return doAfterExecution;
    }

    public boolean isDoWhenExceptionThrown() {
        return doWhenExceptionThrown;
    }

    @Override
    public void doSetup(Invoker invoker) {
        doSetup = true;
        super.doSetup(invoker);
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        doTeardown = true;
        super.doTeardown(invoker);
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
        doValidate = true;
        super.doValidate(event);
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        doBeforeExecution = true;
        super.doBeforeExecution(event);
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        doAfterExecution = true;
        super.doAfterExecution(event);
    }

    @Override
    protected void doWhenExceptionThrown(ExecuteInstructionLineEvent event, RuntimeException ex) {
        doWhenExceptionThrown = true;
        super.doWhenExceptionThrown(event, ex);
    }

    @Keyword("Instruction with no arguments")
    public void first() {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{};
    }

    @Keyword("Instruction with invoker")
    @PassInvoker
    public void withInvoker(Invoker invoker) {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{invoker};
    }

    @Keyword("Instruction with one argument")
    public void oneArgument(Integer a) {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{a};
    }

    @Keyword("Instruction with two arguments")
    public int twoArguments(String a, String b) {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{a, b};
        return 2;
    }

    @Keyword("Instruction with variable arguments")
    public void variableArguments(String a, String... b) {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{a, b};
    }

    @Keyword("Invoker missing")
    @PassInvoker
    public void invokerMissing(String argument) {
        throw new UnsupportedOperationException("Never called.");
    }

    @Keyword("Instruction with array calling instruction with variable argument")
    @PassInvoker
    public void array(Invoker invoker, String a, Object[] b) {
        invoker.publish(new ExecuteInstructionLineEvent("Instruction with variable arguments", b));
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{a, b};
    }

    @Keyword("Minimum parameters")
    @Parameters(min = 1)
    public void minimumParameters(String str) {
        methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        arguments = new Object[]{str};
    }

    @Keyword("Incompatible argument")
    @Parameters(min = 1)
    public void incompatibleArgument(int i) {
        throw new UnsupportedOperationException("Never called.");
    }

    @Keyword("Conflicted")
    public void conflicted() {
        throw new UnsupportedOperationException("Never called.");
    }

    @Keyword("Throw Exception")
    public void throwException()
            throws TestException {
        throw new TestException();
    }

    @Keyword("Throw Runtime Exception")
    public void throwRuntimeException() {
        throw new TestRuntimeException();
    }

    @Keyword("Conversion")
    public Output conversion(Converted input) {
        return new Output();
    }
}
