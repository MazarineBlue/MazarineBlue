/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.util.TestToken;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
abstract class AbstractTest {

    private Token<String> loremIpsum, loremIpsumDeepCopy, empty;

    @Before
    public void setup() {
        loremIpsum = new TestToken("lorem ipsum");
        loremIpsumDeepCopy = new TestToken("lorem ipsum");
        empty = new TestToken("");
    }

    @After
    public void teardown() {
        loremIpsum = loremIpsumDeepCopy = empty = null;
    }

    @Test
    public void hashCode_Orig_Copy_Equal() {
        assertEquals(loremIpsum.hashCode(), loremIpsumDeepCopy.hashCode());
    }

    @Test
    public void hashCode_Copy_Orig_Equal() {
        assertEquals(loremIpsumDeepCopy.hashCode(), loremIpsum.hashCode());
    }

    @Test
    public void hashCode_Orig_Empty_Unequal() {
        assertNotEquals(loremIpsum.hashCode(), empty.hashCode());
    }

    @Test
    public void hashCode_Copy_Empty_Unequal() {
        assertNotEquals(loremIpsumDeepCopy.hashCode(), empty.hashCode());
    }

    @Test
    public void equals_Orig_Orig_True() {
        assertTrue(loremIpsum.equals(loremIpsumDeepCopy));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Orig_Null_False() {
        assertFalse(loremIpsum.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_Orig_String_False() {
        assertFalse(loremIpsum.equals(""));
    }

    @Test
    public void equals_Orig_Empty_False() {
        assertFalse(loremIpsum.equals(empty));
    }

    @Test
    public void equals_Orig_Copy_True() {
        assertTrue(loremIpsum.equals(loremIpsum));
    }
}
