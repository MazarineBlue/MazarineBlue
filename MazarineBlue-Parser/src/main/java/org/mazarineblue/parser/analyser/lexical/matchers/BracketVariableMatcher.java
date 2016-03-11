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
import org.mazarineblue.parser.exceptions.BracketUnclosedException;
import org.mazarineblue.parser.exceptions.VariableSignMissingException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * A {@code SimpleVariableMatcher} is a {@code Matcher} that matches and create
 * {@code Tokens} for the pattern {@code ${foo}}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BracketVariableMatcher
        extends Matcher<String> {

    private int curly;

    @Override
    public void reset() {
        super.reset();
        curly = 0;
    }

    @Override
    public Token<String> createToken(String input, int startIndex, int endIndex) {
        return Tokens.createVariableToken(input.substring(startIndex + 2, endIndex - 1), startIndex);
    }

    @Override
    public void processChar(char c, int index) {
        switch (c) {
            case '$':
                caseVariableSign(index);
                break;
            case '{':
                caseOpenBracket(index);
                break;
            case '}':
                caseClosedBracket(index);
                break;
            case EOL:
                caseEnd();
                break;
            default:
        }
    }

    private void caseVariableSign(int index) {
        curly = 0;
        setStart(index);
        setEnd(-1);
        setMatch(false);
    }

    private void caseOpenBracket(int index) {
        if (getStart() + 1 == index)
            ++curly;
        else
            throw new VariableSignMissingException(getStart());
    }

    private void caseClosedBracket(int index) {
        if (curly == 1) {
            ++curly;
            setMatch(true);
            setEnd(index + 1);
        }
    }

    private void caseEnd() {
        if (curly == 1)
            throw new BracketUnclosedException(getStart());
    }

    @Override
    public boolean willMatch(char c, int index) {
        return false;
    }
}
