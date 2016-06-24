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
package org.mazarineblue.parser.precedenceclimbing.operators.concrete;

import org.mazarineblue.parser.precedenceclimbing.operators.OperatorFacade;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableNameExpectedInLeftParameterException;
import org.mazarineblue.parser.util.MappedVariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DirectAssignmentTest
        extends TestOperatorUtil.TestVariableSourceBinaryOperator {

    private VariableSource source;
    private Object left, right, expected;

    @Before
    public void setup() {
        operator = OperatorFacade.createDirectAssignment(0, null);
        source = new MappedVariableSource();
    }

    @Test
    public void testObject() {
        source.setData(KEY, left);
        assertEquals(expected, operator.evaluate(KEY, right, source));
        assertEquals(expected, source.getData(KEY));
    }

    @Test(expected = VariableNameExpectedInLeftParameterException.class)
    public void testNonVariableLeftArgument_ThrowsVariableNameExpectedInLeftParameterException() {
        operator.evaluate(new Object(), right, source);
    }
}
