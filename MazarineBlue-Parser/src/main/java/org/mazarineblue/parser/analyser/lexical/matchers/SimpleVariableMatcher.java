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
import org.mazarineblue.parser.exceptions.IllegalCloseBacketException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tokens.Tokens;

/**
 * A {@code SimpleVariableMatcher} is a {@code Matcher} that matches and create
 * {@code Tokens} for the pattern {@code $foo}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SimpleVariableMatcher
        extends Matcher<String> {

    private int previous = -1;
    private boolean curly = false;

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
    public void processChar(char c, int index) {
        switch (c) {
            case '$':
                caseVariableSign(index);
                break;
            case '{':
                caseOpenBracket(index);
                break;
            case '}':
                caseCloseBracket();
                break;
            case ' ':
            case EOL:
                caseEnd(index);
                break;
            default:
                defaultCase();
        }
    }

    private void caseVariableSign(int index) {
        storeOffset(index);
        if (isProcessing())
            markEnd(index);
    }

    private void caseCloseBracket() {
        if (isOpenBracket())
            setOpenBracket(false);
        else
            throw new IllegalCloseBacketException(getStart());
    }

    private void caseOpenBracket(int index) {
        if (isOffsetStored())
            markStartUsingStoredOffset();
        if (isOpenBracket(index))
            setOpenBracket(true);
    }

    private void caseEnd(int index) {
        if (isProcessing())
            markEnd(index);
    }

    private void defaultCase() {
        if (isOffsetStored())
            markStartUsingStoredOffset();
    }

    private void storeOffset(int index) {
        previous = index;
    }

    private boolean isProcessing() {
        return getStart() >= 0 && getEnd() == -1;
    }

    private void markEnd(int index) {
        setEnd(index);
        setMatch(true);
    }

    private boolean isOffsetStored() {
        return previous >= 0;
    }

    private void markStartUsingStoredOffset() {
        setStart(previous);
        setEnd(-1);
        setMatch(false);
        previous = -1;
    }

    private boolean isOpenBracket() {
        return curly;
    }

    private void setOpenBracket(boolean flag) {
        curly = flag;
    }

    private boolean isOpenBracket(int index) {
        return getStart() + 1 == index;
    }

    @Override
    public boolean willMatch(char c, int index) {
        return false;
    }
}
