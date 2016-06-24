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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableNameExpectedInLeftParameterException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.util.MappedVariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LongVariableSourceBinaryOperatorTest {

    protected BinaryOperator operator;
    protected String key;
    protected Number left, right, expected;
    protected VariableSource source;

    @Before
    public void setup() {
        operator = OperatorFacade.createCompoundAssignmentByBitwiseLeftShift(0, null);
        source = new MappedVariableSource();
        setup("key", 1, 1, 2);
    }

    protected void setup(String key, Number left, Number right, Number expected) {
        this.key = key;
        this.left = left;
        this.right = right;
        this.expected = expected;
    }

    protected void assertEvaluationEquals(Object left, Object right, Object expected) {
        assertEquals(expected, operator.evaluate(left, right, source));
        assertEquals(expected, source.getData((String) left));
    }

    @Test
    public void testByteAndByte_ReturnsAndStoresAnInteger() {
        source.setData(key, left.byteValue());
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Test
    public void testShortAndByte_ReturnsAndStoresAnInteger() {
        source.setData(key, left.shortValue());
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Test
    public void testShortAndShort_ReturnsAndStoresAnInteger() {
        source.setData(key, left.shortValue());
        assertEvaluationEquals(key, right.shortValue(), expected.intValue());
    }

    @Test
    public void testIntegerAndByte_ReturnsAndStoresAnInteger() {
        source.setData(key, left.intValue());
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Test
    public void testIntegerAndShort_ReturnsAndStoresAnInteger() {
        source.setData(key, left.intValue());
        assertEvaluationEquals(key, right.shortValue(), expected.intValue());
    }

    @Test
    public void testIntegerAndInteger_ReturnsAndStoresAnInteger() {
        source.setData(key, left.intValue());
        assertEvaluationEquals(key, right.intValue(), expected.intValue());
    }

    @Test
    public void testLongAndByte_ReturnsAndStoresAnLong() {
        source.setData(key, left.longValue());
        assertEvaluationEquals(key, right.byteValue(), expected.longValue());
    }

    @Test
    public void testLongAndShort_ReturnsAndStoresAnLong() {
        source.setData(key, left.longValue());
        assertEvaluationEquals(key, right.shortValue(), expected.longValue());
    }

    @Test
    public void testLongAndInteger_ReturnsAndStoresAnLong() {
        source.setData(key, left.longValue());
        assertEvaluationEquals(key, right.intValue(), expected.longValue());
    }

    @Test
    public void testLongAndLong_ReturnsAndStoresAnLong() {
        source.setData(key, left.longValue());
        assertEvaluationEquals(key, right.longValue(), expected.longValue());
    }

    @Test(expected = VariableNameExpectedInLeftParameterException.class)
    public void testLeftNonString_ThrowsVariableNameExpectedInLeftParameterException() {
        assertEvaluationEquals(new Object(), right.byteValue(), expected.intValue());
    }

    @Test(expected = LeftNoLongException.class)
    public void testLeftNull_ThrowsException() {
        source.setData(key, null);
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Test(expected = LeftAndRightNoLongException.class)
    public void testLeftAndRight_ThrowsException() {
        source.setData(key, 1f);
        assertEvaluationEquals(key, 1f, expected.intValue());
    }

    @Test(expected = LeftNoLongException.class)
    public void testLeft_ThrowsException() {
        source.setData(key, 1f);
        assertEvaluationEquals(key, right.byteValue(), expected.intValue());
    }

    @Test(expected = RightNoLongException.class)
    public void testRigth_ThrowsException() {
        source.setData(key, left.intValue());
        assertEvaluationEquals(key, 1f, expected.intValue());
    }
}
