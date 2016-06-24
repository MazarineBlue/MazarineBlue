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
package org.mazarineblue.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.hamcrest.CustomMatcher;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.exceptions.InvalidVariableSourceException;
import org.mazarineblue.parser.util.MappedVariableSource;
import org.mazarineblue.parser.simple.CharacterContainer;
import org.mazarineblue.parser.simple.CharacterProcessor;
import org.mazarineblue.parser.simple.SimpleParser;
import org.mazarineblue.parser.simple.DataMediator;
import org.mazarineblue.parser.simple.exceptions.IllegalVariableException;
import org.mazarineblue.parser.simple.exceptions.IndexException;
import org.mazarineblue.parser.simple.exceptions.UnconsumedCharacterContainerException;
import org.mazarineblue.parser.variable.VariableParser;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SimpleParserTest {

    public void assertCharacterProcessorCount(VariableParser parser, int expected) {
        if (expected == parser.countCharacterProcessors())
            assertEquals(expected, parser.countCharacterProcessors());
        else
            assertEquals(expected, parser.countCharacterProcessors());
    }

    public class GivenEmptyParser {

        private SimpleParser parser;
        private VariableSource source;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() {
            parser = new SimpleParser();
            source = new MappedVariableSource();
        }

        @Test
        public void testEmptyParser() {
            thrown.expect(new ExpectIndexException(0, UnconsumedCharacterContainerException.class));
            parser.parse("bla", source);
        }

        @Test
        public void testConsumeOneCharacter() {
            thrown.expect(new ExpectIndexException(1, UnconsumedCharacterContainerException.class));
            parser.addCharacterProcessor(new SelfRemovingFakeProcessor());
            parser.parse("bla", source);
        }

        private class SelfRemovingFakeProcessor
                implements CharacterProcessor {

            @Override
            public boolean canProcess(CharacterContainer e) {
                return true;
            }

            @Override
            public void process(CharacterContainer e, DataMediator m) {
                m.getProcessorStack().remove(this);
                e.consume();
            }

            @Override
            public void finish(CharacterContainer e, DataMediator m) {
            }
        }
    }

    public class VariableParserTest {

        private VariableParser parser;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() {
            parser = new VariableParser();
        }

        @Test(expected = InvalidVariableSourceException.class)
        public void testNullSource() {
            parser.parse("bla", null);
            assertCharacterProcessorCount(parser, 3);
        }

        public class GivenVariableSource {

            private VariableSource source;

            @Before
            public void setup() {
                source = new MappedVariableSource();
            }

            private void assertExpression(String expected, String expression) {
                assertEquals(expected, parser.parse(expression, source));
            }

            @Test(expected = InvalidExpressionException.class)
            public void testNullInput() {
                parser.parse(null, source);
            }

            @Test
            public void testEmptyInput() {
                assertExpression("", "");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testNonVariableInput() {
                assertExpression("bla", "bla");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testVariableSign() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                parser.parse("$", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testVariableSignWithSpacesInFront() {
                thrown.expect(new ExpectIndexException(3, IllegalVariableException.class));
                parser.parse("  $", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testVariableSignEndingInSpace() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                parser.parse("$  ", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testNonExistingVariable() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                parser.parse("$foo", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testNonExistingVariableWithSpacesInFront() {
                thrown.expect(new ExpectIndexException(3, IllegalVariableException.class));
                parser.parse("  $foo", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testNonExistingVariableEndingInSpaces() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                parser.parse("$foo  ", source);
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testBrokenEmptyComplexVariable() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                assertExpression("content", "${");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testBrokenComplexVariable() {
                thrown.expect(new ExpectIndexException(1, IllegalVariableException.class));
                assertExpression("content", "${foo");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testBrokenLiteralSign() {
                assertExpression("", "\\");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testLiteralOnPlainCharacter() {
                assertExpression("a", "\\a");
                assertCharacterProcessorCount(parser, 3);
            }

            @Test
            public void testLiteralOnVariableSign() {
                assertExpression("$", "\\$");
                assertCharacterProcessorCount(parser, 3);
            }

            public class GivenVaribleSourceInitialised {

                @Before
                public void setup() {
                    source.setData("var", "content");
                    source.setData("v{a}r", "{content}");
                    source.setData("var var", "con-tent");
                }

                @Test
                public void testSimpleVariable() {
                    assertExpression("content", "$var");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testSimpleVariableEndingInSpaces() {
                    assertExpression("content  ", "$var  ");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testSimpleVariableWithCurlyBraces() {
                    assertExpression("{content}", "$v{a}r");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testComplexVariable() {
                    assertExpression("content", "${var}");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testComplexVariableWithLiteralSign() {
                    assertExpression("$content", "\\$${var}");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testComplexVariableUsingSpaces() {
                    assertExpression("con-tent", "${var var}");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testDoubleVariableUsingSpaces() {
                    assertExpression("content content", "$var $var");
                    assertCharacterProcessorCount(parser, 3);
                }

                @Test
                public void testBrokenDoubleVariableUsingSpaces() {
                    assertExpression("content {var}", "$var {var}");
                    assertCharacterProcessorCount(parser, 3);
                }
            }
        }
    }

    private static class ExpectIndexException
            extends CustomMatcher<IndexException> {

        private final int index;
        private final Class<IndexException> type;

        private ExpectIndexException(int index, Class type) {
            super(String.format("%s: %s", getClassName(type), getMessage(index)));
            this.index = index;
            this.type = type;
        }

        private static String getClassName(Class<IndexException> type) {
            return type.getName();
        }

        private static String getMessage(int index) {
            return new IllegalVariableException(index).getMessage();
        }

        @Override
        public boolean matches(Object obj) {
            if (type.isAssignableFrom(obj.getClass()) == false)
                return false;
            IndexException ex = (IndexException) obj;
            return index == ex.index();
        }
    }
}
