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

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class LongVariableSourceEvaluator {

    private final LongEvaluator evaluator;
    protected final VariableSource source;
    protected final String key;

    LongVariableSourceEvaluator(LongEvaluator evaluator, VariableSource source, String key) {
        this.evaluator = evaluator;
        this.source = source;
        this.key = key;
    }

    Object doEvaluatoinIfPosble(Object left, Object right) {
        if (OperatorsUtil.areBothInteger(left, right))
            return doEvaluationOfInteger(left, right);
        if (OperatorsUtil.areBothLong(left, right))
            return doEvaluationOfLong(left, right);
        return null;
    }

    protected Object doEvaluationOfInteger(Object left, Object right) {
        Integer l = OperatorsUtil.toInteger(left);
        Integer r = OperatorsUtil.toInteger(right);
        Object result = evaluator.evaluate(l, r);
        source.setData(key, result);
        return result;
    }

    protected Object doEvaluationOfLong(Object left, Object right) {
        Long l = OperatorsUtil.toLong(left);
        Long r = OperatorsUtil.toLong(right);
        Object result = evaluator.evaluate(l, r);
        source.setData(key, result);
        return result;
    }
}
