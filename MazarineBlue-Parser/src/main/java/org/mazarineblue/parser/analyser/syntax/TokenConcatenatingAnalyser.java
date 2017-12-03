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

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import static org.mazarineblue.parser.tree.TreeUtil.mkLeaf;
import static org.mazarineblue.parser.tree.TreeUtil.mkNode;

/**
 * A {@code SimpleSyntacticAnalyser} is a {@code SyntacticAnalyser} that
 * concatenates all {@code Tokens}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
public class TokenConcatenatingAnalyser<T>
        implements SyntacticAnalyser<T> {

    @Override
    public SyntaxTreeNode<T> buildTree(List<Token<T>> tokens) {
        if (tokens == null)
            return null;
        if (tokens.isEmpty())
            return null;
        Queue<Token<T>> queue = new ArrayDeque<>(tokens);
        return build(queue, queue.size());
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private SyntaxTreeNode<T> build(Queue<Token<T>> queue, int count) {
        return count == 1 ? buildLeaf(queue)
                : count == 2 ? buildUnary(queue)
                : buildBinary(queue, count / 2, (count - 1) / 2);
    }

    private SyntaxTreeNode<T> buildLeaf(Queue<Token<T>> queue) {
        Token<T> t = queue.remove();
        return mkLeaf(t);
    }

    private SyntaxTreeNode<T> buildUnary(Queue<Token<T>> queue) {
        Token<T> t = queue.remove();
        SyntaxTreeNode<T> rigth = build(queue, 1);
        return mkNode(t, rigth);
    }

    private SyntaxTreeNode<T> buildBinary(Queue<Token<T>> queue, int leftCount, int rigthCount) {
        SyntaxTreeNode<T> left = build(queue, leftCount);
        Token<T> node = queue.remove();
        SyntaxTreeNode<T> rigth = build(queue, rigthCount);
        return mkNode(node, left, rigth);
    }
}
