/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.analyser.lexical.StringLexicalAnalyser;
import org.mazarineblue.parser.analyser.lexical.matchers.StringMatcher;
import org.mazarineblue.parser.tokens.Token;
import static org.mazarineblue.parser.tokens.Tokens.createLiteralToken;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class OperatorLexicalAnalyserTest {

    private StringLexicalAnalyser analyser;

    @Before
    public void setup() {
        analyser = new StringLexicalAnalyser();
        analyser.add(new StringMatcher("++"));
        analyser.add(new StringMatcher(">"));
        analyser.add(new StringMatcher(">="));
    }

    @Test
    public void breakdown_Empty() {
        assertBreakdown("", asList());
    }

    @Test
    public void breakdown_Literal() {
        assertBreakdown("foo", asList(createLiteralToken("foo", 0)));
    }

    @Test
    public void breakdown_LiteralOperatorLiteral() {
        List<Token<String>> expected = asList(createLiteralToken("foo", 0),
                                              createLiteralToken("++", 3),
                                              createLiteralToken("++", 6),
                                              createLiteralToken("foo", 8));
        assertBreakdown("foo++ ++foo", expected);
    }

    @Test
    public void parseTwoExpressions() {
        List<Token<String>> expected = asList(createLiteralToken("foo", 0),
                                              createLiteralToken("++", 3),
                                              createLiteralToken("++", 6),
                                              createLiteralToken("foo", 8));
        assertBreakdown("foo++ ++foo", expected);
        assertBreakdown("foo++ ++foo", expected);
    }

    @Test
    public void skipTheImmediateOperator() {
        List<Token<String>> expected = asList(createLiteralToken("foo ", 0),
                                              createLiteralToken(">=", 4),
                                              createLiteralToken(" foo", 6));
        assertBreakdown("foo >= foo", expected);
    }

    private void assertBreakdown(String input, List<Token<String>> tokens) {
        assertEquals(analyser.breakdown(input), unmodifiableList(tokens));
    }
}
