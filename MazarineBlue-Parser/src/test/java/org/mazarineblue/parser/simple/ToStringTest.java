/*
 * Copyright (c) 2015 Alex de Kruijff
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.parser.simple;

import org.mazarineblue.parser.simple.CharacterContainer;
import org.mazarineblue.parser.simple.ProcessorStack;
import org.mazarineblue.parser.simple.Storage;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.parser.variable.AppendToOutputProcessor;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ToStringTest {

    @Test
    public void testCharacterContainer() {
        assertCharacterContainerEquals("c='a', index=0, consumed=false", 'a', 0, false);
        assertCharacterContainerEquals("c='b', index=1, consumed=true", 'b', 1, true);
    }

    private static void assertCharacterContainerEquals(String expected, char c, int index, boolean consumed) {
        CharacterContainer container = new CharacterContainer(c, index);
        if (consumed)
            container.consume();
        assertEquals(expected, container.toString());
    }

    public class GivenAnProcessorStack {

        private ProcessorStack stack;

        @Before
        public void setup() {
            stack = new ProcessorStack();
        }

        @Test
        public void testProcessorEmptyStack() {
            assertEquals("stack=empty", stack.toString());
        }

        @Test
        public void testProcessorWithOneProcessor() {
            stack.add(new AppendToOutputProcessor());
            assertEquals("stack=AppendToOutputProcessor", stack.toString());
        }

        @Test
        public void testProcessorWithTwoProcessors() {
            stack.add(new AppendToOutputProcessor());
            stack.add(new AppendToOutputProcessor());
            assertEquals("stack=AppendToOutputProcessor, AppendToOutputProcessor", stack.toString());
        }
    }

    public class GivenAnStorage {

        private Storage storage;

        @Before
        public void setup() {
            storage = new Storage();
        }
        
        @Test
        public void testEmptyStorage() {
            assertEquals("variable=''", storage.toString());
        }
        
        @Test
        public void testFilledStorage() {
            storage.appendTo(Storage.VARIABLE, "foo");
            assertEquals("variable='foo'", storage.toString());
        }
    }
}
