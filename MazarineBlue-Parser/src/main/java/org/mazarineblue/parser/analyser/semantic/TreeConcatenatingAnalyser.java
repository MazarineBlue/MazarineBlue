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
package org.mazarineblue.parser.analyser.semantic;

import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.exceptions.IllegalSyntaxTreeException;
import org.mazarineblue.parser.tokens.Token;
import static org.mazarineblue.parser.tokens.Tokens.isVariable;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import static org.mazarineblue.parser.tree.TreeUtil.isBinary;
import static org.mazarineblue.parser.tree.TreeUtil.isLeaf;
import static org.mazarineblue.parser.tree.TreeUtil.isUnary;

/**
 * A {@code TreeConcatenatingAnalyser} is a {@code SemanticAnalyser} that takes
 * the string value of all tokens within a syntax tree and concatenate them to
 * getter.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TreeConcatenatingAnalyser
        implements SemanticAnalyser<String, Object> {

    private final VariableSource<Object> source;

    /**
     * Creates a {@code TreeConcatenatingAnalyser} without a {@code
     * VariableSource}.
     */
    public TreeConcatenatingAnalyser() {
        source = null;
    }

    /**
     * Creates a {@code TreeConcatenatingAnalyser} with a
     * {@code VariableSource}.
     *
     * @param source the variable source to use when encountering a variable.
     */
    public TreeConcatenatingAnalyser(VariableSource<Object> source) {
        this.source = source;
    }

    @Override
    public Object evaluate(SyntaxTreeNode<String> tree) {
        if (tree == null)
            throw new IllegalSyntaxTreeException(tree);
        if (isLeaf(tree))
            return getValue(tree);
        if (isUnary(tree))
            return getValue(tree).toString() + evaluate(tree.getRightChild());
        if (isBinary(tree))
            return evaluate(tree.getLeftChild()).toString() + getValue(tree) + evaluate(tree.getRightChild());
        throw new IllegalSyntaxTreeException(tree);
    }

    private Object getValue(SyntaxTreeNode<String> tree) {
        Object value = getValue(tree.getToken());
        if (value == null)
            throw new IllegalSyntaxTreeException(tree);
        return value;
    }

    private Object getValue(Token<String> token) {
        if (token == null)
            return null;
        String key = token.getValue();
        return key == null ? null
                : !isVariable(token) || source == null ? key
                : source.getData(key);
    }
}
