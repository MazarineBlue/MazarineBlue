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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import org.mazarineblue.parser.simple.exceptions.UnconsumedCharacterContainerException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ProcessorStack
        implements Cloneable {

    private final Deque<CharacterProcessor> stack;

    ProcessorStack() {
        stack = new ArrayDeque();
    }

    ProcessorStack(ProcessorStack stack) {
        this(stack.stack);
    }

    private ProcessorStack(Collection<CharacterProcessor> stack) {
        this.stack = new ArrayDeque();
        for (CharacterProcessor p : stack)
            this.stack.add(p);
    }

    @Override
    public String toString() {
        String str = null;
        for (CharacterProcessor processor : stack)
            if (str == null)
                str = "stack=" + processor.getClass().getSimpleName();
            else
                str += ", " + processor.getClass().getSimpleName();
        return str == null ? "stack=empty" : str;
    }

    void publish(CharacterContainer c, DataMediator mediator) {
        for (CharacterProcessor processor : stack)
            if (c.isConsumed())
                return;
            else if (processor.canProcess(c))
                processor.process(c, mediator);
        if (c.isConsumed() == false) {
            int index = mediator.calculateIndex(c, 0);
            throw new UnconsumedCharacterContainerException(index);
        }
    }

    void finish(CharacterContainer c, DataMediator mediator) {
        for (CharacterProcessor processor : stack)
            processor.finish(c, mediator);
    }

    public void add(CharacterProcessor processor) {
        stack.addFirst(processor);
    }

    public void remove(CharacterProcessor processor) {
        stack.removeFirstOccurrence(processor);
    }

    int size() {
        return stack.size();
    }
}
