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
package org.mazarineblue.parser.precedenceclimbing.operators;

import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableNameExpectedInLeftParameterException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class LongVariableSourceBinaryOperator
        extends BinaryOperator {

    protected LongVariableSourceBinaryOperator(int precedence, String symbol, Associativity association) {
        super(precedence, symbol, association);
    }

    @Override
    public Object evaluate(Object left, Object right, VariableSource source) {
        if (left instanceof String == false)
            throw new VariableNameExpectedInLeftParameterException(left, right);

        String variable = (String) left;
        Object l = source.getData(variable);

        EvaluatorHelper helper = getEvaluatorHelper(source, variable);
        Object result = helper.evaluate(l, right);
        if (result != null)
            return result;

        throw createException(l, right);
    }

    protected EvaluatorHelper getEvaluatorHelper(VariableSource source, String variable) {
        return new EvaluatorHelper(source, variable) {

            @Override
            protected Object evaluate(Object left, Object right) {
                LongEvaluator e = getLongEvaluator();
                LongVariableSourceEvaluator evaluator = new LongVariableSourceEvaluator(e, source, variable);
                return evaluator.doEvaluatoinIfPosble(left, right);
            }
        };
    }

    protected abstract LongEvaluator getLongEvaluator();

    protected RuntimeException createException(Object left, Object right) {
        boolean leftIsNoLong = OperatorsUtil.isLong(left) == false;
        boolean rightIsNoLong = OperatorsUtil.isLong(right) == false;
        if (leftIsNoLong && rightIsNoLong)
            return new LeftAndRightNoLongException(left, right);
        if (leftIsNoLong)
            return new LeftNoLongException(left, right);
        return new RightNoLongException(left, right);
    }
}
