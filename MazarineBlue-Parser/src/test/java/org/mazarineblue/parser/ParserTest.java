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
package org.mazarineblue.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.parser.util.MappedVariableSource;
import org.mazarineblue.parser.util.NullParserDummy;
import org.mazarineblue.parser.exceptions.EmptyExpressionException;
import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.exceptions.InvalidVariableSourceException;
import org.mazarineblue.parser.exceptions.ResultOfUnexpectedTypeException;
import org.mazarineblue.parser.exceptions.TypeNotSpecifiedException;
import org.mazarineblue.parser.util.InputCheckerMock;
import org.mazarineblue.parser.util.IntegerParserDummy;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
@RunWith(HierarchicalContextRunner.class)
public class ParserTest {

    private VariableSource source;

    @Before
    public void setup() {
        source = new MappedVariableSource();
    }

    public class BasicSetup {

        private Parser parser;

        @Before
        public void setup() {
            parser = new InputCheckerMock(new NullParserDummy());
        }

        @Test(expected = InvalidExpressionException.class)
        public void nullExpression_ThrowsInvalidExpressionException() {
            parser.parse(null, source);
        }

        @Test(expected = InvalidVariableSourceException.class)
        public void nullSource_ThrowsInvalidVariableSourceException() {
            parser.parse("bla", null);
        }

        @Test(expected = EmptyExpressionException.class)
        public void emptyExpression_ThrowsEmptyExpressionException() {
            parser.parse("", source);
        }

        @Test(expected = EmptyExpressionException.class)
        public void expceptionOfSpaces_ThrowsEmptyExpressionException() {
            parser.parse("  ", source);
        }

        @Test(expected = TypeNotSpecifiedException.class)
        public void nullType_ThrowsTypeNotSpecifiedException() {
            parser.parse("bla", source, null);
        }

        @Test
        public void dummy_ReturnsNull() {
            assertEquals(null, parser.parse("bla", source, Integer.class));
        }
    }

    public class IntegerStub {

        private Parser parser;

        @Before
        public void setup() {
            parser = new InputCheckerMock(new IntegerParserDummy());
        }

        @Test(expected = ResultOfUnexpectedTypeException.class)
        public void getString_ThrowsResultOfUnexpectedTypeException() {
            assertEquals(new Integer(1), parser.parse("bla", source, String.class));
        }

        @Test
        public void getInteger_ReturnsInteger() {
            assertEquals(new Integer(0), parser.parse("bla", source, Integer.class));
        }
    }
}
