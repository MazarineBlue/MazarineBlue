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
package org.mazarineblue.parser;

import org.mazarineblue.parser.analyser.lexical.StringLexicalAnalyser;
import org.mazarineblue.parser.analyser.lexical.matchers.ComplexVariableMatcher;
import org.mazarineblue.parser.analyser.lexical.matchers.SimpleVariableMatcher;
import org.mazarineblue.parser.analyser.semantic.SemanticAnalyser;
import org.mazarineblue.parser.analyser.semantic.TreeConcatenatingAnalyser;
import org.mazarineblue.parser.analyser.syntax.SyntacticAnalyser;
import org.mazarineblue.parser.analyser.syntax.TokenConcatenatingAnalyser;

/**
 * A {@code StringVariableParser} is a {@code Parser} that looks for variables
 * within a String and replaces the variable name with the its value.
 * <p>
 * A variable can be marked using one of two formats: $variable ${my variable}.
 * The former is separated by a space of a end of line (EOL) character and the
 * latter can contain spaces.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StringVariableParser
        implements Parser<String, Object> {

    private final GenericParser<String, Object> parser;

    /**
     * Constructs a parser with an variable source that is used to replace
     * variables names with its value.
     *
     * @param source the variable source to use for the replacements.
     */
    public StringVariableParser(VariableSource<Object> source) {
        StringLexicalAnalyser lexicalAnalyser = new StringLexicalAnalyser();
        lexicalAnalyser.add(new ComplexVariableMatcher());
        lexicalAnalyser.add(new SimpleVariableMatcher());

        SyntacticAnalyser<String> syntacticAnalyser = new TokenConcatenatingAnalyser<>();
        SemanticAnalyser<String, Object> semanticAnalyser = new TreeConcatenatingAnalyser(source);

        parser = new GenericParser<>(lexicalAnalyser, syntacticAnalyser, semanticAnalyser);
    }

    @Override
    public Object parse(String input) {
        return parser.parse(input);
    }
}
