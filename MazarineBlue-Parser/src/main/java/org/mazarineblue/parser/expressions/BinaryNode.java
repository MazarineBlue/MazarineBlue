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

import static java.lang.String.format;
import java.util.Objects;
import org.mazarineblue.parser.VariableSource;

abstract class BinaryNode
        implements Expression {

    private static final long serialVersionUID = 1L;

    private final Expression left;
    private final Expression right;

    BinaryNode(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return format("(%s %s %s)", left, getClass().getSimpleName(), right);
    }

    @Override
    public Object evaluate(VariableSource<Object> source) {
        Object l = left.evaluate(source);
        Object r = right.evaluate(source);
        return evaluate(l, r);
    }

    protected abstract Object evaluate(Object left, Object right);

    @Override
    public int hashCode() {
        return  29 * 29 * 5
                + 29 * Objects.hashCode(this.left)
                + Objects.hashCode(this.right);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj|| obj != null && getClass() == obj.getClass()
                && Objects.equals(this.left, ((BinaryNode) obj).left)
                && Objects.equals(this.right, ((BinaryNode) obj).right);
    }
}
