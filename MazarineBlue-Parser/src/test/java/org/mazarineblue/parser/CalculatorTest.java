/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.lang.Long.parseLong;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.rules.ExpectedException.none;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;
import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.exceptions.SemanticExpressionException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CalculatorTest {

    @Rule
    @SuppressWarnings("PublicField")
    public ExpectedException exception = none();
    private StringPrecedenceClimbingParser<Object> parser;

    @Before
    public void setup() {
        parser = new StringPrecedenceClimbingParser<>(t -> t);
    }

    @After
    public void teardown() {
        parser = null;
    }

    private static long convert(Object obj)
            throws NumberFormatException {
        if (obj instanceof Long)
            return (Long) obj;
        if (obj instanceof String)
            return parseLong(((String) obj).trim());
        throw new UnsupportedOperationException();
    }

    @Test
    public void parse_Initial_ExpressionIsTakenLiteral() {
        assertEquals("4 * 4 + 4", parser.parse("4 * 4 + 4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addGroupCharacters_NullOpenChar_ThrowsIllegalArgumentException() {
        parser.addGroupCharacters(null, ")");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addGroupCharacters_NullCloseChar_ThrowsIllegalArgumentException() {
        parser.addGroupCharacters("(", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUnaryOperator_NullKey_ThrowsIllegalArgumentException() {
        parser.addOperator(null, new UnaryOperator(1, LEFT), t -> -convert(t));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUnaryOperator_NullOperator_ThrowsIllegalArgumentException() {
        parser.addOperator("+", null, t -> -convert(t));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUnaryOperator_NullFunction_ThrowsIllegalArgumentException() {
        parser.addOperator("+", new UnaryOperator(1, LEFT), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBinaryOperator_NullKey_ThrowsIllegalArgumentException() {
        parser.addOperator(null, new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBinaryOperator_NullOperator_ThrowsIllegalArgumentException() {
        parser.addOperator("+", null, (t, u) -> convert(t) + convert(u));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBinaryOperator_NullFunction_ThrowsIllegalArgumentException() {
        parser.addOperator("+", new BinaryOperator(1, LEFT), null);
    }

    @Test
    public void parse_Alphanumeric_ThrowsSemanticExpressionException() {
        exception.expect(SemanticExpressionException.class);
        exception.expectMessage(String.format(InvalidExpressionException.FORMAT_CAUSE, 2, "For input string: \"4a\""));
        parser.addOperator("+", new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
        assertEquals(12, (long) parser.parse("4 + 4a"));
    }

    @Test
    public void parse_PlusOparetor() {
        parser.addOperator("+", new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
        assertEquals(12, (long) parser.parse("4 + 4 + 4"));
    }

    @Test
    public void parse_PlusMultiplyOperatorWithPrecedenceDifference() {
        parser.addOperator("+", new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
        parser.addOperator("*", new BinaryOperator(2, LEFT), (t, u) -> convert(t) * convert(u));
        assertEquals(32, (long) parser.parse("4 * 4 + 4 * 4"));
    }

    @Test
    public void parse_PlusMultiplyBinaryMinusUnaryOperatorWithPrecedenceDifference() {
        parser.addOperator("+", new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
        parser.addOperator("*", new BinaryOperator(2, LEFT), (t, u) -> convert(t) * convert(u));
        parser.addOperator("-", new UnaryOperator(1, LEFT), t -> -convert(t));
        assertEquals(0, (long) parser.parse("4 * 4 + 4 * -4"));
    }

    @Test
    public void parse_PlusMultiplyOperatorWithExplicitGroupings() {
        parser.addOperator("+", new BinaryOperator(1, LEFT), (t, u) -> convert(t) + convert(u));
        parser.addOperator("*", new BinaryOperator(2, LEFT), (t, u) -> convert(t) * convert(u));
        parser.addGroupCharacters("(", ")");
        assertEquals(128, (long) parser.parse("4 * (4 + 4) * 4"));
    }
}
