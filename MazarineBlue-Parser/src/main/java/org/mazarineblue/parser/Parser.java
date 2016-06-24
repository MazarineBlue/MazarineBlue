/*
 * Copyright (c) 2011-2013 Alex de Kruijff
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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.parser;

import org.mazarineblue.parser.exceptions.InvalidExpressionException;
import org.mazarineblue.parser.exceptions.InvalidVariableSourceException;
import org.mazarineblue.parser.exceptions.ResultOfUnexpectedTypeException;
import org.mazarineblue.parser.exceptions.TypeNotSpecifiedException;

/**
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public interface Parser {

    default <T> T parse(String expression, VariableSource source, Class<T> type) {
        Object result = parse(expression, source);
        checkOutput(type, result);
        return (T) result;
    }

    static void checkOutput(Class expected, Object result) {
        if (expected == null)
            throw new TypeNotSpecifiedException();
        if (result == null)
            return;
        if (expected.isAssignableFrom(result.getClass()) == false)
            throw new ResultOfUnexpectedTypeException(expected, result);
    }

    Object parse(String expression, VariableSource source);

    static void checkInput(Object expression, VariableSource source) {
        if (expression == null)
            throw new InvalidExpressionException(expression);
        if (source == null)
            throw new InvalidVariableSourceException(source);
    }
}
