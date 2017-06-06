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
package org.mazarineblue.parser;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.analyser.semantic.TreeEvaluatorAnalyser;
import org.mazarineblue.parser.exceptions.IllegalSyntaxTreeException;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import org.mazarineblue.parser.tree.TreeUtil;
import org.mazarineblue.parser.util.BinarySpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TreeEvaluatorAnalyserTest
        extends AbstractSemanticAnalyserTest {

    private TreeEvaluatorAnalyser<Object> analyser;

    @Before
    public void setup() {
        analyser = new TreeEvaluatorAnalyser<>(t -> t);
    }

    @After
    public void teardown() {
        analyser = null;
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_Null() {
        analyser.evaluate(null);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_LeafWithNullToken_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkLeaf(null);
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_LeafWithNullValue_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkLeaf(token(null));
        analyser.evaluate(tree);
    }

    @Test
    public void evaluate_Leaf_ReturnsString() {
        SyntaxTreeNode<String> tree = TreeUtil.mkLeaf(token("leaf"));
        assertEquals("leaf", analyser.evaluate(tree));
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_UnaryWithNullToken_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(null, leaf("child"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_UnaryWithNullValue_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token(null), leaf("child"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_UnaryWithNullChild_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("unary"), null, null);
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_UnaryWithoutOperator_ThrowIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("unary"), leaf("child"));
        analyser.evaluate(tree);
    }

    @Test
    public void evaluate_UnaryWithOperator_ReturnsValue() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("unary"), leaf("child"));
        analyser.addFunction("unary", (t) -> 1);
        assertEquals(1, analyser.evaluate(tree));
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_BinaryWithNullToken_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(null, leaf("left"), leaf("right"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_BinaryWithNullValue_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token(null), leaf("left"), leaf("right"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_BinaryWithLeftChildNull_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), null, leaf("right"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_Binary_WithLeftChildNullValue_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), leaf(null), leaf("right"));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_Binary_WithRightChildNull_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), leaf("left"), null);
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_Binary_WithRightChildNullValue_ThrowsIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), leaf("left"), leaf(null));
        analyser.evaluate(tree);
    }

    @Test(expected = IllegalSyntaxTreeException.class)
    public void evaluate_BinaryWithoutOperator_ThrowIllegalSyntaxTreeException() {
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), leaf("left"), leaf("right"));
        assertEquals("left binary right", analyser.evaluate(tree));
    }

    @Test
    public void evaluate_BinaryWithOperator_ReturnValue() {
        BinarySpy<Object, Object, Object> spy = new BinarySpy<>(2);
        SyntaxTreeNode<String> tree = TreeUtil.mkNode(token("binary"), leaf("left"), leaf("right"));
        analyser.addFunction("binary", spy);
        assertEquals(2, analyser.evaluate(tree));
        assertEquals("left", spy.getLeft());
        assertEquals("right", spy.getRight());
    }
}
