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

import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.CLOSED_BRACKED_FOUND;
import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.OPEN_BRACKET_FOUND;
import static org.mazarineblue.parser.analyser.lexical.matchers.GenericVariableMatcher.Mode.VARIABLE_SIGN_FOUND;
import org.mazarineblue.parser.exceptions.BracketUnclosedException;
import org.mazarineblue.parser.exceptions.IllegalCloseBacketException;
import org.mazarineblue.parser.exceptions.IllegalOpenBacketException;
import org.mazarineblue.parser.exceptions.VariableSignMissingException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * A {@code ComplexVariableMatcher} is a {@code Matcher} that matches and create
 * {@code Tokens} for the pattern {@code ${foo}}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ComplexVariableMatcher
        extends GenericVariableMatcher {

    @Override
    public Token<String> createToken(String input, int startIndex, int endIndex) {
        String value = input.substring(startIndex + 2, endIndex - 1);
        return Tokens.createVariableToken(value, startIndex);
    }

    @Override
    protected void caseVariableSign() {
        mode = VARIABLE_SIGN_FOUND;
        setStart(indexOfLastCharacter);
    }

    @Override
    protected void caseOpenBracket() {
        if (getStart() + 1 == indexOfLastCharacter)
            mode = OPEN_BRACKET_FOUND;
        else if (mode == OPEN_BRACKET_FOUND)
            throw new IllegalOpenBacketException(getStart());
        else
            throw new VariableSignMissingException(getStart());
    }

    @Override
    protected void caseClosedBracket() {
        if (mode == VARIABLE_SIGN_FOUND)
            throw new IllegalCloseBacketException(indexOfLastCharacter);
        mode = CLOSED_BRACKED_FOUND;
        setMatch(true);
        setEnd(indexOfLastCharacter + 1);
    }

    @Override
    protected void caseEOL() {
        if (mode == OPEN_BRACKET_FOUND)
            throw new BracketUnclosedException(getStart());
    }

    @Override
    protected void caseSpace() {
        // This matcher works by looking at the open and closed brackets.
    }

    @Override
    protected void defaultCase() {
        // This matcher works by looking at the open and closed brackets.
    }
}
