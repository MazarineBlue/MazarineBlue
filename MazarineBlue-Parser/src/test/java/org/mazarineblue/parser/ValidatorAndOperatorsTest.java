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
package org.mazarineblue.parser;

import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.PrecedenceClimbingParser;
import org.mazarineblue.parser.precedenceclimbing.factories.OperatorSearchingFactory;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator;
import org.mazarineblue.parser.precedenceclimbing.storage.PostfixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.SpecialCharacter;
import org.mazarineblue.parser.precedenceclimbing.storage.VariableValidator;
import org.mazarineblue.parser.util.MappedVariableSource;
import org.mazarineblue.parser.util.NumberValidator;
import org.mazarineblue.parser.util.SpecialTokenSpy;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ValidatorAndOperatorsTest {

    private PrecedenceClimbingParser parser;
    private VariableSource source;

    @Before
    public void setup() {
        parser = new PrecedenceClimbingParser(new OperatorSearchingFactory());
        source = new MappedVariableSource();
    }

    @Test
    public void testSetValidatorTwice() {
        VariableValidator validator = new NumberValidator();
        parser.setValidator(validator);
        parser.setValidator(validator);
    }

    @Test
    public void testAddingSameSpecialTokenTwice() {
        SpecialCharacter token = new SpecialTokenSpy();
        parser.add(token);
        parser.add(token);
    }

    @Test
    public void testAddingSameBinaryOperatorTwice() {
        BinaryOperator operator = new BinaryOperator(0, "+", Operator.Associativity.LEFT) {

            @Override
            public Object evaluate(Object left, Object right, VariableSource source) {
                return "";
            }
        };
        parser.add(operator);
        parser.add(operator);
    }

    @Test
    public void testAddingSamePostfixUnaryOperatorTwice() {
        PostfixUnaryOperator operator = new PostfixUnaryOperator(0, "+", Operator.Associativity.LEFT) {

            @Override
            public Object evaluate(Object operand, VariableSource source) {
                return null;
            }
        };
        parser.add(operator);
        parser.add(operator);
    }

    @Test
    public void testAddingSamePrefixUnaryOperatorTwice() {
        PrefixUnaryOperator operator = new PrefixUnaryOperator(0, "+", Operator.Associativity.LEFT) {

            @Override
            public Object evaluate(Object operand, VariableSource source) {
                return operand;
            }
        };
        parser.add(operator);
        parser.add(operator);
    }
}
