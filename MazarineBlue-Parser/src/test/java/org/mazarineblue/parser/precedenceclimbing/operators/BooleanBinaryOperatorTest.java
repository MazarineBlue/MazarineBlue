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
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftAndRightNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.LeftNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.exceptions.operators.RightNoBooleanException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BooleanBinaryOperatorTest {

    private BinaryOperator operator;

    @Before
    public void setup() {
        operator = OperatorFacade.createLogicalAnd(0, null);
    }

    private void assertEvaluationEquals(Object left, Object right, Object result) {
        assertEquals(result, operator.evaluate(left, right, null));
    }

    @Test
    public void testsTrueAndTrue_IsTrue() {
        assertEvaluationEquals(true, true, true);
    }
    
    @Test
    public void testsTrueAndStringTrue_IsTrue() {
        assertEvaluationEquals(true, "true", true);
    }
    
    @Test
    public void testsTrueAndCapitalTrue_IsTrue() {
        assertEvaluationEquals(true, "TRUE", true);
    }
    
    @Test
    public void testTrueAndFalse_IsFalse() {
        assertEvaluationEquals(true, false, false);
    }

    @Test
    public void testTrueAndCapitalFalse_IsFalse() {
        assertEvaluationEquals(true, "no", false);
    }

    @Test(expected = LeftAndRightNoBooleanException.class)
    public void leftAndRightNoBooleans_ThrowsLeftAndRightNoBooleanException() {
        assertEvaluationEquals(new Object(), new Object(), true);
    }

    @Test(expected = LeftNoBooleanException.class)
    public void leftNoBoolean_ThrowsLeftNoBooleanException() {
        assertEvaluationEquals(new Object(), true, true);
    }

    @Test(expected = RightNoBooleanException.class)
    public void rightNoBoolean_RightNoBooleanException() {
        assertEvaluationEquals(true, new Object(), true);
    }
}
