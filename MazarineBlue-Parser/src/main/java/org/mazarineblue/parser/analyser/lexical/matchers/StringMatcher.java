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
package org.mazarineblue.parser.analyser.lexical.matchers;

import org.mazarineblue.parser.analyser.lexical.Matcher;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * A {@code StringMatcher} is a {@code Matcher} that matches any text.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StringMatcher
        extends Matcher<String> {

    private final String text;
    private int index = 0;

    /**
     * Constructs a {@code StringMatcher} that looks for the specified text and
     * indicate a match or create a token for it.
     *
     * @param text the text to match.
     */
    public StringMatcher(String text) {
        if (text == null)
            throw new IllegalArgumentException();
        this.text = text;
    }

    @Override
    public int length() {
        return text.length();
    }

    @Override
    public String toString() {
        return "text=" + text + ", " + super.toString();
    }

    @Override
    public Token<String> createToken(String input, int startIndex, int endIndex) {
        return Tokens.createLiteralToken(input.substring(startIndex, endIndex), startIndex);
    }

    @Override
    public boolean willMatch(char c, int index) {
        if (c != text.charAt(this.index))
            return false;
        return isEnded(this.index + 1);
    }

    @Override
    public void processChar(char c, int index) {
        if (c != text.charAt(this.index)) {
            this.index = 0;
            setMatch(false);
            return;
        }
        ++this.index;
        if (isStart())
            markStart(index);
        if (isEnded(this.index))
            markEnd(index);
    }

    private boolean isStart() {
        return this.index == 1;
    }

    private void markStart(int index) {
        setStart(index);
    }

    private boolean isEnded(int index) {
        return index == text.length();
    }

    private void markEnd(int index) {
        this.index = 0;
        setEnd(index + 1);
        setMatch(true);
    }
}
