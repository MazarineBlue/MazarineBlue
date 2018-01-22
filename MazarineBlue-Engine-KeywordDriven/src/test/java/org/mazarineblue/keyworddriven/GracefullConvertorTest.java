/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.keyworddriven;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceClass;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceMethod;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class GracefullConvertorTest {

    @Test
    public void convertKeyword_InputOneWords_OutputsMethodName() {
        assertEquals("keyWord", degraceMethod("KeyWord"));
    }

    @Test
    public void convertKeyword_InputFewWords_OutputsMethodName() {
        assertEquals("slimTest", degraceMethod("slim test"));
    }

    @Test
    public void convertKeyword_InputTextEndingWithADot_OutputsMethodName() {
        assertEquals("loginUser", degraceMethod("login user."));
    }

    @Test
    public void convertKeyword_InputSentence_OutputsMethodName() {
        assertEquals("openConnectionAndAddUser", degraceMethod("Open connection and add user."));
    }

    @Test
    public void convertKeyword_InputSentenceWithGarbage_OutputsMethodName() {
        assertEquals("openConnectionAndAddUser", degraceMethod(" #Open connection ###and add user."));
    }

    @Test
    public void convertKeyword_InputOneWords_OutputsClassName() {
        assertEquals("MyClass", degraceClass("myClass"));
    }

    @Test
    public void convertKeyword_InputFewWords_OutputsClassName() {
        assertEquals("SlimTest", degraceClass("slim test"));
    }

    @Test
    public void convertKeyword_InputTextEndingWithADot_OutputsClassName() {
        assertEquals("LoginUser", degraceClass("login user."));
    }

    @Test
    public void convertKeyword_InputSentence_OutputsClassName() {
        assertEquals("OpenConnectionAndAddUser", degraceClass("Open connection and add user."));
    }

    @Test
    public void convertKeyword_InputSentenceWithGarbage_OutputsClassName() {
        assertEquals("OpenConnectionAndAddUser", degraceClass(" #Open connection ###and add user."));
    }
}
