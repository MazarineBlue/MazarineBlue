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
package org.mazarineblue.parser.tree;

import org.mazarineblue.parser.tokens.Token;

/**
 * A utility class that creates {@code SyntaxTree} nodes.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class TreeUtil {

    /**
     * Creates a leaf node that contains the specified {@code Token}.
     *
     * @param <T>   the parser input type.
     * @param token the specified token determines the value of the leaf.
     * @return a leaf with the token.
     */
    public static <T> SyntaxTreeNode<T> mkLeaf(Token<T> token) {
        return new Leaf<>(token);
    }

    /**
     * Test if the specified node is a leaf.
     *
     * @param <T>  the parser input type.
     * @param node the tree to test.
     * @return true if the tree is a binary.
     */
    public static <T> boolean isLeaf(SyntaxTreeNode<T> node) {
        return node instanceof Leaf;
    }

    /**
     * Creates a node with one child node and an {@code Operator} that works on
     * it.
     *
     * @param <T>   the parser input type.
     * @param token the specified token determines the value of the operator.
     * @param child the child node that the operator works on.
     * @return a node with the token.
     */
    public static <T> SyntaxTreeNode<T> mkNode(Token<T> token, SyntaxTreeNode<T> child) {
        return new UnaryNode<>(token, child);
    }

    /**
     * Test if the specified node is a unary.
     *
     * @param <T>  the parser input type.
     * @param node the tree to test.
     * @return true if the tree is a leaf.
     */
    public static <T> boolean isUnary(SyntaxTreeNode<T> node) {
        return node instanceof UnaryNode;
    }

    /**
     * Creates a node with two children nodes an an {@code Operator} that
     * works on them.
     *
     * @param <T>   the parser input type.
     * @param token the specified token determines the value of the operator.
     * @param left  the left child node that the operator works on.
     * @param right the right child node that the operator works on.
     * @return a node with the token.
     */
    public static <T> SyntaxTreeNode<T> mkNode(Token<T> token, SyntaxTreeNode<T> left, SyntaxTreeNode<T> right) {
        return new BinaryNode<>(token, left, right);
    }

    /**
     * Test if the specified node is a binary.
     *
     * @param <T>  the parser input type.
     * @param node the tree to test.
     * @return true if the tree is a binary.
     */
    public static <T> boolean isBinary(SyntaxTreeNode<T> node) {
        return node instanceof BinaryNode;
    }

    private TreeUtil() {
    }
}
