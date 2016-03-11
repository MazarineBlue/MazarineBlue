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
package org.mazarineblue.parser.analyser.syntax;

import java.util.NoSuchElementException;
import java.util.Queue;
import org.mazarineblue.parser.tokens.Token;

/**
 * A {@code TokenManager} is a manager of {@link Token tokens}. This class
 * holds the tokens and increases an index as it gives out tokens.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
public class TokenManager<T> {

    private final Queue<Token<T>> tokens;

    /**
     * Construct a object holding {@link Token tokens}.
     *
     * @param queue the queue with tokens holding the tokens.
     */
    public TokenManager(Queue<Token<T>> queue) {
        this.tokens = queue;
    }

    /**
     * Retrieves, but does not remove, the next {@link Token} or returns {@code
     * null} if this queue is empty.
     *
     * @return the next token or {@code null} if there are non.
     */
    public Token<T> peek() {
        return tokens.peek();
    }

    /**
     * Retrieves and removes the next {@code Token}.
     *
     * @return the next token.
     *
     * @throws NoSuchElementException there are no more tokens.
     */
    public Token<T> next() {
        return tokens.remove();
    }

    /**
     * Returns {@code true} if this manager contains no tokens.
     *
     * @return {@code true} if this collection contains no elements.
     */
    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
