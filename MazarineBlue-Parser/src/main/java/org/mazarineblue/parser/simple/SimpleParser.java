/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.parser.simple;

import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.VariableSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SimpleParser
        implements Parser {

    private final ProcessorStack stack;

    public SimpleParser() {
        stack = new ProcessorStack();
    }

    public void addCharacterProcessor(CharacterProcessor processor) {
        stack.add(processor);
    }

    public void removeCharacterProcessor(CharacterProcessor processor) {
        stack.remove(processor);
    }

    public int countCharacterProcessors() {
        return stack.size();
    }

    @Override
    public Object parse(String expression, VariableSource source) {
        Parser.checkInput(expression, source);
        return parse(expression.toCharArray(), source);
    }

    private String parse(char[] arr, VariableSource source) {
        ProcessorStack s = new ProcessorStack(stack);
        DataMediator mediator = new DataMediator(source, s);
        for (int i = 0; i < arr.length; ++i)
            s.publish(new CharacterContainer(arr[i], i), mediator);
        s.finish(new CharacterContainer((char) 0, arr.length), mediator);
        return mediator.getOutput();
    }
}
