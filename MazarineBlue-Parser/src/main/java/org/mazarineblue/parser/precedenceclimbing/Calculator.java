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
package org.mazarineblue.parser.precedenceclimbing;

import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.VariableSourceDummy;
import org.mazarineblue.parser.precedenceclimbing.PrecedenceClimbingParser;
import org.mazarineblue.parser.precedenceclimbing.exceptions.TokenException;
import org.mazarineblue.parser.precedenceclimbing.factories.OperatorSearchingFactory;
import org.mazarineblue.parser.precedenceclimbing.operators.OperatorFacade;

/**
 * An implementation example of the {@link Parser Parser} class. Calculator reads a input string from the command line
 * and output the calculated result or an error message when the input was not well formed.
 * <p>
 * This class knows how to add (+), subtract (-), multiply (*), divide (/) and take the squire root (sqrt) from a float.
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 * @see <a href="doc-files/Calculator.txt">Calculator.java</a>
 */
public class Calculator {

    private static final String FORMAT1 = "Input: %s\nOutput: %s\n";
    private static final String FORMAT2 = "Input: %s\nError near index: %s\n";

    /**
     * An example on how to use the {@link Parser Parser} class.
     *
     * @param args the command line argument containing a simple math expressions.
     */
    public static void main(String[] args) {
        printCopyright();
        if (hasArgument(args))
            runCalculator(args);
        else
            printHelp();
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void printCopyright() {
        System.out.println("Copyright (c) 2011-2013 Alex de Kruijff");
    }

    static private boolean hasArgument(String[] args) {
        return args != null && args.length != 0;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void printHelp() {
        System.out.println("Usage: <Calculator> number (operator number)*");
    }

    private static void runCalculator(String[] args) {
        String input = getInput(args);
        try {
            Object output = processInput(input);
            printReport(input, output);
        } catch (TokenException ex) {
            printReport(input, ex);
        }
    }

    private static String getInput(String[] args) {
        String input = null;
        for (String arg : args)
            input = input == null ? arg : input + " " + arg;
        return input;
    }

    private static Object processInput(String input) {
        final Calculator calculator = new Calculator();
        return calculator.parse(input);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void printReport(String input, Object output) {
        System.out.print(String.format(FORMAT1, input, output.toString()));
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseOfSystemOutOrSystemErr"})
    private static void printReport(String input, TokenException ex) {
        System.out.print(String.format(FORMAT2, input, ex.nearIndex() + 1));
    }

    private final PrecedenceClimbingParser parser = new PrecedenceClimbingParser(new OperatorSearchingFactory());

    /**
     * The calculator instruction overrides and adds the operators to this class.
     */
    public Calculator() {
        parser.add(OperatorFacade.createUnaryPlus(3, "+"));
        parser.add(OperatorFacade.createUnaryMinus(3, "-"));
        parser.add(OperatorFacade.createSquareRoot(2, "sqrt"));
        parser.add(OperatorFacade.createPower(2, "^"));
        parser.add(OperatorFacade.createMultiplication(1, "*"));
        parser.add(OperatorFacade.createDivision(1, "/"));
        parser.add(OperatorFacade.createModulo(1, "%"));
        parser.add(OperatorFacade.createAddition(0, "+"));
        parser.add(OperatorFacade.createSubtraction(0, "-"));
    }

    private Object parse(String input) {
        Object result = parser.parse(input, new VariableSourceDummy());
        return result.toString();
    }
}
