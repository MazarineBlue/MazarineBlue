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

import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.simple.exceptions.IllegalVariableException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class DataMediator {

    private final Storage storage;
    private final VariableSource source;
    private final ProcessorStack stack;

    public DataMediator(VariableSource source, ProcessorStack stack) {
        this.storage = new Storage();
        this.source = source;
        this.stack = stack;
    }

    public ProcessorStack getProcessorStack() {
        return stack;
    }

    public void appendToOutput(CharacterContainer c) {
        appendTo(Storage.OUTPUT, c);
    }

    public void appendToVariable(CharacterContainer c) {
        appendTo(Storage.VARIABLE, c);
    }

    public void appendTo(String key, CharacterContainer c) {
        storage.appendTo(key, c);
    }

    public void appendVariableContentToOutput(CharacterContainer c) {
        String content = getVariableData(c, source);
        appendTo(Storage.OUTPUT, content);
        remove(Storage.VARIABLE);
    }

    public void appendTo(String key, String str) {
        storage.appendTo(key, str);
    }

    public void remove(String key) {
        storage.remove(key);
    }

    private String getVariableData(CharacterContainer c, VariableSource source) {
        String variable = getStorage(Storage.VARIABLE);
        Object result = getData(variable);
        if (result == null)
            throw new IllegalVariableException(calculateIndex(c));
        return result.toString();
    }

    public Object getData(String variable) {
        return source.getData(variable);
    }

    public int calculateIndex(CharacterContainer c) {
        return calculateIndex(c, 0);
    }

    public int calculateIndex(CharacterContainer c, int offset) {
        int index = c.index - offset;
        String variable = getStorage(Storage.VARIABLE);
        return variable.isEmpty() ? index : index - variable.length();
    }

    public String getOutput() {
        return getStorage(Storage.OUTPUT);
    }

    public String getStorage(String key) {
        return storage.get(key);
    }
}
