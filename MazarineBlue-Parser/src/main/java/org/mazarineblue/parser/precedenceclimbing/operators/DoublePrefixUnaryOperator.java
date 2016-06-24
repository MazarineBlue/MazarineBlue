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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.NoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator.Associativity;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class DoublePrefixUnaryOperator
        extends PrefixUnaryOperator {

    protected DoublePrefixUnaryOperator(int precedence, String symbol, Associativity association) {
        super(precedence, symbol, association);
    }

    @Override
    public Object evaluate(Object obj, VariableSource source) {
        Object result = doEvaluatoinIfPosble(obj);
        if (result != null)
            return result;
        throw new NoDoubleException(obj);
    }

    private Object doEvaluatoinIfPosble(Object obj) {
        if (OperatorsUtil.isInteger(obj))
            return evaluate(OperatorsUtil.toInteger(obj));
        if (OperatorsUtil.isLong(obj))
            return evaluate(OperatorsUtil.toLong(obj));
        if (OperatorsUtil.isFloat(obj))
            return evaluate(OperatorsUtil.toFloat(obj));
        if (OperatorsUtil.isDouble(obj))
            return evaluate(OperatorsUtil.toDouble(obj));
        return null;
    }

    protected abstract Object evaluate(Integer obj);

    protected abstract Object evaluate(Long obj);

    protected abstract Object evaluate(Float obj);

    protected abstract Object evaluate(Double obj);
}
