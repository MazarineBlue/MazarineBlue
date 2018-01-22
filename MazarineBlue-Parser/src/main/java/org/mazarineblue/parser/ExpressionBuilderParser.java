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
package org.mazarineblue.parser;

import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.RIGHT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.expressions.Expression;

public class ExpressionBuilderParser
        extends StringPrecedenceClimbingParser<Expression> {

    public ExpressionBuilderParser() {
        super(Expression::leaf);
        addGroupCharacters("(", ")");
        // http://en.cppreference.com/w/cpp/language/operator_precedence
        addOperator("+", new UnaryOperator(3, RIGHT), Expression::plus);
        addOperator("-", new UnaryOperator(3, RIGHT), Expression::minus);
        addOperator("!", new UnaryOperator(3, RIGHT), Expression::not);
        addOperator("*", new BinaryOperator(5, LEFT), Expression::multiplication);
        addOperator("/", new BinaryOperator(5, LEFT), Expression::devision);
        addOperator("%", new BinaryOperator(5, LEFT), Expression::remainder);
        addOperator("+", new BinaryOperator(6, LEFT), Expression::addition);
        addOperator("-", new BinaryOperator(6, LEFT), Expression::subtraction);
        addOperator(">", new BinaryOperator(9, LEFT), Expression::greatherThan);
        addOperator("<", new BinaryOperator(9, LEFT), Expression::lessThan);
        addOperator(">=", new BinaryOperator(9, LEFT), Expression::greatherThanOrEqual);
        addOperator("<=", new BinaryOperator(9, LEFT), Expression::lessThanOrEqual);
        addOperator("==", new BinaryOperator(10, LEFT), Expression::equality);
        addOperator("!=", new BinaryOperator(10, LEFT), Expression::inequality);
        addOperator("&&", new BinaryOperator(14, LEFT), Expression::and);
        addOperator("||", new BinaryOperator(15, LEFT), Expression::or);
    }
}
