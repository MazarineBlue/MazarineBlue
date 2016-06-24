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

import org.mazarineblue.parser.precedenceclimbing.Calculator;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CalculatorTest {

    private static final String HELP
            = "Copyright (c) 2011-2013 Alex de Kruijff\n"
            + "Usage: <Calculator> number (operator number)*";

    private PrintStream old;
    private ByteArrayOutputStream output;

    @Before
    public void setup() {
        old = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @After
    public void teardown() {
        System.setOut(old);
    }

    private void assertSystemOutEquals(String input)
            throws IOException {
        BufferedReader expected = getExpectedReader(input);
        BufferedReader actual = getActualReader();
        assertLinesEquals(expected, actual);
    }

    private BufferedReader getExpectedReader(String input) {
        Reader reader = new StringReader(input);
        return new BufferedReader(reader);
    }

    private BufferedReader getActualReader() {
        InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        Reader reader = new InputStreamReader(inputStream);
        return new BufferedReader(reader);
    }

    private void assertLinesEquals(BufferedReader expected, BufferedReader actual)
            throws IOException {
        String line = null;
        while ((line = expected.readLine()) != null)
            assertEquals(line, actual.readLine());
        assertEquals(line, actual.readLine());
    }

    @Test
    public void main_Null_ReturnsHelp()
            throws Exception {
        Calculator.main(null);
        assertSystemOutEquals(HELP);
    }

    @Test
    public void main_Empty_ReturnsHelp()
            throws Exception {
        Calculator.main(new String[]{});
        assertSystemOutEquals(HELP);
    }

    @Test
    public void main_SingleArgument_ReturnsHelp()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: 2+2\n"
                + "Output: 4";
        Calculator.main(new String[]{"2+2"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_MultipleArguments_ReturnsHelp()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: 2 + 2\n"
                + "Output: 4";
        Calculator.main(new String[]{"2", "+", "2"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_SquarRootLetter_ReturnsError()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: sqrt a\n"
                + "Error near index: 5";
        Calculator.main(new String[]{"sqrt", "a"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_SquarRootFour_ReturnsTwo()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: sqrt 4\n"
                + "Output: 2.0";
        Calculator.main(new String[]{"sqrt", "4"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_TwoPlusLetter_ReturnsError()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: 2 + b\n"
                + "Error near index: 4";
        Calculator.main(new String[]{"2", "+", "b"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_LetterPlusTwo_ReturnsError()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: a + 2\n"
                + "Error near index: 1";
        Calculator.main(new String[]{"a", "+", "2"});
        assertSystemOutEquals(output);
    }

    @Test
    public void main_LetterPlusLetter_ReturnsError()
            throws Exception {
        String output = "Copyright (c) 2011-2013 Alex de Kruijff\n"
                + "Input: a + b\n"
                + "Error near index: 1";
        Calculator.main(new String[]{"a", "+", "b"});
        assertSystemOutEquals(output);
    }
}
