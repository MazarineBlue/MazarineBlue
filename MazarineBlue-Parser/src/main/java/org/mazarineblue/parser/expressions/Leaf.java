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

import java.util.Objects;
import java.util.regex.Pattern;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.exceptions.expression.LeafException;
import org.mazarineblue.parser.exceptions.expression.NoVariableSourceException;

class Leaf
        implements Expression {

    private static final long serialVersionUID = 1L;

    private static final Pattern NUMBER_LONG = Pattern.compile("^\\d*$");
    private static final Pattern NUMBER_DOUBLE = Pattern.compile("^[\\d.]*$");
    private static final Pattern VARIABLE = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");

    private final String symbol;

    Leaf(String symbol) {
        this.symbol = symbol.trim();
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public Object evaluate(VariableSource<Object> source) {
        if (isString())
            return toString(symbol);
        if (NUMBER_LONG.matcher(symbol).matches())
            return Long.valueOf(symbol);
        if (NUMBER_DOUBLE.matcher(symbol).matches())
            return Double.valueOf(symbol);
        if (VARIABLE.matcher(symbol).matches()) {
            if (source == null)
                throw new NoVariableSourceException();
            return source.getData(symbol);
        }
        throw new LeafException(symbol);
    }

    private String toString(String symbol) {
        return symbol.substring(1, symbol.length() - 1);
    }

    private boolean isString() {
        return symbol.length() >= 2 && symbol.charAt(0) == '"' && symbol.charAt(symbol.length() - 1) == '"';
    }

    @Override
    public int hashCode() {
        return 89 * 3 + Objects.hashCode(this.symbol);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.symbol, ((Leaf) obj).symbol);
    }
}
