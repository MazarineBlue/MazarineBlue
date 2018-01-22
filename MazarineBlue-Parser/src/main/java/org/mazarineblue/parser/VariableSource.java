/*
 * Copyright (c) 2011-2013 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.util.Map;
import org.mazarineblue.parser.exceptions.VariableNotFoundException;

/**
 * A {@code VariableSource} is source that's able to variable
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 * @param <R> the output type.
 */
@FunctionalInterface
public interface VariableSource<R> {

    public static <R> VariableSource<R> newInstance(Map<String, R> map) {
        return new VariableSource<R>() {
            @Override
            public R getData(String name) {
                if (map.containsKey(name))
                    return map.get(name);
                throw new VariableNotFoundException(name);
            }

            @Override
            public void setData(String name, R value) {
                map.put(name, value);
            }
        };
    }

    /**
     * Return the object stored under the specified variable name.
     *
     * @param name the variable name.
     * @return the value or {@code null} if such a variable doesn't exist.
     */
    public R getData(String name);

    /**
     * Stores the spacified object under the specified variable name.
     *
     * @param name the variable name.
     * @param value the object to store.
     */
    default void setData(String name, R value) {
        throw new UnsupportedOperationException("Operation not supported jet.");
    }
}
