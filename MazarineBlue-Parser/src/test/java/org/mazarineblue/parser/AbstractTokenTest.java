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

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.parser.tokens.Token;
import static org.mazarineblue.parser.tokens.Tokens.createLiteralToken;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractTokenTest
        extends TestHashCodeAndEquals<Token<String>> {

    @Test
    public void hashCode_DifferentIndexes() {
        int a = createLiteralToken("foo", 0).hashCode();
        int b = createLiteralToken("foo", 9).hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentIndexes() {
        Token<String> a = createLiteralToken("foo", 0);
        Token<String> b = createLiteralToken("foo", 9);
        assertNotEquals(a, b);
    }

    @Override
    protected Token<String> getObject() {
        return createLiteralToken("foo", 0);
    }

    @Override
    protected Token<String> getDifferentObject() {
        return createLiteralToken("oof", 0);
    }
}
