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
package org.mazarineblue.parser.precedenceclimbing.tree;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TreeTest {

    @Test
    public void getFirst_Leaf_ReturnsLeaf() {
        Tree leaf = Tree.createLeaf(new Token("left"));
        assertEquals(leaf, leaf.getFirst());
    }

    @Test
    public void getFirst_PrefixUnaryNode_ReturnsLeaf() {
        PrefixUnaryOperator operator = null;
        Tree leaf = Tree.createLeaf(new Token("leaf"));
        Tree tree = Tree.createNode(operator, leaf);
        assertEquals(leaf, tree.getFirst());
    }

    @Test
    public void getFirst_PostfixUnaryNode_ReturnsLeaf() {
        PostfixUnaryOperator operator = null;
        Tree leaf = Tree.createLeaf(new Token("leaf"));
        Tree tree = Tree.createNode(operator, leaf);
        assertEquals(leaf, tree.getFirst());
    }

    @Test
    public void getFirst_BinaryNode_ReturnsLeaf() {
        BinaryOperator operator = null;
        Tree left = Tree.createLeaf(new Token("left"));
        Tree right = Tree.createLeaf(new Token("left"));
        Tree tree = Tree.createNode(operator, left, right);
        assertEquals(left, tree.getFirst());
    }
}
