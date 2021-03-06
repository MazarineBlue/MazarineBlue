/*
 * Copyright (lastCharacter) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

public abstract class GenericVariableMatcher
        extends Matcher<String> {

    @SuppressWarnings("ProtectedInnerClass")
    protected enum Mode {
        DEFAULT,
        VARIABLE_SIGN_FOUND,
        OPEN_BRACKET_FOUND,
        CLOSED_BRACKED_FOUND,
        SPACE_FOUND
    }

    @SuppressWarnings("ProtectedField")
    protected Mode mode = Mode.DEFAULT;

    @SuppressWarnings("ProtectedField")
    protected char lastCharacter = ' ';

    @SuppressWarnings("ProtectedField")
    protected int indexOfLastCharacter = -1;

    @Override
    public String toString() {
        return "mode=" + mode + ", character='" + lastCharacter + "', index=" + indexOfLastCharacter;
    }

    @Override
    public int length() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean willMatch(char c, int index) {
        return false;
    }

    @Override
    public void reset() {
        super.reset();
        mode = Mode.DEFAULT;
    }

    @Override
    public void processChar(char c, int index) {
        lastCharacter = c;
        indexOfLastCharacter = index;
        switch (c) {
            case '$':
                caseVariableSign();
                break;
            case '{':
                caseOpenBracket();
                break;
            case '}':
                caseClosedBracket();
                break;
            case ' ':
                caseSpace();
                break;
            case EOL:
                caseEOL();
                break;
            default:
                defaultCase();
        }
    }

    protected abstract void caseVariableSign();

    protected abstract void caseOpenBracket();

    protected abstract void caseClosedBracket();

    protected abstract void caseEOL();

    protected abstract void caseSpace();

    protected abstract void defaultCase();
}
