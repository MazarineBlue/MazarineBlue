/*
 * Copyright (c) 2011-2013 Alex de Kruijff
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
package org.mazarineblue.parser.precedenceclimbing.storage;

/**
 * A symbol indication what function to perform on which operands.
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public abstract class Operator {

    /**
     * An enumeration of all the associativity properties for an Operator.
     *
     * @see Operator
     */
    public static enum Associativity {

        /**
         * Indicates that this operator has an left-to-right associativity
         * property.
         */
        LEFT,
        /**
         * Indicates that this operator has an right-to-left associativity
         * property.
         */
        RIGHT,
    }

    private final int precedence;
    private final String symbol;
    private final Associativity associative;

    Operator(final int precedence, final String symbol,
             final Associativity associative) {
        this.precedence = precedence;
        this.symbol = symbol;
        this.associative = associative;
    }

    @Override
    public String toString() {
        return symbol;
    }

    /**
     * Returns the precedence of this operator.
     *
     * @return the precedence of this operator.
     */
    public int precedence() {
        return precedence;
    }

    public int associativityPrecedence() {
        int offset = associative.equals(Associativity.LEFT) ? 1 : 0;
        return precedence + offset;
    }

    /**
     * Return the symbol of this operator.
     *
     * @return the symbol of this operator.
     */
    public String symbol() {
        return symbol;
    }
}
