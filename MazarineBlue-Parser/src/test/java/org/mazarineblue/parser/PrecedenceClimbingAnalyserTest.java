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
import static org.mazarineblue.parser.ParserTestUtil.createTokens;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.RIGHT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.PrecedenceClimbingAnalyser;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.exceptions.SyntacticExpressionException;
import org.mazarineblue.parser.tree.SyntaxTreeNode;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PrecedenceClimbingAnalyserTest {

    private PrecedenceClimbingAnalyser<String> analyser;

    @Before
    public void setup() {
        analyser = new PrecedenceClimbingAnalyser<>();
    }

    @After
    public void teardown() {
        analyser = null;
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Null_ThrowsSyntacticExpressionException() {
        analyser.buildTree(null);
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Empty_ThrowsSyntacticExpressionException() {
        analyser.buildTree(createTokens());
    }

    @Test
    public void buildTree_SingleToken_ReturnsLeaf() {
        SyntaxTreeNode<String> tree = analyser.buildTree(createTokens("a"));
        assertEquals("a", tree.toString());
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Unary_ThrowsSyntacticExpressionException() {
        analyser.addOperator("a", new UnaryOperator(1, LEFT));
        analyser.buildTree(createTokens("a"));
    }

    @Test
    public void buildTree_UnaryPlusToken_ReturnsNode() {
        analyser.addOperator("a", new UnaryOperator(1, LEFT));
        SyntaxTreeNode<String> tree = analyser.buildTree(createTokens("a", "b"));
        assertEquals("(a b)", tree.toString());
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Binary_ThrowsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.buildTree(createTokens("b"));
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_TokenPlusBinary_ThrowsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.buildTree(createTokens("a", "b"));
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_BinaryPlusToken_ThrowsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.buildTree(createTokens("b", "c"));
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Binaries_ReturnsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.addOperator("d", new BinaryOperator(1, LEFT));
        analyser.buildTree(createTokens("b", "d"));
    }

    @Test
    public void buildTree_TokensPlusBinariesWithAssociativityLeft_ReturnsTree() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.addOperator("d", new BinaryOperator(1, LEFT));
        SyntaxTreeNode<String> tree = analyser.buildTree(createTokens("a", "b", "c", "d", "e"));
        assertEquals("((a b c) d e)", tree.toString());
    }

    @Test
    public void buildTree_TokensPlusBinariesWithAssociativityRight_ReturnsTree() {
        analyser.addOperator("b", new BinaryOperator(1, RIGHT));
        analyser.addOperator("d", new BinaryOperator(1, RIGHT));
        SyntaxTreeNode<String> tree = analyser.buildTree(createTokens("a", "b", "c", "d", "e"));
        assertEquals("(a b (c d e))", tree.toString());
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_TokensPlusBinariesPlusOpenGroup_ThrowsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.addOperator("d", new BinaryOperator(1, LEFT));
        analyser.addGroupCharacters("(", ")");
        analyser.buildTree(createTokens("a", "b", "(", "c", "d", "e"));
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_TokensPlusBinariesPlusCloseGroup_ThrowsSyntacticExpressionException() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.addOperator("d", new BinaryOperator(1, LEFT));
        analyser.addGroupCharacters("(", ")");
        analyser.buildTree(createTokens("a", "b", "c", "d", "e", ")"));
    }

    @Test(expected = SyntacticExpressionException.class)
    public void buildTree_Grouping_ThrowsSyntacticExpressionException() {
        analyser.addGroupCharacters("(", ")");
        analyser.buildTree(createTokens("(", ")"));
    }

    @Test
    public void buildTree_TokensPlusBinariesPlusGrouping_ReturnsTree() {
        analyser.addOperator("b", new BinaryOperator(1, LEFT));
        analyser.addOperator("d", new BinaryOperator(1, LEFT));
        analyser.addGroupCharacters("(", ")");
        SyntaxTreeNode<String> tree = analyser.buildTree(createTokens("a", "b", "(", "c", "d", "e", ")"));
        assertEquals("(a b (c d e))", tree.toString());
    }
}
