/*
 * Copyright (c) 2011-2013 Alex de Kruijff
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
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.exceptions.EmptyExpressionException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.SpecialCharacter;
import org.mazarineblue.parser.precedenceclimbing.tokens.Tokens;
import org.mazarineblue.parser.precedenceclimbing.tree.Tree;
import org.mazarineblue.parser.precedenceclimbing.storage.VariableValidator;

/**
 * An parser that uses a list of specified binary and unary operators to parse a
 * input string and returns a expression. This is an implementation of the
 * <a href="http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm">Precedence
 * Climbing</a> algorithm as described by Theodore Norvell.
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public class PrecedenceClimbingParser
        implements Parser {

    private final TokenFactory factory;
    private final Storage storage;

    public PrecedenceClimbingParser(StorageTokenFactory factory) {
        this((TokenFactory) factory);
        factory.setStorage(storage);
    }

    public PrecedenceClimbingParser(TokenFactory factory) {
        this.factory = factory;
        this.storage = new Storage();
    }

    public void setValidator(VariableValidator validator) {
        storage.setValidator(validator);
    }

    public void add(SpecialCharacter token) {
        storage.add(token);
    }

    public void add(BinaryOperator operator) {
        storage.add(operator);
    }

    public void add(PostfixUnaryOperator operator) {
        storage.add(operator);
    }

    public void add(PrefixUnaryOperator operator) {
        storage.add(operator);
    }

    @Override
    public Object parse(String expression, VariableSource source) {
        Parser.checkInput(expression, source);
        if (expression.trim().isEmpty())
            throw new EmptyExpressionException();
        return parseExpression(expression, source);
    }

    private Object parseExpression(String expression, VariableSource source) {
        Tokens tokens = factory.fetchTokens(expression);
        PrecedenceClimbingTokenParser parser = new PrecedenceClimbingTokenParser(tokens, storage);
        Tree tree = parser.parseExpression();
        return tree.evaluate(source);
    }
}
