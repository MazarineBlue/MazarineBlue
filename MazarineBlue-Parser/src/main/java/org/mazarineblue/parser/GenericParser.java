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

import java.util.List;
import org.mazarineblue.parser.analyser.lexical.LexicalAnalyser;
import org.mazarineblue.parser.analyser.semantic.SemanticAnalyser;
import org.mazarineblue.parser.analyser.syntax.SyntacticAnalyser;
import org.mazarineblue.parser.exceptions.BracketUnclosedException;
import org.mazarineblue.parser.exceptions.IllegalCloseBacketException;
import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.exceptions.LexicalExpressionException;
import org.mazarineblue.parser.exceptions.SemanticExpressionException;
import org.mazarineblue.parser.exceptions.VariableSignMissingException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tree.SyntaxTreeNode;

/**
 * A {@code GenericParser} converts an expression in to data structure.
 * <p>
 * Parsing happens in the lexical, syntactic and semantic stages. During the
 * lexical stage the input is broken down in to tokens. Then in the
 * syntactic stage these tokens are validated and converted into a syntax
 * tree. And finally in the semantic stage the implication of the expression
 * are determined.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 * @param <R> the parser output type.
 */
public class GenericParser<T, R>
        implements Parser<T, R> {

    private final LexicalAnalyser<T> lexicalAnalyser;
    private final SyntacticAnalyser<T> syntacticAnalyser;
    private final SemanticAnalyser<T, R> semanticAnalyser;

    /**
     * Constructs a parser capable of converting specified data into a syntax
     * tree.
     *
     * @param lexicalAnalyser   the convertor of data into tokens.
     * @param syntacticAnalyser the convertor of tokens into a syntax tree.
     * @param semanticAnalyser  the convertor of syntax tree into a result.
     */
    public GenericParser(LexicalAnalyser<T> lexicalAnalyser, SyntacticAnalyser<T> syntacticAnalyser,
                         SemanticAnalyser<T, R> semanticAnalyser) {
        this.lexicalAnalyser = lexicalAnalyser;
        this.syntacticAnalyser = syntacticAnalyser;
        this.semanticAnalyser = semanticAnalyser;
    }

    @Override
    public R parse(T input) {
        if (input == null)
            throw new InvalidExpressionException(0);
        List<Token<T>> tokens = parseLexically(input);
        SyntaxTreeNode<T> tree = parseSyntactically(tokens);
        return parseSemantically(tree);
    }

    private List<Token<T>> parseLexically(T input) {
        try {
            return lexicalAnalyser.breakdown(input);
        } catch (VariableSignMissingException | IllegalCloseBacketException | BracketUnclosedException ex) {
            throw new LexicalExpressionException(ex.getIndex(), ex);
        }
    }

    private SyntaxTreeNode<T> parseSyntactically(List<Token<T>> tokens) {
        return syntacticAnalyser.buildTree(tokens);
    }

    private R parseSemantically(SyntaxTreeNode<T> tree) {
        try {
            return semanticAnalyser.evaluate(tree);
        } catch (RuntimeException ex) {
            throw new SemanticExpressionException(tree.getToken().getIndex(), ex);
        }
    }
}
