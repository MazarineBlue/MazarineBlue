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
package org.mazarineblue.parser.precedenceclimbing;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.characters.CloseBracketCharacter;
import org.mazarineblue.parser.precedenceclimbing.characters.OpenBracketCharacter;
import org.mazarineblue.parser.precedenceclimbing.characters.QuotedSpecialCharacter;
import org.mazarineblue.parser.precedenceclimbing.exceptions.CloseBracketExpectedException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.ExpressionEndExpectedException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.OpenBracketUnexpectedException;
import org.mazarineblue.parser.precedenceclimbing.factories.OperatorSearchingFactory;
import org.mazarineblue.parser.precedenceclimbing.operators.OperatorFacade;
import org.mazarineblue.parser.util.MappedVariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SpecialCharactersTest {

    private PrecedenceClimbingParser parser;
    private VariableSource source;

    @Before
    public void setup() {
        parser = new PrecedenceClimbingParser(new OperatorSearchingFactory());
        source = new MappedVariableSource();
    }

    private void assertExpressionEquals(String expression, Object expectedResult) {
        assertEquals(expectedResult, parser.parse(expression, source));
    }

    @Test
    public void testQuotes() {
        parser.add(new QuotedSpecialCharacter());
        parser.add(OperatorFacade.createAddition(0, "+"));
        assertExpressionEquals("\"1+1\"", "1+1");
        assertExpressionEquals("1+1", 2);
    }

    @Test(expected = OpenBracketUnexpectedException.class)
    public void testOpenBracketExpectedException() {
        parser.add(new CloseBracketCharacter());
        assertExpressionEquals(")", "");
    }

    @Test(expected = CloseBracketExpectedException.class)
    public void testCloseBracketExpectedException() {
        parser.add(new OpenBracketCharacter());
        assertExpressionEquals("(", "");
    }

    @Test
    public void testParentheses() {
        parser.add(new OpenBracketCharacter());
        parser.add(new CloseBracketCharacter());
        assertEquals("bla", parser.parse("(bla)", source));
    }

    @Test(expected = ExpressionEndExpectedException.class)
    public void testExpressionEndExpected() {
        parser.add(new OpenBracketCharacter());
        parser.add(new CloseBracketCharacter());
        assertEquals("", parser.parse("(bla)bla", source));
    }
}
