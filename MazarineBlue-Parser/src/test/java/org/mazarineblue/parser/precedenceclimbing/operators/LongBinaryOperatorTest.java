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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoLongException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoLongException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LongBinaryOperatorTest {

    protected BinaryOperator operator;
    protected Number left, right, result;

    @Before
    public void setup() {
        operator = OperatorFacade.createBitwiseLeftShift(0, null);
        setup(1L, 1L, 2L);
    }

    protected void setup(Number left, Number right, Number result) {
        this.left = left;
        this.right = right;
        this.result = result;
    }

    protected void assertEvaluationEquals(Object left, Object right, Object result) {
        assertEquals(result, operator.evaluate(left, right, null));
    }

    @Test
    public void testIntegerAndString_ReturnsInteger() {
        assertEvaluationEquals(left.intValue(), right.toString(), result.intValue());
    }

    @Test
    public void testByteAndByte_ReturnsInteger() {
        assertEvaluationEquals(left.byteValue(), right.byteValue(), result.intValue());
    }

    @Test
    public void testShortAndByte_ReturnsInteger() {
        assertEvaluationEquals(left.shortValue(), right.byteValue(), result.intValue());
    }

    @Test
    public void testIntegerAndByte_ReturnsInteger() {
        assertEvaluationEquals(left.intValue(), right.byteValue(), result.intValue());
    }

    @Test
    public void testIntegerAndShort_ReturnsInteger() {
        assertEvaluationEquals(left.intValue(), right.shortValue(), result.intValue());
    }

    @Test
    public void testLongAndString_ReturnsLong() {
        assertEvaluationEquals(left.longValue(), right.toString(), result.longValue());
    }

    @Test
    public void testLongAndByte_ReturnsLong() {
        assertEvaluationEquals(left.longValue(), right.byteValue(), result.longValue());
    }

    @Test
    public void testLongAndInteger_ReturnsLong() {
        assertEvaluationEquals(left.longValue(), right.intValue(), result.longValue());
    }

    @Test
    public void testLongAndShort_ReturnsLong() {
        assertEvaluationEquals(left.longValue(), right.shortValue(), result.longValue());
    }

    @Test(expected = LeftAndRightNoLongException.class)
    public void leftAndRightNoNumbers_ThrowsException() {
        assertEvaluationEquals(1f, 1f, 0);
    }

    @Test(expected = LeftNoLongException.class)
    public void leftNoNumber_ThrowsException() {
        assertEvaluationEquals(1f, 0, 0);
    }

    @Test(expected = RightNoLongException.class)
    public void rightNoNumber_ThrowsException() {
        assertEvaluationEquals(0, 1f, 0);
    }
}
