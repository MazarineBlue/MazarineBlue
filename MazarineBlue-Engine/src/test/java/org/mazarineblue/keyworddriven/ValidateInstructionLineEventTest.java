/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ValidateInstructionLineEventTest {

    private ValidateInstructionLineEvent event;

    @Before
    public void setup() {
        event = new ValidateInstructionLineEvent("", "");
    }

    @After
    public void teardown() {
        event = null;
    }

    @Test
    public void setCustomFlags_One_ReturnsInvalidAndOne() {
        event.setUserErrorFlags(1);
        assertFalse(event.isValid());
        assertEquals(1, event.getUserErrorFlags());
    }

    @Test
    public void setCustomFlags_OneAndTwo_ReturnsInvalidAndThree() {
        event.setUserErrorFlags(1);
        event.setUserErrorFlags(2);
        assertFalse(event.isValid());
        assertEquals(3, event.getUserErrorFlags());
    }

    @Test
    public void setCustomFlags_ThreeAndTwelve_ReturnsInvalidAndAndFifteen() {
        event.setUserErrorFlags(3);
        event.setUserErrorFlags(12);
        assertFalse(event.isValid());
        assertEquals(15, event.getUserErrorFlags());
    }
}
