/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.mazarineblue.parser.precedenceclimbing.storage.TokenParser;
import org.mazarineblue.parser.precedenceclimbing.storage.ParserHelper;
import org.mazarineblue.parser.precedenceclimbing.storage.Storage;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableExpectedException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;
import org.mazarineblue.parser.precedenceclimbing.tokens.Tokens;
import org.mazarineblue.parser.precedenceclimbing.tree.Tree;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class PrecedenceClimbingTokenParser
        implements TokenParser {

    private final Tokens tokens;
    private final Storage storage;

    PrecedenceClimbingTokenParser(Tokens tokens, Storage storage) {
        this.tokens = tokens;
        this.storage = storage;
    }

    @Override
    public Tokens getTokens() {
        return tokens;
    }

    Tree parseExpression() {
        Tree node = parseExpression(0);
        tokens.expectEnd();
        return node;
    }

    @Override
    public Tree parseExpression(int precedenceLimit) {
        Tree leftNode = parseParentheses();
        while (nextTokenIsBinaryOperator()) {
            if (nextTokenHasLowerPrecedence(precedenceLimit))
                break;
            leftNode = parseBinaryParentheses(leftNode);
        }
        return leftNode;
    }

    private boolean nextTokenIsBinaryOperator() {
        Token peek = tokens.peek();
        return storage.isBinaryOperator(peek.getToken());
    }

    private boolean nextTokenHasLowerPrecedence(int precedence) {
        Token peek = tokens.peek();
        return storage.precedenceOf(peek) < precedence;
    }

    private Tree parseBinaryParentheses(Tree leftNode) {
        Operator operator = getNextOperator();
        Tree rightNode = parseExpressionRightOfOperator(operator);
        return Tree.createNode((BinaryOperator) operator, leftNode, rightNode);
    }

    private Operator getNextOperator() {
        Token next = tokens.next();
        return storage.getOperator(next);
    }

    private Tree parseExpressionRightOfOperator(Operator operator) {
        int p = operator.associativityPrecedence();
        return parseExpression(p);
    }

    private Tree parseParentheses() {
        Token current = tokens.next();
        Tree specialTree = processSpecialTokenIfPresent(current);
        if (specialTree != null)
            return specialTree;
        if (storage.isPrefixUnaryOperator(current.getToken()))
            return parsePrefixUnaryParentheses(tokens, current);
        if (storage.isVariable(current.getToken()) == false)
            throw new VariableExpectedException(current);
        return isNextPostfixUnaryOperator()
                ? parsePostfixUnaryParentheses(current)
                : Tree.createLeaf(current);
    }

    private Tree processSpecialTokenIfPresent(Token current) {
        for (ParserHelper helper : storage.getParserHelper()) {
            helper.initialize(this);
            if (helper.canParse(current))
                return helper.parse(current);
        }
        return null;
    }

    private Tree parsePrefixUnaryParentheses(Tokens tokens, Token next) {
        Operator operator = storage.getOperator(next);
        Tree node = parseExpression(operator.precedence());
        return Tree.createNode((PrefixUnaryOperator) operator, node);
    }

    private boolean isNextPostfixUnaryOperator() {
        Token peek = tokens.peek();
        String token = peek.getToken();
        return storage.isPostfixUnaryOperator(token);
    }

    private Tree parsePostfixUnaryParentheses(Token current) {
        Tree node = Tree.createLeaf(current);
        Token peek = tokens.next();
        Operator operator = storage.getOperator(peek);
        return Tree.createNode((PostfixUnaryOperator) operator, node);
    }
}
