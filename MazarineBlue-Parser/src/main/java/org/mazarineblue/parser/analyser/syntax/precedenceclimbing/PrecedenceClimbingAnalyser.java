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
package org.mazarineblue.parser.analyser.syntax.precedenceclimbing;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import org.mazarineblue.parser.analyser.syntax.SyntacticAnalyser;
import org.mazarineblue.parser.analyser.syntax.TokenManager;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.Storage;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.exceptions.SyntacticExpressionException;
import org.mazarineblue.parser.tokens.Token;
import static org.mazarineblue.parser.tokens.Tokens.createSpecialMarkToken;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import static org.mazarineblue.parser.tree.TreeUtil.mkLeaf;
import static org.mazarineblue.parser.tree.TreeUtil.mkNode;

/**
 * A {@code PrecedenceClimbingAnalyser} is a {@code SyntacticAnalyser} that
 * implements the <a href="http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm">
 * "Precedence Climbing"</a> algorithm as described by Theodore Norvell.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
public class PrecedenceClimbingAnalyser<T>
        implements PrecedenceClimbingRegister<T>, SyntacticAnalyser<T> {

    private final Token<T> end = createSpecialMarkToken("END");
    private final Storage<T> storage = new Storage<>();

    @Override
    public void addGroupCharacters(T open, T close) {
        storage.addGroupCharacters(open, close);
    }

    @Override
    public void addOperator(T identifier, BinaryOperator operator) {
        storage.addOperator(identifier, operator);
    }

    @Override
    public void addOperator(T identifier, UnaryOperator operator) {
        storage.addOperator(identifier, operator);
    }

    @Override
    public SyntaxTreeNode<T> buildTree(List<Token<T>> tokens) {
        if (tokens == null)
            throw new SyntacticExpressionException(0);
        Queue<Token<T>> queue = new ArrayDeque<>(tokens);
        queue.add(end);
        return new ParserHelper(new TokenManager<>(queue)).parse();
    }

    private class ParserHelper {

        private final TokenManager<T> tokens;

        private ParserHelper(TokenManager<T> tokens) {
            this.tokens = tokens;
        }

        private SyntaxTreeNode<T> parse() {
            SyntaxTreeNode<T> node = parseExpression(0);
            expect(end);
            return node;
        }

        private void expect(Token<T> token) {
            if (!tokens.peek().equals(token))
                throw new SyntacticExpressionException(token.getIndex());
            tokens.next();
        }

        private SyntaxTreeNode<T> parseExpression(int limit) {
            SyntaxTreeNode<T> tree = parseParentheses();
            if (tokens.isEmpty())
                throw new SyntacticExpressionException(0);
            while (nextTokenIsBinaryOperator() && getNextBinaryOperatorgetPrecedence() >= limit)
                tree = parseExpressionLoop(tree);
            return tree;
        }

        private boolean nextTokenIsBinaryOperator() {
            return storage.isBinary(tokens.peek());
        }

        private int getNextBinaryOperatorgetPrecedence() {
            return storage.binary(tokens.peek()).getPrecedence();
        }

        private SyntaxTreeNode<T> parseParentheses() {
            Token<T> token = tokens.next();
            if (storage.isUnary(token))
                return parseUnaryTerm(token);
            if (storage.isStartOfGroup(token.getValue()))
                return parseGrouping(token);
            if (storage.isSentinel(token))
                return parseSentinal(token);
            throw new SyntacticExpressionException(token.getIndex());
        }

        private SyntaxTreeNode<T> parseUnaryTerm(Token<T> token) {
            Operator operator = storage.unary(token);
            SyntaxTreeNode<T> node = parseExpression(operator.getPrecedence());
            return mkNode(token, node);
        }

        private SyntaxTreeNode<T> parseGrouping(Token<T> token) {
            SyntaxTreeNode<T> tree = parseExpression(0);
            expect(storage.getMatchingClose(token.getValue()), token.getIndex());
            return tree;
        }

        private void expect(T t, int index) {
            T value = tokens.peek().getValue();
            if (value == null || !value.equals(t))
                throw new SyntacticExpressionException(index);
            tokens.next();
        }

        private SyntaxTreeNode<T> parseSentinal(Token<T> token) {
            return mkLeaf(token);
        }

        private SyntaxTreeNode<T> parseExpressionLoop(SyntaxTreeNode<T> tree) {
            Token<T> token = tokens.next();
            Operator operator = storage.binary(token);
            int p = operator.getAssociativity() == LEFT
                    ? operator.getPrecedence() + 1 : operator.getPrecedence();
            return mkNode(token, tree, parseExpression(p));
        }
    }
}
