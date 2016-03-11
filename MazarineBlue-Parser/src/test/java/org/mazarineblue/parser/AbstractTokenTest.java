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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractTokenTest {

    @Test
    public void equals_OneTokenAndNull() {
        Token<String> a = Tokens.createLiteralToken("foo", 0);
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClasses() {
        Token<String> a = Tokens.createLiteralToken("foo", 0);
        assertNotEquals(a, "");
    }

    @Test
    public void hashCode_DifferentValues() {
        int a = Tokens.createLiteralToken("foo", 0).hashCode();
        int b = Tokens.createLiteralToken("oof", 0).hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentValues() {
        Token<String> a = Tokens.createLiteralToken("foo", 0);
        Token<String> b = Tokens.createLiteralToken("oof", 0);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentIndexes() {
        int a = Tokens.createLiteralToken("foo", 0).hashCode();
        int b = Tokens.createLiteralToken("foo", 9).hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_EqualTokens() {
        int a = Tokens.createLiteralToken("foo", 0).hashCode();
        int b = Tokens.createLiteralToken("foo", 0).hashCode();
        assertEquals(a, b);
    }

    @Test
    public void equals_EqualTokens() {
        Token<String> a = Tokens.createLiteralToken("foo", 0);
        Token<String> b = Tokens.createLiteralToken("foo", 0);
        assertEquals(a, b);
    }
}
