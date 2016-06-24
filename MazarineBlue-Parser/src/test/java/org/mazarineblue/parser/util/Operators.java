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
package org.mazarineblue.parser.util;

import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator.Associativity;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.util.operators.AddBinaryOperator;
import org.mazarineblue.parser.util.operators.LogicalIsOperator;
import org.mazarineblue.parser.util.operators.MultipyBinaryOperator;
import org.mazarineblue.parser.util.operators.NegativeOperator;
import org.mazarineblue.parser.util.operators.UpperCaseOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Operators {

    public final PrefixUnaryOperator negative;
    public final PostfixUnaryOperator upperCase;
    public final BinaryOperator add;
    public final BinaryOperator singleIs;
    public final BinaryOperator doubleIs;
    public final BinaryOperator multiply;

    public Operators() {
        negative = new NegativeOperator(12, "-", Associativity.LEFT);
        upperCase = new UpperCaseOperator(12, "~", Associativity.LEFT);
        add = new AddBinaryOperator(1, "+", Associativity.LEFT);
        singleIs = new LogicalIsOperator(7, "=", Associativity.LEFT);
        doubleIs = new LogicalIsOperator(7, "==", Associativity.LEFT);
        multiply = new MultipyBinaryOperator(11, "*", Associativity.LEFT);
    }
}
