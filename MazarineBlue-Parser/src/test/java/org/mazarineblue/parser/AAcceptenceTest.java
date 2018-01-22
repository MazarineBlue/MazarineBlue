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

import static java.lang.Long.parseLong;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity.LEFT;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;

public class AAcceptenceTest {

    @Test
    public void test_StringPrecedenceClimbingParser() {
        StringPrecedenceClimbingParser<MyTreeNode> parser = new StringPrecedenceClimbingParser<>(l -> new MyVariable(l));
        parser.addOperator("+", new BinaryOperator(1, LEFT), (l, r) -> new MyAdd(l, r));
        MyTreeNode top = parser.parse("a + b");

        Map<String, Object> map = new HashMap<>(4);
        map.put("a", 1L);
        map.put("b", "2");
        VariableSource<Object> source = VariableSource.newInstance(map);
        assertEquals(3L, top.evaluate(source));
    }

    @Test
    public void test_StringVariableParser() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("a", 1);
        map.put("b", "2");
        VariableSource<Object> source = VariableSource.newInstance(map);
        Parser<String, Object> parser = new StringVariableParser(source);
        assertEquals("1 + 2", parser.parse("$a + ${b}"));
    }

    private BiFunction<MyTreeNode, MyTreeNode, MyTreeNode> x() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static interface MyTreeNode {

        public Object evaluate(VariableSource<Object> source);
    }

    private abstract static class MyBinary
            implements MyTreeNode {

        private final MyTreeNode left;
        private final MyTreeNode right;

        MyBinary(MyTreeNode left, MyTreeNode right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Object evaluate(VariableSource<Object> source) {
            Object l = left.evaluate(source);
            Object r = right.evaluate(source);
            return evaluate(l, r);
        }

        protected abstract Object evaluate(Object left, Object right);
    }

    private static class MyAdd extends MyBinary {

        MyAdd(MyTreeNode left, MyTreeNode right) {
            super(left, right);
        }

        @Override
        protected Object evaluate(Object left, Object right) {
            return convert(left) + convert(right);
        }
    }

    private static class MyVariable
            implements MyTreeNode {

        private final String symbol;

        MyVariable(String symbol) {
            this.symbol = symbol.trim();
        }

        @Override
        public Object evaluate(VariableSource<Object> source) {
            return source.getData(symbol);
        }
    }

    private static long convert(Object obj)
            throws NumberFormatException {
        if (obj instanceof Integer)
            return (Integer) obj;
        if (obj instanceof Long)
            return (Long) obj;
        if (obj instanceof String)
            return parseLong(((String) obj).trim());
        throw new UnsupportedOperationException();
    }
}
