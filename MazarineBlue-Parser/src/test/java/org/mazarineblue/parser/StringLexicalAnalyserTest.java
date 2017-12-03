/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.analyser.lexical.StringLexicalAnalyser;
import org.mazarineblue.parser.analyser.lexical.matchers.StringMatcher;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

public class StringLexicalAnalyserTest {

    private StringLexicalAnalyser analyser;

    @Before
    public void setup() {
        analyser = new StringLexicalAnalyser();
    }

    @After
    public void teardown() {
        analyser = null;
    }

    @Test
    public void test1() {
        analyser.add(new StringMatcher("aa"));
        analyser.add(new StringMatcher("a"));
        List<Token<String>> expected = asList(Tokens.createLiteralToken("a", 0));
        List<Token<String>> actual = analyser.breakdown("a");
        assertEquals(expected, actual);
    }

    @Test
    public void test11() {
        analyser.add(new StringMatcher("aa"));
        analyser.add(new StringMatcher("a"));
        List<Token<String>> expected = asList(Tokens.createLiteralToken("aa", 0));
        assertEquals(expected, analyser.breakdown("aa"));
    }

    @Test
    public void test2() {
        analyser.add(new StringMatcher("a"));
        analyser.add(new StringMatcher("aa"));
        List<Token<String>> expected = asList(Tokens.createLiteralToken("a", 0));
        assertEquals(expected, analyser.breakdown("a"));
    }

    @Test
    public void test22() {
        analyser.add(new StringMatcher("a"));
        analyser.add(new StringMatcher("aa"));
        List<Token<String>> expected = asList(Tokens.createLiteralToken("aa", 0));
        assertEquals(expected, analyser.breakdown("aa"));
    }
}
