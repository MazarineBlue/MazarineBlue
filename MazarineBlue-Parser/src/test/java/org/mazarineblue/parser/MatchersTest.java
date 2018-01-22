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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mazarineblue.parser.analyser.lexical.TestMatcherFacade.createDefaultStringMatcher;
import org.mazarineblue.parser.analyser.lexical.matchers.ComplexVariableMatcher;
import org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher;
import org.mazarineblue.parser.analyser.lexical.matchers.StringMatcher;
import org.mazarineblue.utilities.exceptions.NeverThrownException;
import static org.mazarineblue.utilities.util.TestUtil.assertRegex;

@RunWith(HierarchicalContextRunner.class)
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class MatchersTest {

    @Test(expected = NeverThrownException.class)
    public void lenght_DefaultStringMatcher() {
        createDefaultStringMatcher().length();
    }

    @Test(expected = NeverThrownException.class)
    public void willMatch_DefaultStringMatcher() {
        createDefaultStringMatcher().willMatch(' ', 0);
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestGenericVariableMatcher {

        private GenericVariableMatcher matcher;
        private String REGEX;

        @Before
        public void setup() {
            matcher = new ComplexVariableMatcher();
            REGEX = "mode=[^,]+, character='.', index=-?[0-9]+";
        }

        @After
        public void teardown() {
            matcher = null;
            REGEX = null;
        }

        @Test
        public void toString_Intialized() {
            assertRegex(REGEX, matcher.toString());
        }

        @Test
        public void toString_ProcessCharCalled() {
            matcher.processChar('$', 1);
            assertRegex(REGEX, matcher.toString());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestStringMatcher {

        private StringMatcher matcher;

        @Before
        public void setup() {
            matcher = new StringMatcher("foo");
        }

        @After
        public void teardown() {
            matcher = null;
        }

        @Test
        public void toString_Intialized() {
            assertEquals("text=foo, start=-1, end=-1, match=false", matcher.toString());
        }

        @Test
        public void toString_ProcessCharChalled1() {
            matcher.processChar('f', 1);
            matcher.processChar('o', 2);
            assertEquals("text=foo, start=1, end=-1, match=false", matcher.toString());
        }

        @Test
        public void toString_ProcessCharChalled2() {
            matcher.processChar('f', 1);
            matcher.processChar('o', 2);
            matcher.processChar('o', 3);
            assertEquals("text=foo, start=1, end=4, match=true", matcher.toString());
        }
    }
}
