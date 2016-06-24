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
package org.mazarineblue.parser.precedenceclimbing.factories;

import org.mazarineblue.parser.precedenceclimbing.storage.Storage;
import org.mazarineblue.parser.precedenceclimbing.storage.SearchHelper;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class OperatorSearchingFetchToken
        extends AbstractFetchToken {
    
    private final Storage storage;

    public OperatorSearchingFetchToken(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected Token addCharAt(int index) {
        token += expression.charAt(index);
        for (SearchHelper helper : storage.getSearchHelpers()) {
            helper.initialize(this);
            if (helper.canCreateToken(token))
                return helper.createToken(token);
        }
        if (indexIsAtEnd(index, end))
            return createLastToken(token);
        if (hasOperatorAtNextIteration(token, index))
            return null;
        return createTokenIfOperatorPresent(token);
    }

    private boolean indexIsAtEnd(int index, int end) {
        return index + 1 >= end;
    }

    private Token createLastToken(String token) {
        if (tokenContainsOperator(token))
            return createTokenWithoutOperator(token);
        return createToken(token);
    }

    private boolean hasOperatorAtNextIteration(String token, int index) {
        if (storage.isOperator(token)) {
            char peek = expression.charAt(index + 1);
            if (storage.isOperator(token + peek))
                return true;
        }
        return false;
    }

    private Token createTokenIfOperatorPresent(String token) {
        if (tokenIsOperator(token))
            return createToken(token);
        if (tokenContainsOperator(token))
            return createTokenWithoutOperator(token);
        return null;
    }

    private boolean tokenContainsOperator(String token) {
        int indexOfOperator = storage.indexOfOperator(token);
        return indexOfOperator > 0;
    }

    private boolean tokenIsOperator(String token) {
        return storage.isOperator(token);
    }

    private Token createTokenWithoutOperator(String token) {
        int indexOfOperator = storage.indexOfOperator(token);
        String t = token.substring(0, indexOfOperator);
        return new Token(t, startAtIndex);
    }
}
