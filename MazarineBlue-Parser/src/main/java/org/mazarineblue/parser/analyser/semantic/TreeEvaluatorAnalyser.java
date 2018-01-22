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
package org.mazarineblue.parser.analyser.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.mazarineblue.parser.exceptions.IllegalSyntaxTreeException;
import org.mazarineblue.parser.tokens.Token;
import org.mazarineblue.parser.tree.SyntaxTreeNode;
import static org.mazarineblue.parser.tree.TreeUtil.isBinary;
import static org.mazarineblue.parser.tree.TreeUtil.isLeaf;
import static org.mazarineblue.parser.tree.TreeUtil.isUnary;

/**
 * A {@code TreeEvaluatorAnalyser} is a {@code SemanticAnalyser} that
 * evaluates a {@link SyntaxTreeNode syntax tree} using the functions
 * registered with for the tokens.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TreeEvaluatorAnalyser<R>
        implements SemanticAnalyser<String, R> {

    private final Function<String, R> leafFunc;
    private final Map<String, Function<R, R>> unaries = new HashMap<>(4);
    private final Map<String, BiFunction<R, R, R>> binaries = new HashMap<>(4);

    public TreeEvaluatorAnalyser(Function<String, R> leafFunc) {
        this.leafFunc = leafFunc;
    }

    /**
     * Defines a unary operator using a specified symbol and a specified
     * function. The function is called every time the analyser comes across
     * the specified symbol.
     *
     * @param identifier the symbol that is paired with the function.
     * @param function   the function that is paired with the symbol.
     */
    public void addFunction(String identifier, Function<R, R> function) {
        if (function == null)
            throw new IllegalArgumentException();
        unaries.put(identifier, function);
    }

    /**
     * Defines a binary operator using a specified symbol and a specified
     * function. The function is called every time the analyser comes across
     * the specified symbol.
     *
     * @param key      the symbol that is paired with the function.
     * @param function the function that is paired with the symbol.
     */
    public void addFunction(String key, BiFunction<R, R, R> function) {
        if (function == null)
            throw new IllegalArgumentException();
        binaries.put(key, function);
    }

    @Override
    public R evaluate(SyntaxTreeNode<String> tree) {
        if (tree == null)
            throw new IllegalSyntaxTreeException(tree);
        if (isLeaf(tree))
            return leafFunc.apply(getTokenValue(tree));
        if (isUnary(tree))
            return evaluateUnaryTree(tree);
        if (isBinary(tree))
            return evaluateBinaryTree(tree);
        throw new IllegalSyntaxTreeException(tree);
    }

    private String getTokenValue(SyntaxTreeNode<String> tree) {
        String value = getTokenValue(getToken(tree));
        if (value == null)
            throw new IllegalSyntaxTreeException(tree);
        return value;
    }

    private Token<String> getToken(SyntaxTreeNode<String> tree) {
        Token<String> token = tree.getToken();
        if (token == null)
            throw new IllegalSyntaxTreeException(tree);
        return token;
    }

    private String getTokenValue(Token<String> token) {
        return token.getValue();
    }

    private R evaluateUnaryTree(SyntaxTreeNode<String> tree) {
        Function<R, R> func = getUnaryFunction(tree);
        R right = evaluate(tree.getRightChild());
        return func.apply(right);
    }

    private R evaluateBinaryTree(SyntaxTreeNode<String> tree) {
        BiFunction<R, R, R> func = getBinaryFunction(tree);
        R left = evaluate(tree.getLeftChild());
        R right = evaluate(tree.getRightChild());
        return func.apply(left, right);
    }

    private Function<R, R> getUnaryFunction(SyntaxTreeNode<String> tree) {
        String key = getTokenValue(tree);
        Function<R, R> func = unaries.get(key);
        if (func == null)
            throw new IllegalSyntaxTreeException(tree);
        return func;
    }

    private BiFunction<R, R, R> getBinaryFunction(SyntaxTreeNode<String> tree) {
        String key = getTokenValue(tree);
        BiFunction<R, R, R> func = binaries.get(key);
        if (func == null)
            throw new IllegalSyntaxTreeException(tree);
        return func;
    }
}
