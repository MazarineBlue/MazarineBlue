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

import java.util.function.BiFunction;
import java.util.function.Function;
import org.mazarineblue.parser.analyser.lexical.StringLexicalAnalyser;
import org.mazarineblue.parser.analyser.lexical.matchers.StringMatcher;
import org.mazarineblue.parser.analyser.semantic.TreeEvaluatorAnalyser;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Operator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.PrecedenceClimbingAnalyser;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.UnaryOperator;

/**
 * A {@code StringPrecedenceClimbingParser} is a {@code Parser} that takes a
 * {@code String} as input and outputs an {@code Object}.
 * <p>
 * A client is expected to add {@link Operator operators} and functions that
 * are to be called whenever the specified identifier is found. The input of
 * the functions are the pieces of the input {@code String} and the output of
 * other functions.
 * <p>
 * For example, a simple calculator, could be implemented by adding the
 * following operators and functions:
 * <pre>
 * parser.addOperator("+", new UnaryOperator(1, Associativity.RIGHT), t -> convert(t));
 * parser.addOperator("-", new UnaryOperator(1, Associativity.RIGHT), t -> -convert(t));
 * parser.addOperator("-", new BinaryOperator(1, Associativity.LEFT), (t, u) -> convert(t) - convert(u));
 * parser.addOperator("+", new BinaryOperator(1, Associativity.LEFT), (t, u) -> convert(t) + convert(u));
 * parser.addOperator("*", new BinaryOperator(2, Associativity.LEFT), (t, u) -> convert(t) * convert(u));
 * parser.addOperator("/", new BinaryOperator(2, Associativity.LEFT), (t, u) -> convert(t) / convert(u));
 * parser.addOperator("^", new BinaryOperator(3, Associativity.LEFT), (t, u) -> Math.pow(convert(t) , convert(u)));
 * parser.addOperator("sqrt", new UnaryOperator(3, Associativity.LEFT), t -> Math.sqrt(convert(t)));
 * </pre>
 * The convert method needs to convert a {@code String} in to a {@code Double}.
 * The consequences of this is that it can receive both a {@code String} and a
 * {@code Double} as arguments.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StringPrecedenceClimbingParser<R>
        implements Parser<String, R> {

    private final StringLexicalAnalyser lexicalAnalyser;
    private final PrecedenceClimbingAnalyser<String> syntacticAnalyser;
    private final TreeEvaluatorAnalyser<R> semanticAnalyser;
    private final GenericParser<String, R> parser;

    /**
     * Constructs an empty {@code StringPrecedenceClimbingParser}.
     */
    public StringPrecedenceClimbingParser(Function<String, R> leafFunc) {
        lexicalAnalyser = new StringLexicalAnalyser();
        syntacticAnalyser = new PrecedenceClimbingAnalyser<>();
        semanticAnalyser = new TreeEvaluatorAnalyser<>(leafFunc);
        parser = new GenericParser<>(lexicalAnalyser, syntacticAnalyser, semanticAnalyser);
    }

    @Override
    public R parse(String input) {
        return parser.parse(input);
    }

    /**
     * Registers two characters that represent grouping.
     *
     * @param open  this element indicates a grouping started.
     * @param close this element indicates a grouping ended.
     * @return this
     */
    public StringPrecedenceClimbingParser<R> addGroupCharacters(String open, String close) {
        lexicalAnalyser.add(new StringMatcher(open));
        lexicalAnalyser.add(new StringMatcher(close));
        syntacticAnalyser.addGroupCharacters(open, close);
        return this;
    }

    /**
     * Register an {@code UnaryOperator operator} and the function that goes
     * with it under the specified identifier.
     *
     * @param identifier the identifier to register the specified operator and
     *                   function under.
     * @param operator   the operator to register.
     * @param function   the function to register.
     * @return this
     */
    public StringPrecedenceClimbingParser<R> addOperator(String identifier, UnaryOperator operator,
                                                         Function<R, R> function) {
        lexicalAnalyser.add(new StringMatcher(identifier));
        syntacticAnalyser.addOperator(identifier, operator);
        semanticAnalyser.addFunction(identifier, function);
        return this;
    }

    /**
     * Register an {@code BinaryOperator operator} and the function that goes
     * with it under the specified identifier.
     *
     * @param identifier the identifier to register the specified operator and
     *                   function under.
     * @param operator   the operator to register.
     * @param function   the function to register.
     * @return this
     */
    public StringPrecedenceClimbingParser<R> addOperator(String identifier, BinaryOperator operator,
                                                         BiFunction<R, R, R> function) {
        lexicalAnalyser.add(new StringMatcher(identifier));
        syntacticAnalyser.addOperator(identifier, operator);
        semanticAnalyser.addFunction(identifier, function);
        return this;
    }
}
