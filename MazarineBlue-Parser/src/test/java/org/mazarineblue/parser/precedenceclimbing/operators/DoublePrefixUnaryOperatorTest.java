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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.NoDoubleException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DoublePrefixUnaryOperatorTest
        extends LongPrefixUnaryOperatorTest {

    @Before
    @Override
    public void setup() {
        operator = OperatorFacade.createUnaryMinus(0, null);
        setup(4d, -4d);
    }

    private void assertEvaluationEquals(Object obj, Object result) {
        assertEquals(result, operator.evaluate(obj, null));
    }

    @Test
    @Override
    public void testLongString_ReturnsNumber() {
        assertEvaluationEquals(input.toString(), expected.floatValue());
    }

    @Test
    public void testFloat_ReturnsFloat() {
        assertEvaluationEquals(input.floatValue(), expected.floatValue());
    }

    @Test
    public void testDouble_ReturnsDouble() {
        assertEvaluationEquals(input.doubleValue(), expected.doubleValue());
    }

    @Override
    @Test(expected = NoDoubleException.class)
    public void testObject_ThrowsException() {
        assertEvaluationEquals(new Object(), expected.doubleValue());
    }
}
