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
package org.mazarineblue.parser.tokens;

/**
 * This class consists exclusively of static methods that create tokens.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class Tokens {

    /**
     * Construct a literal {@link LiteralToken}.
     *
     * @param value the value to construct the token with.
     * @param index the index where the token starts.
     * @return the newly created token.
     */
    public static Token<String> createLiteralToken(String value, int index) {
        return new LiteralToken(value, index);
    }

    /**
     * Construct a {@link VariableToken}.
     *
     * @param value the value to construct the token with.
     * @param index the index where the token starts.
     * @return the newly created token.
     */
    public static Token<String> createVariableToken(String value, int index) {
        return new VariableToken(value, index);
    }

    /**
     * Test is a token is a variable.
     *
     * @param token the token to test.
     * @return true if the token is a variable.
     */
    public static boolean isVariable(Token<String> token) {
        return token instanceof VariableToken;
    }

    /**
     * Construct a {@link SpecialMarkToken} with a the specified identifier.
     *
     * @param <T>        this can be whatever you need it to be.
     * @param identifier the identifier used as the identifier mark.
     * @return the newly created token.
     */
    public static <T> Token<T> createSpecialMarkToken(String identifier) {
        return new SpecialMarkToken<>(identifier);
    }

    private Tokens() {
    }
}
