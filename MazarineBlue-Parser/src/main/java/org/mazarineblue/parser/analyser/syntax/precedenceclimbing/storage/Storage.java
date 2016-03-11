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
package org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Operator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.PrecedenceClimbingRegister;
import org.mazarineblue.parser.tokens.Token;

/**
 * A {@code OperatorsStorage} is register of elements and {@code Operator
 * Operators}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
public class Storage<T>
        implements PrecedenceClimbingRegister<T> {

    private final Map<T, T> groupings = new HashMap<>(4);
    private final Map<T, BinaryOperator> binaries = new HashMap<>(16);
    private final Map<T, UnaryOperator> unaries = new HashMap<>(16);

    @Override
    public void addGroupCharacters(T open, T close) {
        groupings.put(open, close);
    }

    /**
     * Test if the specified element indicates the start of a grouping.
     *
     * @param element the element to test.
     * @return {@code true} if the element indicates that start of a grouping.
     */
    public boolean isStartOfGroup(T element) {
        return groupings.containsKey(element);
    }

    /**
     * Returns the closing element that is paired with the specified start
     * element.
     *
     * @param open the element to use in the search.
     * @return the closing element that is paired with the open element.
     */
    public T getMatchingClose(T open) {
        return groupings.get(open);
    }

    /**
     * Tests if the specified {@code Token} is a sentinel, that is not an
     * {@link BinaryOperator} or {@link UnaryOperator}.
     *
     * @param token the {@code Token} to test.
     * @return {@code true} if the {@code Token} is a sentinel.
     */
    public boolean isSentinel(Token<T> token) {
        return !isBinary(token) && !isUnary(token);
    }

    @Override
    public void addOperator(T identifier, BinaryOperator operator) {
        if (operator == null)
            throw new IllegalArgumentException();
        binaries.put(identifier, operator);
    }

    /**
     * Tests if the specified {@code Token} is a {@link BinaryOperator}.
     *
     * @param token the {@code Token} to test.
     * @return {@code true} if the {@code Token} is a {@code BinaryOperator}.
     */
    public boolean isBinary(Token<T> token) {
        return binaries.containsKey(token.getValue());
    }

    /**
     * Converts the specified {@code Token} in to a {@link BinaryOperator}.
     *
     * @param token the {@code Token} to convert.
     * @return the {@link BinaryOperator}.
     */
    public Operator binary(Token<T> token) {
        return binaries.get(token.getValue());
    }

    @Override
    public void addOperator(T identifier, UnaryOperator operator) {
        if (operator == null)
            throw new IllegalArgumentException();
        unaries.put(identifier, operator);
    }

    /**
     * Tests if the specified {@code Token} is an {@link UnaryOperator}.
     *
     * @param token the {@code Token} to test.
     * @return {@code true} if the {@code Token} is a {@code UnaryOperator}.
     */
    public boolean isUnary(Token<T> token) {
        return unaries.containsKey(token.getValue());
    }

    /**
     * Converts the specified {@code Token} in to a {@link UnaryOperator}.
     *
     * @param token the {@code Token} to convert.
     * @return the {@link UnaryOperator}.
     */
    public Operator unary(Token<T> token) {
        return unaries.get(token.getValue());
    }
}
