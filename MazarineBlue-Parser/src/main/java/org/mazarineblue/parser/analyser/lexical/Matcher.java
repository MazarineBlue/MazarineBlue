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
package org.mazarineblue.parser.analyser.lexical;

import org.mazarineblue.parser.tokens.Token;

/**
 * A {@code Matcher} is a class that can parse a String for patterns and create
 * a tokens.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
public abstract class Matcher<T> {

    public static final char EOL = (char) 0;

    private int startIndex = -1;
    private int endIndex = -1;
    private boolean match = false;

    @Override
    public String toString() {
        return "start=" + startIndex + ", end=" + endIndex + ", match=" + match;
    }

    public abstract int length();

    /**
     * Test if this matches will match the specified character.
     *
     * @param c     the character to process.
     * @param index the index the character was found at.
     * @return {@code true} if match will be found.
     */
    public abstract boolean willMatch(char c, int index);

    /**
     * Test if this matches found a match.
     *
     * @return {@code true} if match is found.
     */
    final boolean isMatch() {
        return match;
    }

    protected final void setMatch(boolean match) {
        this.match = match;
    }

    /**
     * Test if the {@link #setStart(int) setStart} method was called and
     * {@link #setEnd(int) setEnd} method was not called.
     *
     * @return {@code true} is matcher is processing the input stream of
     *         characters.
     */
    protected boolean isProcessing() {
        return startIndex >= 0 && endIndex == -1;
    }

    /**
     * Returns the start index of the variable.
     *
     * @return the start index (inclusive).
     */
    protected final int getStart() {
        return startIndex;
    }

    protected final void setStart(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * Returns the stop index of the variable.
     *
     * @return the end index (exclusive).
     */
    protected final int getEnd() {
        return endIndex;
    }

    protected final void setEnd(int endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * Processes the specified character.
     *
     * @param c     the character to process.
     * @param index the index the character was found at.
     */
    protected abstract void processChar(char c, int index);

    /**
     * Creates a {@code Token} containing a substring of the specified input
     * string.
     *
     * @param input      the string to take the substring from.
     * @param startIndex the starting index, inclusive.
     * @param endIndex   the ending index, exclusive.
     * @return the created {@code Token} with the specified substring.
     *
     * @see Token
     */
    protected abstract Token<T> createToken(T input, int startIndex, int endIndex);

    /**
     * Resets this matcher in the initialize state.
     */
    protected void reset() {
        startIndex = endIndex = -1;
        match = false;
    }

    /**
     * Test whetter or not the last character needs to resend.
     *
     * @return true if the last character needs to be reprocesses.
     */
    protected boolean resendRequired() {
        return false;
    }

    /**
     * Test if this matcher should be stored before the other matcher.
     *
     * @param other the matcher to compare
     * @return {@code true} if this matcher should be stored before the other.
     */
    public boolean before(Matcher<T> other) {
        return length() > other.length();
    }
}
