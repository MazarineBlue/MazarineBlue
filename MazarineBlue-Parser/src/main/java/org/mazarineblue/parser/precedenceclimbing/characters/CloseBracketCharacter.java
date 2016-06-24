/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.parser.precedenceclimbing.characters;

import org.mazarineblue.parser.precedenceclimbing.storage.SpecialCharacter;
import org.mazarineblue.parser.precedenceclimbing.exceptions.OpenBracketUnexpectedException;
import org.mazarineblue.parser.precedenceclimbing.tokens.Token;
import org.mazarineblue.parser.precedenceclimbing.tree.Tree;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CloseBracketCharacter
        extends SpecialCharacter {

    static final Token closeToken = new Token(")");

    @Override
    public boolean canParse(Token token) {
        return token.equals(closeToken);
    }

    @Override
    public Tree parse(Token token) {
        throw new OpenBracketUnexpectedException(token);
    }

    @Override
    public boolean canCreateToken(String token) {
        String str = token.trim();
        if (str.isEmpty())
            return false;
        
        int n = str.length();
        String actual = str.substring(n-1, n);
        String expected = closeToken.getToken();
        return actual.equals(expected);
    }

    @Override
    public Token createToken(String token) {
        int index = token.lastIndexOf(closeToken.getToken());
        String tokenWithoutBracket = token.substring(0, index);
        return fetchToken.createToken(index == 0 ? token : tokenWithoutBracket);
    }
}
