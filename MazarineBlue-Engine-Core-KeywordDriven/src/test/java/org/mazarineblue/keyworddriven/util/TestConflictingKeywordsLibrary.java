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
public class TestConflictingKeywordsLibrary
        extends Library {

    public TestConflictingKeywordsLibrary() {
        super("");
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Keyword("Conflict")
    @Keyword("Conflict")
    public void test() {
        // The test is about the use of conflicting keywords.
    }
}
