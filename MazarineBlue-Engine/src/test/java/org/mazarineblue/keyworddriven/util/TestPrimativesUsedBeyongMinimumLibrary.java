/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestPrimativesUsedBeyongMinimumLibrary
        extends Library {

    public static final String PRIMATIVES_USED_BEYOND_MINIMUM_INSTRUCTION = "Wrong use of primatives";

    public TestPrimativesUsedBeyongMinimumLibrary() {
        super("");
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

    @Keyword(PRIMATIVES_USED_BEYOND_MINIMUM_INSTRUCTION)
    @Parameters(min = 1)
    public void test(int first, int second) {
    }
}
