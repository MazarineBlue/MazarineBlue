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
package org.mazarineblue.parser.precedenceclimbing.tokens;

import java.util.ArrayDeque;
import java.util.Queue;
import org.mazarineblue.parser.precedenceclimbing.exceptions.ExpressionEndExpectedException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.ReachedEndOfLineException;

/**
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public class Tokens {

    public static Token end = new EndToken();
    private final Queue<Token> queue = new ArrayDeque<>();

    @Override
    public String toString() {
        String str = null;
        for (Object obj : queue.toArray())
            str = (str == null ? "" : str + " ") + obj.toString();
        return str == null ? "" : str;
    }

    public void add(Token token) {
        if (token.isEmpty())
            return;
        queue.add(token);
    }

    public Token next() {
        Token peek = peek();
        queue.remove();
        return peek;
    }

    public Token peek() {
        Token token = queue.peek();
        if (token == null)
            throw new ReachedEndOfLineException();
        return token;
    }

    public void expectEnd() {
        Token endToken = queue.peek();
        Class type = endToken.getClass();
        if (type.equals(EndToken.class) == false)
            throw new ExpressionEndExpectedException(endToken.index());
    }

    public Token[] toArray() {
        Token[] arr = new Token[queue.size()];
        return queue.toArray(arr);
    }
}
