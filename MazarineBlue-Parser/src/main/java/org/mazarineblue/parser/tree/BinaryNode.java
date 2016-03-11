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
 * A {@code BinaryNode} is a tree node containing an {@code Operator} and a
 * two reference to another nodes or leafs.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class BinaryNode<T>
        extends Node<T> {

    private final SyntaxTreeNode<T> left;
    private final SyntaxTreeNode<T> rigth;

    BinaryNode(Token<T> token, SyntaxTreeNode<T> left, SyntaxTreeNode<T> right) {
        super(token);
        this.left = left;
        this.rigth = right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + getToken() + " " + rigth + ')';
    }

    @Override
    public SyntaxTreeNode<T> getLeftChild() {
        return left;
    }

    @Override
    public SyntaxTreeNode<T> getRightChild() {
        return rigth;
    }
}