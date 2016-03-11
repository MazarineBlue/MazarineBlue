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

import java.util.List;
import org.mazarineblue.parser.exceptions.BracketUnclosedException;
import org.mazarineblue.parser.exceptions.IllegalCloseBacketException;
import org.mazarineblue.parser.exceptions.VariableSignMissingException;
import org.mazarineblue.parser.tokens.Token;

/**
 * A {@code LexicalAnalyser} converts data into an set of tokens.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the parser input type.
 */
@FunctionalInterface
public interface LexicalAnalyser<T> {

    /**
     * Converts the specified input into an array of {@link Token Tokens}.
     *
     * @param input the input data.
     * @return a list of tokens representing the input data.
     *
     * @throws VariableSignMissingException when an open bracket was used with
     *                                      no variable sign in front of it.
     * @throws BracketUnclosedException     when an open bracket was not closed.
     * @throws IllegalCloseBacketException  when an close bracket was used with
     *                                      no opening bracket in front of it.
     */
    public List<Token<T>> breakdown(T input);
}
