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

import org.mazarineblue.parser.precedenceclimbing.storage.Storage;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;
import org.mazarineblue.parser.precedenceclimbing.tokens.Tokens;
import org.mazarineblue.parser.util.NumberValidator;
import org.mazarineblue.parser.util.Operators;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
abstract class AbstractFactoryTest {

    protected StorageTokenFactory factory;
    protected Storage storage;
    private NumberValidator numberValidator;

    @Before
    public void setup() {
        factory = createFactory();
        storage = new Storage();
        factory.setStorage(storage);
        setupOperators(storage);
        numberValidator = new NumberValidator();
    }

    protected abstract StorageTokenFactory createFactory();

    private void setupOperators(Storage storage) {
        Operators operators = new Operators();
        storage.add(operators.add);
        storage.add(operators.multiply);
        storage.add(operators.singleIs);
        storage.add(operators.doubleIs);
        storage.add(operators.negative);
        storage.add(operators.upperCase);
    }

    @Test
    public void testBinaryOperator() {
        assertTokens("11 == 1", "11", "==", "1");
    }

    @Test
    public void testPrefixOperator() {
        assertTokens("- bla", "-", "bla");
    }

    @Test
    public void testPostfixOperator() {
        assertTokens("bla ~", "bla", "~");
    }

    protected void assertTokens(String expression, String... token) {
        Tokens fetchTokens = factory.fetchTokens(expression);
        Token[] arr = fetchTokens.toArray();
        for (int i = 0; i < token.length; ++i)
            assertEquals(new Token(token[i]), arr[i]);
        assertEquals(Tokens.end, arr[token.length]);
    }
}
