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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.analyser.lexical.StringLexicalAnalyser;
import org.mazarineblue.parser.analyser.lexical.matchers.BracketVariableMatcher;
import org.mazarineblue.parser.analyser.lexical.matchers.SimpleVariableMatcher;
import org.mazarineblue.parser.exceptions.BracketUnclosedException;
import org.mazarineblue.parser.exceptions.IllegalCloseBacketException;
import org.mazarineblue.parser.exceptions.VariableSignMissingException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class VariableLexicalAnalyserTest {

    private StringLexicalAnalyser analyser;

    @Before
    public void setup() {
        analyser = new StringLexicalAnalyser();
        analyser.add(new BracketVariableMatcher());
        analyser.add(new SimpleVariableMatcher());
    }

    @Test
    public void breakdown_Empty() {
        assertBreakdown("");
    }

    @Test
    public void breakdown_Literal() {
        assertBreakdown("foo", Tokens.createLiteralToken("foo", 0));
    }

    @Test
    public void breakdown_SimpleVariable() {
        assertBreakdown("$foo", Tokens.createVariableToken("foo", 0));
    }

    @Test(expected = VariableSignMissingException.class)
    public void breakdown_SimpleVariable_OpeningBracket() {
        analyser.breakdown("$f{oo");
    }

    @Test(expected = IllegalCloseBacketException.class)
    public void breakdown_SimpleVariable_ClosingBracket() {
        analyser.breakdown("$f}oo");
    }

    @Test
    public void breakdown_SimpleVariableAndSimpleVariable() {
        assertBreakdown("$foo$foo", Tokens.createVariableToken("foo", 0), Tokens.createVariableToken("foo", 4));
    }

    @Test
    public void breakdown_SimpleVariableAndLiteral() {
        assertBreakdown("$foo foo", Tokens.createVariableToken("foo", 0), Tokens.createLiteralToken(" foo", 4));
    }

    @Test
    public void breakdown_LiteralAndSimpleVariableAndLiteral() {
        assertBreakdown("foo$foo", Tokens.createLiteralToken("foo", 0), Tokens.createVariableToken("foo", 3));
    }

    @Test(expected = BracketUnclosedException.class)
    public void breakdown_BracketVariable_UnclosedBracket() {
        analyser.breakdown("${foo");
    }

    @Test(expected = VariableSignMissingException.class)
    public void breakdown_BracketVariable_IllegalOpeningBracket() {
        analyser.breakdown("${fo{o}");
    }

    @Test
    public void breakdown_BracketVariable() {
        assertBreakdown("${foo}", Tokens.createVariableToken("foo", 0));
    }

    @Test
    public void breakdown_BracketVariableAndBracketVariable() {
        assertBreakdown("${foo}${foo}", Tokens.createVariableToken("foo", 0), Tokens.createVariableToken("foo", 6));
    }

    @Test
    public void breakdown_BracketVariableAndLiteral() {
        assertBreakdown("${foo}foo", Tokens.createVariableToken("foo", 0), Tokens.createLiteralToken("foo", 6));
    }

    @Test
    public void breakdown_LiteralAndBracketVariable() {
        assertBreakdown("foo${foo}", Tokens.createLiteralToken("foo", 0), Tokens.createVariableToken("foo", 3));
    }

    @Test
    public void breakdown_BracketVariableAndSimpleVariable() {
        assertBreakdown("${foo}$foo", Tokens.createVariableToken("foo", 0), Tokens.createVariableToken("foo", 6));
    }

    @Test
    public void breakdown_SimpleVariableAndBracketVariable() {
        assertBreakdown("$foo${foo}", Tokens.createVariableToken("foo", 0), Tokens.createVariableToken("foo", 4));
    }

    private void assertBreakdown(String input, Token<?>... tokens) {
        assertEquals(analyser.breakdown(input), asList(tokens));
    }
}
