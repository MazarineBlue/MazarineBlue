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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableNameExpectedInLeftParameterException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;
import org.mazarineblue.parser.precedenceclimbing.operators.BooleanBinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.operators.DoubleEvaluator;
import org.mazarineblue.parser.precedenceclimbing.operators.DoubleVariableSourceBinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.operators.LongBinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.operators.LongEvaluator;
import org.mazarineblue.parser.precedenceclimbing.operators.LongVariableSourceBinaryOperator;
import org.mazarineblue.parser.util.MappedVariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestOperatorUtil {

    static abstract class TestPrefixUnaryOperator {

        protected PrefixUnaryOperator operator;
    }

    static abstract class TestBooleanPrefixUnaryOperator
            extends TestPrefixUnaryOperator {

        protected Boolean input, expected;

        protected void setup(Boolean input, Boolean expected) {
            this.input = input;
            this.expected = expected;
        }

        @Test
        public void testBoolean() {
            Object result = operator.evaluate(input, null);
            assertEquals(expected, result);
        }

    }

    static abstract class TestLongPrefixUnaryOperator
            extends TestPrefixUnaryOperator {

        protected Number input, expected;

        protected void setup(Number input, Number expected) {
            this.input = input;
            this.expected = expected;
        }

        @Test
        public void testInteger() {
            assertEquals(expected.intValue(), operator.evaluate(input.intValue(), null));
        }

        @Test
        public void testLong() {
            assertEquals(expected.longValue(), operator.evaluate(input.longValue(), null));
        }
    }

    static abstract class TestDoublePrefixUnaryOperator
            extends TestLongPrefixUnaryOperator {

        @Test
        public void testFloat() {
            assertEquals(expected.floatValue(), operator.evaluate(input.floatValue(), null));
        }

        @Test
        public void testDouble() {
            assertEquals(expected.doubleValue(), operator.evaluate(input.doubleValue(), null));
        }
    }

    static abstract class TestBinaryOperator {

        protected BinaryOperator operator;
    }

    static abstract class TestObjectBinaryOperator
            extends TestBinaryOperator {

        protected Object left, right, expected;

        protected void setup(Object left, Object right, Object expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }

        @Test
        public void testObjects() {
            Object result = operator.evaluate(left, right, null);
            assertEquals(expected, result);
        }
    }

    static abstract class TestBooleanBinaryOperator
            extends TestBinaryOperator {

        protected Boolean left, right, expected;

        protected void setup(Boolean left, Boolean right, Boolean expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }

        @Test
        public void testBoolean() {
            Object result = operator.evaluate(left, right, null);
            assertEquals(expected, result);
        }
    }

    static abstract class TestLongBinaryOperator
            extends TestBinaryOperator {

        protected Number left, right, expected;

        protected void setup(Number left, Number right, Number expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }

        @Test
        public void testInteger() {
            Object result = operator.evaluate(left.intValue(), right.intValue(), null);
            assertEquals(expected.intValue(), result);
        }

        @Test
        public void testLong() {
            Object result = operator.evaluate(left.longValue(), right.longValue(), null);
            assertEquals(expected.longValue(), result);
        }
    }

    static abstract class TestDoubleBinaryOperator
            extends TestLongBinaryOperator {

        @Test
        public void testFloat() {
            Object result = operator.evaluate(left.floatValue(), right.floatValue(), null);
            assertEquals(expected.floatValue(), result);
        }

        @Test
        public void testDouble() {
            Object result = operator.evaluate(left.doubleValue(), right.doubleValue(), null);
            assertEquals(expected.doubleValue(), result);
        }
    }

    static abstract class TestLongBinaryReturnsBooleanOperator
            extends TestBinaryOperator {

        protected Number left, right;
        protected Object expected;

        protected void setup(Number left, Number right, Object expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }

        @Test
        public void testInteger() {
            Object result = operator.evaluate(left.intValue(), right.intValue(), null);
            assertEquals(expected, result);
        }

        @Test
        public void testLong() {
            Object result = operator.evaluate(left.longValue(), right.longValue(), null);
            assertEquals(expected, result);
        }
    }

    static abstract class TestDoubleBinaryReturnsBooleanOperator
            extends TestLongBinaryReturnsBooleanOperator {

        @Test
        public void testFloat() {
            Object result = operator.evaluate(left.floatValue(), right.floatValue(), null);
            assertEquals(expected, result);
        }

        @Test
        public void testDouble() {
            Object result = operator.evaluate(left.doubleValue(), right.doubleValue(), null);
            assertEquals(expected, result);
        }
    }

    static abstract class TestVariableSourceBinaryOperator
            extends TestBinaryOperator {

        protected VariableSource source;
        protected static String KEY = "key";
        
        @Before
        public void setupVariableSourceBinaryOperatorTest() {
            source = new MappedVariableSource();
        }
    }

    static abstract class TestLongVariableSourceBinaryOperator
            extends TestVariableSourceBinaryOperator {

        protected Number left, right, expected;

        protected void setup(Number left, Number right, Number expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }

        @Test
        public void testInteger() {
            source.setData(KEY, left.intValue());
            assertEquals(expected.intValue(), operator.evaluate(KEY, right.intValue(), source));
            assertEquals(expected.intValue(), source.getData(KEY));
        }

        @Test
        public void testLong() {
            source.setData(KEY, left.longValue());
            assertEquals(expected.longValue(), operator.evaluate(KEY, right.longValue(), source));
            assertEquals(expected.longValue(), source.getData(KEY));
        }
    }

    static abstract class TestDoubleVariableSourceBinaryOperator
            extends TestLongVariableSourceBinaryOperator {

        private Number expected2;

        @Override
        protected void setup(Number left, Number right, Number expected) {
            super.setup(left, right, expected);
            this.expected2 = expected;
        }

        protected void setup(Number left, Number right, Number expected, Number expected2) {
            this.setup(left, right, expected);
            this.expected2 = expected2;
        }

        @Test
        public void testFloat() {
            source.setData(KEY, left.floatValue());
            assertEquals(expected2.floatValue(), operator.evaluate(KEY, right.floatValue(), source));
            assertEquals(expected2.floatValue(), source.getData(KEY));
        }

        @Test
        public void testDouble() {
            source.setData(KEY, left.doubleValue());
            assertEquals(expected2.doubleValue(), operator.evaluate(KEY, right.doubleValue(), source));
            assertEquals(expected2.doubleValue(), source.getData(KEY));
        }
    }
}
