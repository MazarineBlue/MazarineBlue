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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.Storage;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.tokens.Token;
import static org.mazarineblue.parser.tokens.Tokens.createLiteralToken;

@RunWith(HierarchicalContextRunner.class)
public class StorageTest {

    private Storage<String> storage;

    @Before
    public void setup() {
        storage = new Storage<>();
        storage.addOperator("u", new UnaryOperator(1, LEFT));
        storage.addOperator("b", new BinaryOperator(1, LEFT));
        storage.addOperator("c", new UnaryOperator(1, LEFT));
        storage.addOperator("c", new BinaryOperator(1, LEFT));
    }

    @After
    public void teardown() {
        storage = null;
    }

    private abstract class TestTokenFlagMethod {

        @Test
        public void is_UnaryOperator_False() {
            boolean expected = expected("u");
            boolean actual = actual(createLiteralToken("u", -1));
            assertEquals(expected, actual);
        }

        @Test
        public void is_BinaryOperator_False() {
            boolean expected = expected("b");
            boolean actual = actual(createLiteralToken("b", -1));
            assertEquals(expected, actual);
        }

        @Test
        public void is_CombinedOperator_False() {
            boolean expected = expected("c");
            boolean actual = actual(createLiteralToken("c", -1));
            assertEquals(expected, actual);
        }

        @Test
        public void is_LiteralOperator_False() {
            boolean expected = expected("l");
            boolean actual = actual(createLiteralToken("l", -1));
            assertEquals(expected, actual);
        }

        protected abstract boolean expected(String t);

        protected abstract boolean actual(Token<String> t);
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestIsSentinelMethod
            extends TestTokenFlagMethod {

        @Override
        protected boolean expected(String t) {
            return "l".equals(t);
        }

        @Override
        protected boolean actual(Token<String> t) {
            return storage.isSentinel(t);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestIsUnaryMethod
            extends TestTokenFlagMethod {

        @Override
        protected boolean expected(String t) {
            return "u".equals(t) || "c".equals(t);
        }

        @Override
        protected boolean actual(Token<String> t) {
            return storage.isUnary(t);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestIsBinaryMethod
            extends TestTokenFlagMethod {

        @Override
        protected boolean expected(String t) {
            return "b".equals(t) || "c".equals(t);
        }

        @Override
        protected boolean actual(Token<String> t) {
            return storage.isBinary(t);
        }
    }
}
