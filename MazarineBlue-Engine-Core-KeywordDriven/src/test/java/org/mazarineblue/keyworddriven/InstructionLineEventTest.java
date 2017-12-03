/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class InstructionLineEventTest {

    private InstructionLineEvent event;

    @After
    public void teardown() {
        event = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class NoArguments {

        @Before
        public void setup() {
            event = new MyInstruction("namespace.keyword");
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

    @SuppressWarnings("PublicInnerClass")
    public class TestCompatibility {

        private Class<?>[] expected;

        @Before
        public void setup() {
            expected = new Class<?>[2];
            expected[0] = String.class;
            expected[1] = String[].class;
        }

        @After
        public void teardown() {
            expected = null;
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualNone1() {
            event = new MyInstruction("namespace.keyword");
            assertFalse(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualNone2() {
            event = new MyInstruction("namespace.keyword");
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualString1() {
            event = new MyInstruction("namespace.keyword", "argument 1");
            assertFalse(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualString2() {
            event = new MyInstruction("namespace.keyword", "argument 1");
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringString1() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2");
            assertTrue(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringString2() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2");
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringStringString1() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2", "argument 3");
            assertTrue(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringStringString2() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2", "argument 3");
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualInteger1() {
            event = new MyInstruction("namespace.keyword", 1);
            assertFalse(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualInteger2() {
            event = new MyInstruction("namespace.keyword", 1);
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringInteger1() {
            event = new MyInstruction("namespace.keyword", "argument 1", 2);
            assertTrue(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringInteger2() {
            event = new MyInstruction("namespace.keyword", "argument 1", 2);
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringStringInteger1() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2", 3);
            assertTrue(event.haveIncompatibleArguments(expected, false));
        }

        @Test
        public void haveIncompatibleArguments_ExpectedStringString_ActualStringStringInteger2() {
            event = new MyInstruction("namespace.keyword", "argument 1", "argument 2", 3);
            assertFalse(event.haveIncompatibleArguments(expected, true));
        }
    }

    private static class MyInstruction
            extends InstructionLineEvent {

        private MyInstruction(String path, Object... arguments) {
            super(path, arguments);
        }
    }
}
