/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestEmptyLibrary
        extends Library {

    public TestEmptyLibrary(String test) {
        super(test);
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
