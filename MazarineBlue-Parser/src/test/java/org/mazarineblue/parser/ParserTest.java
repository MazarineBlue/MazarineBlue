/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.util.Arrays.asList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import org.mazarineblue.parser.util.TestLexicalAnalyser;
import org.mazarineblue.parser.util.TestSemanticParser;
import org.mazarineblue.parser.util.TestSyntacticAnalyser;
import org.mazarineblue.parser.util.TestSyntaxTree;
import org.mazarineblue.parser.util.TestToken;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ParserTest {

    private TestLexicalAnalyser lexicalAnalyser;
    private TestSyntacticAnalyser<String> syntacticAnalyser;
    private TestSemanticParser semanticParsing;
    private GenericParser<String, String> parser;

    @Before
    public void setup() {
        lexicalAnalyser = new TestLexicalAnalyser();
        syntacticAnalyser = new TestSyntacticAnalyser<>();
        semanticParsing = new TestSemanticParser();
        parser = new GenericParser<>(lexicalAnalyser, syntacticAnalyser, semanticParsing);
    }

    @Test(expected = InvalidExpressionException.class)
    public void parse_Null() {
        parser.parse(null);
    }

    @Test
    public void parse_Input() {
        List<Token<String>> tokens = asList(new TestToken("token"));
        SyntaxTreeNode<String> tree = new TestSyntaxTree();
        lexicalAnalyser.setTokens(tokens);
        syntacticAnalyser.setTree(new TestSyntaxTree());
        semanticParsing.setOutput("output");

        String output = parser.parse("input");
        assertEquals("input", lexicalAnalyser.getInput());
        assertEquals(tokens, syntacticAnalyser.getTokens());
        assertEquals(tree, semanticParsing.getTree());
        assertEquals("output", output);
    }
}
