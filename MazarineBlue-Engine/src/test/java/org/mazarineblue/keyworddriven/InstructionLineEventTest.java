/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InstructionLineEventTest {

    private InstructionLineEvent event;

    @Before
    public void setup() {
        event = new InstructionLineEvent("namespace.keyword") {};
    }

    @After
    public void teardown() {
        event = null;
    }

    @Test
    public void path() {
        assertEquals("namespace.keyword", event.getPath());
    }

    @Test
    public void namespace() {
        assertEquals("namespace", event.getNamespace());
    }

    @Test
    public void keyword() {
        assertEquals("keyword", event.getKeyword());
    }
}
