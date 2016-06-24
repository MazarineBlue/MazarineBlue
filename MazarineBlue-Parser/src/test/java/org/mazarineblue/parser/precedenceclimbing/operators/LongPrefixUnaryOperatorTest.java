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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.NoLongException;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LongPrefixUnaryOperatorTest {

    protected PrefixUnaryOperator operator;
    protected Number input, expected;

    @Before
    public void setup() {
        operator = OperatorFacade.createBitwiseNot(0, null);
        setup(0L, -1L);
    }

    protected void setup(Number input, Number expected) {
        this.input = input;
        this.expected = expected;
    }

    private void assertEvaluationEquals(Object obj, Object result) {
        assertEquals(result, operator.evaluate(obj, null));
    }

    @Test
    public void testLongString_ReturnsNumber() {
        assertEvaluationEquals(input.toString(), expected.intValue());
    }

    @Test
    public void testByte_ReturnsInteger() {
        assertEvaluationEquals(input.byteValue(), expected.intValue());
    }

    @Test
    public void testShort_ReturnsInteger() {
        assertEvaluationEquals(input.shortValue(), expected.intValue());
    }

    @Test
    public void testInteger_ReturnsInteger() {
        assertEvaluationEquals(input.intValue(), expected.intValue());
    }

    @Test
    public void testLong_ReturnsLong() {
        assertEvaluationEquals(input.longValue(), expected.longValue());
    }

    @Test(expected = NoLongException.class)
    public void testObject_ThrowsException() {
        assertEvaluationEquals(1f, expected.doubleValue());
    }
}
