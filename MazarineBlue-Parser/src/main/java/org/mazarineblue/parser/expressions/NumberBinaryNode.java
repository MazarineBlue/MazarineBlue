/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.parser.expressions;

import org.mazarineblue.parser.exceptions.expression.NumberBinaryNodeException;

abstract class NumberBinaryNode
        extends BinaryNode {

    private static final long serialVersionUID = 1L;

    protected NumberBinaryNode(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected Object evaluate(Object left, Object right) {
        return new MyEvaluatorHelper().evaluate(left, right);
    }

    private class MyEvaluatorHelper
            extends EvaluatorHelper {

        @Override
        protected Object doEvaluate(Long left, Long right) {
            return thisEvaluate(left, right);
        }

        @Override
        protected Object doEvaluate(Double left, Double right) {
            return thisEvaluate(left, right);
        }

        @Override
        protected Object evaluateAsObjects(Object left, Object right) {
            throw new NumberBinaryNodeException(left, right);
        }
    }

    protected Object thisEvaluate(Long left, Long right) {
        return evaluate(left, right);
    }

    protected abstract Object evaluate(Long left, Long right);

    protected Object thisEvaluate(Double left, Double right) {
        return evaluate(left, right);
    }

    protected abstract Object evaluate(Double left, Double right);
}
