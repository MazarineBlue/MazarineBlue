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

import org.mazarineblue.utilities.TypeConvertor;

abstract class EvaluatorHelper {

    private static final long serialVersionUID = 1L;

    Object evaluate(Object left, Object right) {
        if (canCastToLong(left) && canCastToLong(right))
            return evaluateAsLongs(left, right);
        else if (canCastToDouble(left) && canCastToDouble(right))
            return evaluateAsDoubles(left, right);
        else
            return evaluateAsObjects(left, right);
    }

    private static boolean canCastToDouble(Object input) {
        return canCastToLong(input) || input instanceof Float || input instanceof Double;
    }

    private static boolean canCastToLong(Object input) {
        return input instanceof Byte || input instanceof Short || input instanceof Integer || input instanceof Long;
    }

    private Object evaluateAsLongs(Object left, Object right) {
        Long l = TypeConvertor.getSingletonInstance().convert(left, Long.TYPE);
        Long r = TypeConvertor.getSingletonInstance().convert(right, Long.TYPE);
        return doEvaluate(l, r);
    }

    private Object evaluateAsDoubles(Object left, Object right) {
        Double l = TypeConvertor.getSingletonInstance().convert(left, Double.TYPE);
        Double r = TypeConvertor.getSingletonInstance().convert(right, Double.TYPE);
        return doEvaluate(l, r);
    }

    protected abstract Object evaluateAsObjects(Object left, Object right);

    protected abstract Object doEvaluate(Long left, Long right);

    protected abstract Object doEvaluate(Double left, Double right);
}
