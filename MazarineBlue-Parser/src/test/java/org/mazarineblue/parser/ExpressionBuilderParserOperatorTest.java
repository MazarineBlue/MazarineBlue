/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.exceptions.SyntacticExpressionException;
import org.mazarineblue.parser.exceptions.VariableNotFoundException;
import org.mazarineblue.parser.exceptions.expression.LeafException;
import org.mazarineblue.parser.exceptions.expression.NoVariableSourceException;
import org.mazarineblue.parser.exceptions.expression.NumberBinaryNodeException;
import org.mazarineblue.parser.exceptions.expression.NumberUnaryNodeException;
import org.mazarineblue.parser.expressions.Expression;

public class ExpressionBuilderParserOperatorTest {

    private VariableSource<Object> source;
    private ExpressionBuilderParser parser;

    @Before
    public void setup() {
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("TRUE", TRUE);
        map.put("FALSE", FALSE);
        source = VariableSource.newInstance(map);
        parser = new ExpressionBuilderParser();
    }

    @After
    public void teardown() {
        parser = null;
    }

    @Test(expected = SyntacticExpressionException.class)
    public void leaf_EmptyQuery() {
        Expression expression = parser.parse("");
        assertEquals("", expression.evaluate(null));
    }

    @Test(expected = LeafException.class)
    public void leaf_String_SingleQuotes() {
        Expression expression = parser.parse("\"");
        assertEquals("", expression.evaluate(null));
    }

    @Test(expected = LeafException.class)
    public void leaf_String_UnclosedQuote() {
        Expression expression = parser.parse("\"ab");
        assertEquals("", expression.evaluate(null));
    }

    @Test(expected = LeafException.class)
    public void leaf_String_UnclosedQuote2() {
        Expression expression = parser.parse("a\"b");
        assertEquals("", expression.evaluate(null));
    }

    @Test(expected = LeafException.class)
    public void leaf_String_UnclosedQuote3() {
        Expression expression = parser.parse("ab\"");
        assertEquals("", expression.evaluate(null));
    }

    @Test(expected = LeafException.class)
    public void leaf_String_InproperQuotesUse() {
        Expression expression = parser.parse("a\"b\"c");
        assertEquals("", expression.evaluate(null));
    }

    @Test
    public void leaf_String_Empty() {
        Expression expression = parser.parse("\"\"");
        assertEquals("", expression.evaluate(null));
    }

    @Test
    public void leaf_String_Abc() {
        Expression expression = parser.parse("\"abc\"");
        assertEquals("abc", expression.evaluate(null));
    }

    @Test
    public void leaf_Long() {
        Expression expression = parser.parse("123");
        assertEquals(123L, expression.evaluate(null));
    }

    @Test
    public void leaf_Double() {
        Expression expression = parser.parse("1.23");
        assertEquals(1.23, (Double) expression.evaluate(null), 0.1d);
    }

    @Test
    public void leaf_Variable_Found() {
        Expression expression = parser.parse("foo");
        assertEquals("bar", expression.evaluate(source));
    }

    @Test(expected = VariableNotFoundException.class)
    public void leaf_Variable_NotFound() {
        Expression expression = parser.parse("bar");
        expression.evaluate(source);
    }

    @Test(expected = LeafException.class)
    public void leaf_Jiberish() {
        Expression expression = parser.parse("@#$");
        expression.evaluate(source);
    }

    @Test
    public void negative_Long() {
        Expression expression = parser.parse("-1");
        assertEquals(-1L, expression.evaluate(null));
    }

    @Test
    public void negative_Double() {
        Expression expression = parser.parse("-1.0");
        assertEquals(-1d, (Double)expression.evaluate(null), 0.1);
    }

    @Test(expected = NumberUnaryNodeException.class)
    public void negative_VariableIsNoNumber() {
        Expression expression = parser.parse("-foo");
        expression.evaluate(source);
    }

    @Test
    public void not_True() {
        Expression expression = parser.parse("!TRUE");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void not_False() {
        Expression expression = parser.parse("!FALSE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void multiply_Long() {
        Expression expression = parser.parse("2 * 2");
        assertEquals(4L, expression.evaluate(null));
    }

    @Test
    public void multiply_Double() {
        Expression expression = parser.parse("2 * 2.0");
        assertEquals(4d, (Double) expression.evaluate(null), 0.1);
    }

    @Test(expected = NoVariableSourceException.class)
    public void multiply_NoVariableSource() {
        Expression expression = parser.parse("2 * TRUE");
        assertEquals(4L, expression.evaluate(null));
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void multiply_Boolean() {
        Expression expression = parser.parse("2 * TRUE");
        assertEquals(4L, expression.evaluate(source));
    }

    @Test
    public void devide_Long() {
        Expression expression = parser.parse("5 / 2");
        assertEquals(2L, expression.evaluate(null));
    }

    @Test
    public void devide_Double() {
        Expression expression = parser.parse("5.0 / 2");
        assertEquals(2.5d, (Double) expression.evaluate(null), 0.1d);
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void devide_Boolean() {
        Expression expression = parser.parse("5.0 / TRUE");
        expression.evaluate(source);
    }

    @Test
    public void remainder_Long() {
        Expression expression = parser.parse("5 % 3");
        assertEquals(2L, expression.evaluate(null));
    }

    @Test
    public void remainder_Double() {
        Expression expression = parser.parse("5.0 % 3");
        assertEquals(2d, (Double) expression.evaluate(null), 0.1d);
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void remainder_Boolean() {
        Expression expression = parser.parse("5.0 % TRUE");
        expression.evaluate(source);
    }

    @Test
    public void addition_Long() {
        Expression expression = parser.parse("5 + 3");
        assertEquals(8L, expression.evaluate(null));
    }

    @Test
    public void addition_Double() {
        Expression expression = parser.parse("5.0 + 3");
        assertEquals(8d, (Double) expression.evaluate(null), 0.1d);
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void addition_Boolean() {
        Expression expression = parser.parse("5.0 + TRUE");
        expression.evaluate(source);
    }

    @Test
    public void subtraction_Long() {
        Expression expression = parser.parse("5 - 3");
        assertEquals(2L, expression.evaluate(null));
    }

    @Test
    public void subtraction_Double() {
        Expression expression = parser.parse("5.0 - 3");
        assertEquals(2d, (Double) expression.evaluate(null), 0.1d);
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void subtraction_Boolean() {
        Expression expression = parser.parse("5.0 - TRUE");
        expression.evaluate(source);
    }

    @Test
    public void greatherThan_Long_True() {
        Expression expression = parser.parse("5 > 4");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void greatherThan_Long_False() {
        Expression expression = parser.parse("5 > 5");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test
    public void greatherThan_Double_True() {
        Expression expression = parser.parse("5.0 > 4.9");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void greatherThan_Double_False() {
        Expression expression = parser.parse("5.0 > 5.0");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void greatherThan_Boolean() {
        Expression expression = parser.parse("5.0 > TRUE");
        expression.evaluate(source);
    }

    @Test
    public void greatherThanOrEqual_Long_True() {
        Expression expression = parser.parse("5 >= 5");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void greatherThanOrEqual_Long_False() {
        Expression expression = parser.parse("5 >= 6");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test
    public void greatherThanOrEqual_Double_True() {
        Expression expression = parser.parse("5.0 >= 5.0");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void greatherThanOrEqual_Double_False() {
        Expression expression = parser.parse("5.0 >= 5.1");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void greatherThanOrEqual_Boolean() {
        Expression expression = parser.parse("5.0 >= TRUE");
        expression.evaluate(source);
    }

    @Test
    public void lessThan_Long_True() {
        Expression expression = parser.parse("5 < 6");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void lessThan_Long_False() {
        Expression expression = parser.parse("5 < 5");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test
    public void lessThan_Double_True() {
        Expression expression = parser.parse("5.0 < 5.1");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void lessThan_Double_False() {
        Expression expression = parser.parse("5.0 < 5.0");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void lessThan_Boolean() {
        Expression expression = parser.parse("5.0 < TRUE");
        expression.evaluate(source);
    }

    @Test
    public void lessThanOrEqual_Long_True() {
        Expression expression = parser.parse("5 <= 5");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void lessThanOrEqual_Long_False() {
        Expression expression = parser.parse("5 <= 4");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test
    public void lessThanOrEqual_Double_True() {
        Expression expression = parser.parse("5.0 <= 5.0");
        assertEquals(TRUE, expression.evaluate(null));
    }

    @Test
    public void lessThanOrEqual_Double_False() {
        Expression expression = parser.parse("5.0 <= 4.9");
        assertEquals(FALSE, expression.evaluate(null));
    }

    @Test(expected = NumberBinaryNodeException.class)
    public void lessThanOrEqual_Boolean() {
        Expression expression = parser.parse("5.0 <= TRUE");
        expression.evaluate(source);
    }

    @Test
    public void equality_Long_True() {
        Expression expression = parser.parse("1 == 1");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void equality_Long_False() {
        Expression expression = parser.parse("1 == 2");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void equality_LongDouble_True() {
        Expression expression = parser.parse("1 == 1.0");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void equality_DoubleLong_True() {
        Expression expression = parser.parse("1.0 == 1");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void equality_Double_False() {
        Expression expression = parser.parse("1 == 1.1");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void equality_Boolean_True() {
        Expression expression = parser.parse("TRUE == TRUE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void equality_Boolean_False() {
        Expression expression = parser.parse("1 == FALSE");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void inequality_Long_True() {
        Expression expression = parser.parse("1 != 2");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void inequality_Long_False() {
        Expression expression = parser.parse("1 != 1");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void inequality_Double_True() {
        Expression expression = parser.parse("1 != 1.0");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void inequality_LongDouble_False() {
        Expression expression = parser.parse("1 != 1.1");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void inequality_DoubleLong_False() {
        Expression expression = parser.parse("1.1 != 1");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void inequality_Boolean_True() {
        Expression expression = parser.parse("1 != FALSE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void inequality_Boolean_False() {
        Expression expression = parser.parse("TRUE != TRUE");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void and_True() {
        Expression expression = parser.parse("TRUE && TRUE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void and_LeftFalse() {
        Expression expression = parser.parse("FALSE && TRUE");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void and_RightFalse() {
        Expression expression = parser.parse("TRUE && FALSE");
        assertEquals(FALSE, expression.evaluate(source));
    }

    @Test
    public void or_LeftTrue() {
        Expression expression = parser.parse("TRUE || FALSE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void or_RightTrue() {
        Expression expression = parser.parse("TRUE || FALSE");
        assertEquals(TRUE, expression.evaluate(source));
    }

    @Test
    public void or_False() {
        Expression expression = parser.parse("FALSE || FALSE");
        assertEquals(FALSE, expression.evaluate(source));
    }
}
