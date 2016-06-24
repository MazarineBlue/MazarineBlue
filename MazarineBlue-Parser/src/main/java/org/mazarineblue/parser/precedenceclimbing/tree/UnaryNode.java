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
package org.mazarineblue.parser.precedenceclimbing.tree;

import org.mazarineblue.parser.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.TokenException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.OperatorException;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;

/**
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
class UnaryNode
        extends Tree {

    protected final UnaryOperator operator;
    protected final Tree tree;

    protected UnaryNode(UnaryOperator operator, Tree tree) {
        this.operator = operator;
        this.tree = tree;
    }

    @Override
    public Object evaluate(VariableSource source) {
        try {
            Object result = tree.evaluate(source);
            result = operator.evaluate(result, source);
            return trim(result);
        } catch (OperatorException ex) {
            Leaf leaf = tree.getFirst();
            Token token = leaf.getToken();
            throw new TokenException(token, ex);
        }
    }

    @Override
    Leaf getFirst() {
        return tree.getFirst();
    }
}
