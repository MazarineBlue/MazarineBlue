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
import org.mazarineblue.parser.exceptions.EmptyExpressionException;
import org.mazarineblue.parser.util.NumberValidator;
import org.mazarineblue.parser.precedenceclimbing.PrecedenceClimbingParser;
import org.mazarineblue.parser.precedenceclimbing.exceptions.ReachedEndOfLineException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableExpectedException;
import org.mazarineblue.parser.precedenceclimbing.factories.OperatorSearchingFactory;
import org.mazarineblue.parser.util.MappedVariableSource;
import org.mazarineblue.parser.util.Operators;
import org.mazarineblue.parser.util.SpecialTokenSpy;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
@RunWith(HierarchicalContextRunner.class)
public class PrecedenceClimbingParserTest {

    private PrecedenceClimbingParser parser;
    private VariableSource source;
    private Operators operators;
    private NumberValidator numberValidator;

    @Before
    public void setup() {
        parser = new PrecedenceClimbingParser(new OperatorSearchingFactory());
        source = new MappedVariableSource();
        operators = new Operators();
        numberValidator = new NumberValidator();
    }

    @Test(expected = EmptyExpressionException.class)
    public void testEmptyInput() {
        parser.parse("", source);
    }

    @Test
    public void testSpecialToken() {
        SpecialTokenSpy spy = new SpecialTokenSpy();
        parser.add(spy);
        parser.parse("2+2", source);
        assertEquals(true, spy.isCalledDuringTokenCreation());
        assertEquals(true, spy.isCalledDuringParsing());
    }

    @Test
    public void testPrefixUnaryOperator() {
        parser.add(operators.negative);
        assertEquals(-1, parser.parse("-1", source));
    }

    @Test
    public void testPostfixUnaryOperator() {
        parser.add(operators.upperCase);
        assertEquals("BLA", parser.parse("bla~", source));
    }

    public class GivenAddOperator {

        @Before
        public void setup() {
            parser.add(operators.add);
        }

        @Test(expected = ReachedEndOfLineException.class)
        public void testReachedEndOfLine() {
            assertEquals("", parser.parse("1+", source));
        }

        @Test
        public void testBinaryOperator() {
            assertEquals(4, parser.parse("2+2", source));
        }

        @Test
        public void testPrecedence() {
            parser.add(operators.multiply);
            assertEquals(8, parser.parse("2*2+2*2", source));
        }

        @Test(expected = VariableExpectedException.class)
        public void testDoubleOperator() {
            parser.parse("2+++2", source);
        }

        @Test(expected = VariableExpectedException.class)
        public void testNumberValidatorRainy() {
            parser.setValidator(numberValidator);
            parser.parse("2a", source);
        }

        @Test
        public void testNumberValidatorHappy() {
            parser.setValidator(numberValidator);
            parser.parse("2", source);
        }
    }
}
