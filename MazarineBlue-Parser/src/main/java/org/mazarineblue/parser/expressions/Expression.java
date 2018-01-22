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

import java.io.Serializable;
import org.mazarineblue.parser.VariableSource;

public interface Expression
        extends Serializable {

    public static Expression leaf(String symbol) {
        return new Leaf(symbol);
    }

    public static Expression plus(Expression value) {
        return value;
    }

    public static Expression minus(Expression value) {
        return new Minus(value);
    }

    public static Expression not(Expression value) {
        return new Not(value);
    }

    public static Expression multiplication(Expression left, Expression right) {
        return new Multiply(left, right);
    }

    public static Expression devision(Expression left, Expression right) {
        return new Devide(left, right);
    }

    public static Expression remainder(Expression left, Expression right) {
        return new Remainder(left, right);
    }

    public static Expression addition(Expression left, Expression right) {
        return new Add(left, right);
    }

    public static Expression greatherThan(Expression left, Expression right) {
        return new GreatherThan(left, right);
    }

    public static Expression greatherThanOrEqual(Expression left, Expression right) {
        return new Not(new LessThan(left, right));
    }

    public static Expression lessThan(Expression left, Expression right) {
        return new LessThan(left, right);
    }

    public static Expression lessThanOrEqual(Expression left, Expression right) {
        return new Not(new GreatherThan(left, right));
    }

    public static Expression subtraction(Expression left, Expression right) {
        return new Subtract(left, right);
    }

    public static Expression equality(Expression left, Expression right) {
        return new Equals(left, right);
    }

    public static Expression inequality(Expression left, Expression right) {
        return new Not(new Equals(left, right));
    }

    public static Expression and(Expression left, Expression right) {
        return new And(left, right);
    }

    public static Expression or(Expression left, Expression right) {
        return new Or(left, right);
    }

    public Object evaluate(VariableSource<Object> source);
}
