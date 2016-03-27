/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestFilledLibrarySpy
        extends Library {

    public static final String NON_EXISTING_INSTRUCTION = "foo";
    public static final String ZERO_ARGUMENTS_INSTRUCTION = "Zero";
    public static final String ONE_ARGUMENT_INSTRUCTION = "One";
    public static final String INVOKER_ONE_ARGUMENT_INSTRUCTION = "Invoker One";

    private int count = 0;

    public TestFilledLibrarySpy(String namespace) {
        super(namespace);
    }

    @Keyword(ZERO_ARGUMENTS_INSTRUCTION)
    public void test() {
        ++count;
    }

    @Keyword(ONE_ARGUMENT_INSTRUCTION)
    @Parameters(min = 1)
    public void test(int number) {
        ++count;
    }

    @Keyword(INVOKER_ONE_ARGUMENT_INSTRUCTION)
    @PassInvoker
    public void test(Invoker invoker, Integer number) {
        ++count;
    }

    public int calledCount() {
        return count;
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
    }
}
