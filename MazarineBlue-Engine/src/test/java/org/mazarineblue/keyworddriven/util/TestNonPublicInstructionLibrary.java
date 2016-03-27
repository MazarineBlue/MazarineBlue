/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestNonPublicInstructionLibrary
        extends Library {

    public static final String NON_PUBLIC_LIBRARY_INSTRUCTION = "Non public library instruction";

    public TestNonPublicInstructionLibrary() {
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

    @Keyword(NON_PUBLIC_LIBRARY_INSTRUCTION)
    void test() {
    }
}
