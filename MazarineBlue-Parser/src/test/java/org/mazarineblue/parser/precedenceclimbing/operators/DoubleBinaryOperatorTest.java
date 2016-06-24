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
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoDoubleException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoDoubleException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DoubleBinaryOperatorTest
        extends LongBinaryOperatorTest {

    @Before
    @Override
    public void setup() {
        operator = OperatorFacade.createAddition(0, null);
        setup(3d, 1, 4d);
    }

    @Test
    public void testFloatAndString_ReturnsFloat() {
        assertEvaluationEquals(left.floatValue(), right.toString(), result.floatValue());
    }

    @Test
    public void testFloatAndByte_ReturnsFloat() {
        assertEvaluationEquals(left.floatValue(), right.byteValue(), result.floatValue());
    }

    @Test
    public void testFloatAndInteger_ReturnsFloat() {
        assertEvaluationEquals(left.floatValue(), right.intValue(), result.floatValue());
    }

    @Test
    public void testFloatAndLong_ReturnsFloat() {
        assertEvaluationEquals(left.floatValue(), right.longValue(), result.floatValue());
    }

    @Test
    public void testDoubleAndString_ReturnDouble() {
        assertEvaluationEquals(left.doubleValue(), right.toString(), result.doubleValue());
    }

    @Test
    public void testDoubleAndByte_ReturnDouble() {
        assertEvaluationEquals(left.doubleValue(), right.byteValue(), result.doubleValue());
    }

    @Test
    public void testDoubleAndInteger_ReturnDouble() {
        assertEvaluationEquals(left.doubleValue(), right.intValue(), result.doubleValue());
    }

    @Test
    public void testDoubleAndLong_ReturnDouble() {
        assertEvaluationEquals(left.doubleValue(), right.longValue(), result.doubleValue());
    }

    @Test
    public void testDoubleAndFloat_ReturnDouble() {
        assertEvaluationEquals(left.doubleValue(), right.floatValue(), result.doubleValue());
    }

    @Override
    @Test(expected = LeftAndRightNoDoubleException.class)
    public void leftAndRightNoNumbers_ThrowsException() {
        assertEvaluationEquals("3a", "1a", result.doubleValue());
    }

    @Override
    @Test(expected = LeftNoDoubleException.class)
    public void leftNoNumber_ThrowsException() {
        assertEvaluationEquals("3a", 1d, result.doubleValue());
    }

    @Override
    @Test(expected = RightNoDoubleException.class)
    public void rightNoNumber_ThrowsException() {
        assertEvaluationEquals(3d, "1a", result.doubleValue());
    }
}
