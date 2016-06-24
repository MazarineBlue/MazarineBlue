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

import org.mazarineblue.parser.precedenceclimbing.operators.OperatorFacade;
import org.mazarineblue.parser.precedenceclimbing.operators.DoubleVariableSourceBinaryOperator;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoDoubleException;
import org.mazarineblue.parser.util.MappedVariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DoubleVariableSourceBinaryOperatorTest
        extends LongVariableSourceBinaryOperatorTest {

    @Before
    @Override
    public void setup() {
        operator = OperatorFacade.createCompoundAssignmentBySum(0, null);
        source = new MappedVariableSource();
        setup("key", 1, 1, 2);
    }

    @Test
    public void getLongEvaluator_ReturnDoubleEvaluator() {
        DoubleVariableSourceBinaryOperator testOperator = (DoubleVariableSourceBinaryOperator) operator;
        assertEquals(testOperator.getDoubleEvaluator(), testOperator.getLongEvaluator());
    }

    @Test
    public void testFloatAndByte_ReturnsAndStoresAnFloat() {
        source.setData(key, left.floatValue());
        assertEvaluationEquals(key, right.byteValue(), expected.floatValue());
    }

    @Test
    public void testFloatAndShort_ReturnsAndStoresAnFloat() {
        source.setData(key, left.floatValue());
        assertEvaluationEquals(key, right.shortValue(), expected.floatValue());
    }

    @Test
    public void testFloatAndInteger_ReturnsAndStoresAnFloat() {
        source.setData(key, left.floatValue());
        assertEvaluationEquals(key, right.intValue(), expected.floatValue());
    }

    @Test
    public void testFloatAndLong_ReturnsAndStoresAnFloat() {
        source.setData(key, left.floatValue());
        assertEvaluationEquals(key, right.longValue(), expected.floatValue());
    }

    @Test
    public void testFloatAndFloat_ReturnsAndStoresAnFloat() {
        source.setData(key, left.floatValue());
        assertEvaluationEquals(key, right.floatValue(), expected.floatValue());
    }

    @Test
    public void testDoubleAndByte_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.byteValue(), expected.doubleValue());
    }

    @Test
    public void testDoubleAndShort_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.shortValue(), expected.doubleValue());
    }

    @Test
    public void testDoubleAndInteger_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.intValue(), expected.doubleValue());
    }

    @Test
    public void testDoubleAndLong_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.longValue(), expected.doubleValue());
    }

    @Test
    public void testDoubleAndFloat_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.floatValue(), expected.doubleValue());
    }

    @Test
    public void testDoubleAndDouble_ReturnsAndStoresAnDouble() {
        source.setData(key, left.doubleValue());
        assertEvaluationEquals(key, right.floatValue(), expected.doubleValue());
    }

    @Override
    @Test(expected = LeftNoDoubleException.class)
    public void testLeftNull_ThrowsException() {
        source.setData(key, null);
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Override
    @Test(expected = LeftAndRightNoDoubleException.class)
    public void testLeftAndRight_ThrowsException() {
        source.setData(key, new Object());
        assertEvaluationEquals(key, new Object(), expected.intValue());
    }

    @Override
    @Test(expected = LeftNoDoubleException.class)
    public void testLeft_ThrowsException() {
        source.setData(key, new Object());
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Override
    @Test(expected = RightNoDoubleException.class)
    public void testRigth_ThrowsException() {
        source.setData(key, left.intValue());
        assertEvaluationEquals(key, new Object(), expected.intValue());
    }
}
