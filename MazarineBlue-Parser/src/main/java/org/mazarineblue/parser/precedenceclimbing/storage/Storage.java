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
package org.mazarineblue.parser.precedenceclimbing.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mazarineblue.parser.precedenceclimbing.exceptions.OperatorNotFoundException;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;

/**
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public class Storage {

    private final List<SpecialCharacter> specialCharacters = new ArrayList();
    private final Map<String, BinaryOperator> binaryOperators = new HashMap();
    private final Map<String, PostfixUnaryOperator> postfixOperators = new HashMap();
    private final Map<String, PrefixUnaryOperator> prefixOperators = new HashMap();
    private int largestSymbolLength = 0;
    private VariableValidator validator;

    public Storage() {
        this.validator = new VariableValidatorDummy();
    }

    public Iterable<SearchHelper> getSearchHelpers() {
        return new ArrayList(specialCharacters);
    }

    public Iterable<ParserHelper> getParserHelper() {
        return new ArrayList(specialCharacters);
    }

    public void setValidator(VariableValidator validator) {
        this.validator = validator;
    }

    public void add(SpecialCharacter token) {
        specialCharacters.add(token);
    }

    public void add(BinaryOperator operator) {
        BinaryOperator put = binaryOperators.put(operator.symbol(), operator);
        adjustLargestSize(operator.symbol());
    }

    public void add(PostfixUnaryOperator operator) {
        postfixOperators.put(operator.symbol(), operator);
        adjustLargestSize(operator.symbol());
    }

    public void add(PrefixUnaryOperator operator) {
        prefixOperators.put(operator.symbol(), operator);
        adjustLargestSize(operator.symbol());
    }

    private void adjustLargestSize(String symbol) {
        if (largestSymbolLength < symbol.length())
            largestSymbolLength = symbol.length();
    }

    public int indexOfOperator(String token) {
        int n = token.length();
        int start = n <= largestSymbolLength ? 0 : n - largestSymbolLength;
        for (int i = start; i < n; ++i)
            if (isOperator(token.substring(i, n)))
                return i;
        return -1;
    }

    public boolean isVariable(String token) {
        if (isOperator(token))
            return false;
        return validator.validate(token);
    }

    public boolean isOperator(String token) {
        return isBinaryOperator(token)
                || isPostfixUnaryOperator(token)
                || isPrefixUnaryOperator(token);
    }

    public boolean isBinaryOperator(String token) {
        return binaryOperators.containsKey(token);
    }

    public boolean isPostfixUnaryOperator(String token) {
        return postfixOperators.containsKey(token);
    }

    public boolean isPrefixUnaryOperator(String token) {
        return prefixOperators.containsKey(token);
    }

    public int precedenceOf(Token next) {
        return getOperator(next).precedence();
    }

    public Operator getOperator(Token token) {
        String key = token.getToken();
        if (binaryOperators.containsKey(key))
            return binaryOperators.get(key);
        if (postfixOperators.containsKey(key))
            return postfixOperators.get(key);
        if (prefixOperators.containsKey(key))
            return prefixOperators.get(key);
        throw new OperatorNotFoundException(key);
    }
}
