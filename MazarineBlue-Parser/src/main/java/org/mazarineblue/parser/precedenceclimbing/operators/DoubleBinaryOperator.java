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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class DoubleBinaryOperator
        extends BinaryOperator {

    protected DoubleBinaryOperator(int precedence, String symbol, Associativity association) {
        super(precedence, symbol, association);
    }

    @Override
    public Object evaluate(Object left, Object right, VariableSource source) {
        Object result = doEvaluatoinIfPosble(left, right);
        if (result != null)
            return result;
        throw createException(left, right);
    }

    private Object doEvaluatoinIfPosble(Object left, Object right) {
        if (OperatorsUtil.areBothInteger(left, right))
            return evaluate(OperatorsUtil.toInteger(left), OperatorsUtil.toInteger(right));
        if (OperatorsUtil.areBothLong(left, right))
            return evaluate(OperatorsUtil.toLong(left), OperatorsUtil.toLong(right));
        if (OperatorsUtil.areBothFloat(left, right))
            return evaluate(OperatorsUtil.toFloat(left), OperatorsUtil.toFloat(right));
        if (OperatorsUtil.areBothDouble(left, right))
            return evaluate(OperatorsUtil.toDouble(left), OperatorsUtil.toDouble(right));
        return null;
    }

    private RuntimeException createException(Object left, Object right) {
        if (OperatorsUtil.isDouble(left) == false && OperatorsUtil.isDouble(right) == false)
            return new LeftAndRightNoDoubleException(left, right);
        if (OperatorsUtil.isDouble(left) == false)
            return new LeftNoDoubleException(left, right);
        return new RightNoDoubleException(left, right);
    }

    protected abstract Object evaluate(Integer left, Integer right);

    protected abstract Object evaluate(Long left, Long right);

    protected abstract Object evaluate(Float left, Float right);

    protected abstract Object evaluate(Double left, Double right);
}
