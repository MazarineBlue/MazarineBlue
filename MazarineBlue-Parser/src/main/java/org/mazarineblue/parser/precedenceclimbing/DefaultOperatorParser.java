/*
 * Copyright (c) 2011-2015 Alex de Kruijff
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
package org.mazarineblue.parser.precedenceclimbing;

import org.mazarineblue.parser.precedenceclimbing.characters.CloseBracketCharacter;
import org.mazarineblue.parser.precedenceclimbing.characters.QuotedSpecialCharacter;
import org.mazarineblue.parser.precedenceclimbing.characters.OpenBracketCharacter;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.PrecedenceClimbingParser;
import org.mazarineblue.parser.precedenceclimbing.factories.OperatorSearchingFactory;
import org.mazarineblue.parser.precedenceclimbing.operators.OperatorFacade;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DefaultOperatorParser
        extends PrecedenceClimbingParser {

    public DefaultOperatorParser() {
        super(new OperatorSearchingFactory());

        // Ordered from high to low precedence
        addUnaryOperators();
        addMathOperators();
        addBitwiseShiftOperators();
        addComparingOperators();
        addBitwiseAndXorOrOperators();
        addLogicalAndOrOpertors();
        addAssignmentOperators();

        // Special characters
        add(new OpenBracketCharacter());
        add(new CloseBracketCharacter());
        add(new QuotedSpecialCharacter());
    }
    
    private void addUnaryOperators() {
        add(OperatorFacade.createUnaryPlus(12, "+"));
        add(OperatorFacade.createUnaryMinus(12, "-"));
        add(OperatorFacade.createLogicalNot(12, "!"));
        add(OperatorFacade.createBitwiseNot(12, "~"));
    }

    private void addMathOperators() {
        add(OperatorFacade.createMultiplication(11, "*"));
        add(OperatorFacade.createDivision(11, "/"));
        add(OperatorFacade.createModulo(11, "%"));
        add(OperatorFacade.createAddition(10, "+"));
        add(OperatorFacade.createSubtraction(10, "-"));
    }

    private void addBitwiseShiftOperators() {
        add(OperatorFacade.createBitwiseLeftShift(9, "<<"));
        add(OperatorFacade.createBitwiseRightShift(9, ">>"));
    }

    private void addComparingOperators() {
        add(OperatorFacade.createLessThen(8, "<"));
        add(OperatorFacade.createLessThenOrEqualTo(8, "<="));
        add(OperatorFacade.createGreaterThen(8, ">"));
        add(OperatorFacade.createGreaterThenOrEqualTo(8, ">="));
        add(OperatorFacade.createEqualTo(7, "=="));
        add(OperatorFacade.createNotEqualTo(7, "!="));
    }

    private void addBitwiseAndXorOrOperators() {
        add(OperatorFacade.createBitwiseAnd(6, "&"));
        add(OperatorFacade.createBitwiseXor(5, "^"));
        add(OperatorFacade.createBitwiseOr(4, "|"));
    }

    private void addLogicalAndOrOpertors() {
        add(OperatorFacade.createLogicalAnd(3, "&&"));
        add(OperatorFacade.createLogicalOr(2, "||"));
    }

    private void addAssignmentOperators() {
        add(OperatorFacade.createDirectAssignment(1, "="));
        add(OperatorFacade.createCompoundAssignmentBySum(1, "+="));
        add(OperatorFacade.createCompoundAssignmentByDifference(1, "-="));
        add(OperatorFacade.createCompoundAssignmentByProduct(1, "*="));
        add(OperatorFacade.createCompoundAssignmentByQuotient(1, "/="));
        add(OperatorFacade.createCompoundAssignmentByRemainder(1, "%="));
        add(OperatorFacade.createCompoundAssignmentByBitwiseLeftShift(1, "<<="));
        add(OperatorFacade.createCompoundAssignmentByBitwiseRightShift(1, ">>="));
        add(OperatorFacade.createCompoundAssignmentByBitwiseAnd(1, "&="));
        add(OperatorFacade.createCompoundAssignmentByBitwiseXor(1, "^="));
        add(OperatorFacade.createCompoundAssignmentByBitwiseOr(1, "|="));
    }

    @Override
    public Object parse(String expression, VariableSource source) {
        return super.parse(expression, source);
    }
}
