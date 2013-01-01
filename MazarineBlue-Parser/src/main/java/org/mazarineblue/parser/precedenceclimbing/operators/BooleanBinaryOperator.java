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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class BooleanBinaryOperator
        extends BinaryOperator {

    protected BooleanBinaryOperator(int precedence, String symbol, Associativity association) {
        super(precedence, symbol, association);
    }

    @Override
    public Object evaluate(Object left, Object right, VariableSource source) {
        if (OperatorsUtil.areBothBoolean(left, right))
            return evaluate(OperatorsUtil.toBoolean(left), OperatorsUtil.toBoolean(right));
        throw createException(left, right);
    }

    private RuntimeException createException(Object left, Object right) {
        if (OperatorsUtil.isBoolean(left) == false && OperatorsUtil.isBoolean(right) == false)
            return new LeftAndRightNoBooleanException(left, right);
        if (OperatorsUtil.isBoolean(left) == false)
            return new LeftNoBooleanException(left, right);
        return new RightNoBooleanException(left, right);
    }

    protected abstract Object evaluate(Boolean left, Boolean right);
}
