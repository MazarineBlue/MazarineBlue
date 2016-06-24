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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator.Associativity;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.tokens.EndToken;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;
import org.mazarineblue.parser.precedenceclimbing.tokens.Tokens;
import org.mazarineblue.parser.precedenceclimbing.tree.Tree;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ToStringTest {

    @Test
    public void testPrefixNode() {
        PrefixUnaryOperator operator = new PrefixOperatorDummy(1, "-",
                                                               Associativity.LEFT);
        Tree t = Tree.createLeaf(new Token("1"));
        Tree node = Tree.createNode(operator, t);
        assertEquals("-1", node.toString());
    }

    @Test
    public void testPostfixNode() {
        PostfixUnaryOperator operator = new PostfixOperatorDummy(1, "~",
                                                                 Associativity.LEFT);
        Tree t = Tree.createLeaf(new Token("1"));
        Tree node = Tree.createNode(operator, t);
        assertEquals("1~", node.toString());
    }

    @Test
    public void testBinaryNode() {
        BinaryOperator operator = new BinaryOperatorDummy(1, "+",
                                                          Associativity.LEFT);
        Tree left = Tree.createLeaf(new Token("1"));
        Tree right = Tree.createLeaf(new Token("2"));
        Tree node = Tree.createNode(operator, left, right);
        assertEquals("1 + 2", node.toString());
    }

    @Test
    public void testToken() {
        Token token = new Token("1");
        assertEquals("1", token.toString());
    }

    @Test
    public void testEndToken() {
        assertEquals("EndToken", new EndToken().toString());
    }

    @Test
    public void testTokens() {
        Tokens tokens = new Tokens();
        tokens.add(new Token(""));
        tokens.add(new Token("1"));
        tokens.add(new Token("3"));
        tokens.add(new Token("2"));
        assertEquals("1 3 2", tokens.toString());
    }

    private static class PrefixOperatorDummy
            extends PrefixUnaryOperator {

        public PrefixOperatorDummy(int precedence, String str,
                                   Associativity association) {
            super(precedence, str, association);
        }

        @Override
        public Object evaluate(Object operand, VariableSource source) {
            return null;
        }
    }

    private static class PostfixOperatorDummy
            extends PostfixUnaryOperator {

        public PostfixOperatorDummy(int precedence, String str,
                                    Associativity association) {
            super(precedence, str, association);
        }

        @Override
        public Object evaluate(Object operand, VariableSource source) {
            return null;
        }
    }

    private static class BinaryOperatorDummy
            extends BinaryOperator {

        public BinaryOperatorDummy(int precedence, String symbol,
                                   Associativity association) {
            super(precedence, symbol, association);
        }

        @Override
        public Object evaluate(Object left, Object right,
                               VariableSource source) {
            return null;
        }
    }
}
