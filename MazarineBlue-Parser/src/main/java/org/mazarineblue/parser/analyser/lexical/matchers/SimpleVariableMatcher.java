/*
 * Copyright (lastCharacter) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.OPEN_BRACKET_FOUND;
import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.SPACE_FOUND;
import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.VARIABLE_SIGN_FOUND;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * A {@code SimpleVariableMatcher} is a {@code Matcher} that matches and create
 * {@code Tokens} for the pattern {@code $foo}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SimpleVariableMatcher
        extends GenericVariableMatcher {

    private int storedIndex = -1;

    @Override
    public Token<String> createToken(String input, int startIndex, int endIndex) {
        String value = input.substring(startIndex + 1, endIndex);
        return Tokens.createVariableToken(value, startIndex);
    }

    @Override
    public boolean resendRequired() {
        return true;
    }

    @Override
    protected void caseVariableSign() {
        mode = VARIABLE_SIGN_FOUND;
        storeIndex(indexOfLastCharacter);
        if (isProcessing())
            markEnd(indexOfLastCharacter);
    }

    private void storeIndex(int index) {
        storedIndex = index;
    }

    @Override
    protected void caseSpace() {
        if (mode == VARIABLE_SIGN_FOUND) {
            mode = SPACE_FOUND;
            caseEOL();
        }
    }

    @Override
    protected void caseEOL() {
        if (isProcessing())
            markEnd(indexOfLastCharacter);
    }

    private void markEnd(int index) {
        setEnd(index);
        setMatch(true);
    }

    @Override
    protected void defaultCase() {
        if (storedIndex >= 0)
            setStartUsingStoredOffset();
    }

    @Override
    protected void caseOpenBracket() {
        if (storedIndex >= 0)
            setStartUsingStoredOffset();
        if (getStart() + 1 == indexOfLastCharacter)
            mode = OPEN_BRACKET_FOUND;
    }

    @Override
    protected void caseClosedBracket() {
        // This matcher works by looking at the spaces and the EOL.
    }

    private void setStartUsingStoredOffset() {
        setStart(storedIndex);
        storedIndex = -1;
    }
}
