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
package org.mazarineblue.parser.precedenceclimbing.operators;

import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.precedenceclimbing.exceptions.VariableNameExpectedInLeftParameterException;
import org.mazarineblue.parser.precedenceclimbing.storage.BinaryOperator;
import org.mazarineblue.parser.precedenceclimbing.storage.Operator.Associativity;
import org.mazarineblue.parser.precedenceclimbing.storage.PrefixUnaryOperator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class OperatorFacade {

    public static PrefixUnaryOperator createUnaryPlus(int precedence, String symbol) {
        return createUnaryPlus(precedence, symbol, Associativity.LEFT);
    }

    public static PrefixUnaryOperator createUnaryPlus(int precedence, String symbol, Associativity association) {
        return new DoublePrefixUnaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer obj) {
                return +obj;
            }

            @Override
            protected Object evaluate(Long obj) {
                return +obj;
            }

            @Override
            protected Object evaluate(Float obj) {
                return +obj;
            }

            @Override
            protected Object evaluate(Double obj) {
                return +obj;
            }
        };
    }

    public static PrefixUnaryOperator createUnaryMinus(int precedence, String symbol) {
        return createUnaryMinus(precedence, symbol, Associativity.LEFT);
    }

    public static PrefixUnaryOperator createUnaryMinus(int precedence, String symbol, Associativity association) {
        return new DoublePrefixUnaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer obj) {
                return -obj;
            }

            @Override
            protected Object evaluate(Long obj) {
                return -obj;
            }

            @Override
            protected Object evaluate(Float obj) {
                return -obj;
            }

            @Override
            protected Object evaluate(Double obj) {
                return -obj;
            }
        };
    }

    public static PrefixUnaryOperator createLogicalNot(int precedence, String symbol) {
        return createLogicalNot(precedence, symbol, Associativity.LEFT);
    }

    public static PrefixUnaryOperator createLogicalNot(int precedence, String symbol, Associativity association) {
        return new BooleanPrefixUnaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Boolean obj) {
                return obj == false;
            }
        };
    }

    public static PrefixUnaryOperator createBitwiseNot(int precedence, String symbol) {
        return createBitwiseNot(precedence, symbol, Associativity.LEFT);
    }

    public static PrefixUnaryOperator createBitwiseNot(int precedence, String symbol, Associativity association) {
        return new LongPrefixUnaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer obj) {
                return ~obj;
            }

            @Override
            protected Object evaluate(Long obj) {
                return ~obj;
            }
        };
    }

    public static PrefixUnaryOperator createSquareRoot(int precedence, String symbol) {
        return createSquareRoot(precedence, symbol, Associativity.LEFT);
    }

    public static PrefixUnaryOperator createSquareRoot(int precedence, String symbol, Associativity association) {
        return new DoublePrefixUnaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer obj) {
                return Math.sqrt(obj);
            }

            @Override
            protected Object evaluate(Long obj) {
                return Math.sqrt(obj);
            }

            @Override
            protected Object evaluate(Float obj) {
                return Math.sqrt(obj);
            }

            @Override
            protected Object evaluate(Double obj) {
                return Math.sqrt(obj);
            }
        };
    }

    public static BinaryOperator createPower(int precedence, String symbol) {
        return createPower(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createPower(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return Math.pow(left, right);
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return Math.pow(left, right);
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return Math.pow(left, right);
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return Math.pow(left, right);
            }
        };
    }

    public static BinaryOperator createMultiplication(int precedence, String symbol) {
        return createMultiplication(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createMultiplication(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left * right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left * right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left * right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left * right;
            }
        };
    }

    public static BinaryOperator createDivision(int precedence, String symbol) {
        return createDivision(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createDivision(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left / right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left / right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left / right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left / right;
            }
        };
    }

    public static BinaryOperator createModulo(int precedence, String symbol) {
        return createModulo(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createModulo(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left % right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left % right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left % right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left % right;
            }
        };
    }

    public static BinaryOperator createAddition(int precedence, String symbol) {
        return createAddition(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createAddition(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left + right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left + right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left + right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left + right;
            }
        };
    }

    public static BinaryOperator createSubtraction(int precedence, String symbol) {
        return createSubtraction(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createSubtraction(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left - right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left - right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left - right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left - right;
            }
        };
    }

    public static BinaryOperator createBitwiseLeftShift(int precedence, String symbol) {
        return createBitwiseLeftShift(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createBitwiseLeftShift(int precedence, String symbol, Associativity association) {
        return new LongBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left << right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left << right;
            }
        };
    }

    public static BinaryOperator createBitwiseRightShift(int precedence, String symbol) {
        return createBitwiseRightShift(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createBitwiseRightShift(int precedence, String symbol, Associativity association) {
        return new LongBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left >> right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left >> right;
            }
        };
    }

    public static BinaryOperator createLessThen(int precedence, String symbol) {
        return createLessThen(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createLessThen(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left < right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left < right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left < right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left < right;
            }
        };
    }

    public static BinaryOperator createLessThenOrEqualTo(int precedence, String symbol) {
        return createLessThenOrEqualTo(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createLessThenOrEqualTo(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left <= right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left <= right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left <= right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left <= right;
            }
        };
    }

    public static BinaryOperator createGreaterThen(int precedence, String symbol) {
        return createGreaterThen(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createGreaterThen(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left > right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left > right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left > right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left > right;
            }
        };
    }

    public static BinaryOperator createGreaterThenOrEqualTo(int precedence, String symbol) {
        return createGreaterThenOrEqualTo(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createGreaterThenOrEqualTo(int precedence, String symbol, Associativity association) {
        return new DoubleBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left >= right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left >= right;
            }

            @Override
            protected Object evaluate(Float left, Float right) {
                return left >= right;
            }

            @Override
            protected Object evaluate(Double left, Double right) {
                return left >= right;
            }
        };
    }

    public static BinaryOperator createEqualTo(int precedence, String symbol) {
        return createEqualTo(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createEqualTo(int precedence, String symbol, Associativity association) {
        return new BinaryOperator(precedence, symbol, association) {

            @Override
            public Object evaluate(Object left, Object right, VariableSource source) {
                return left.equals(right);
            }
        };
    }

    public static BinaryOperator createNotEqualTo(int precedence, String symbol) {
        return createNotEqualTo(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createNotEqualTo(int precedence, String symbol, Associativity association) {
        return new BinaryOperator(precedence, symbol, association) {

            @Override
            public Object evaluate(Object left, Object right, VariableSource source) {
                return left.equals(right) == false;
            }
        };
    }

    public static BinaryOperator createBitwiseAnd(int precedence, String symbol) {
        return createBitwiseAnd(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createBitwiseAnd(int precedence, String symbol, Associativity association) {
        return new LongBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left & right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left & right;
            }
        };
    }

    public static BinaryOperator createBitwiseXor(int precedence, String symbol) {
        return createBitwiseXor(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createBitwiseXor(int precedence, String symbol, Associativity association) {
        return new LongBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left ^ right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left ^ right;
            }
        };
    }

    public static BinaryOperator createBitwiseOr(int precedence, String symbol) {
        return createBitwiseOr(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createBitwiseOr(int precedence, String symbol, Associativity association) {
        return new LongBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Integer left, Integer right) {
                return left | right;
            }

            @Override
            protected Object evaluate(Long left, Long right) {
                return left | right;
            }
        };
    }

    public static BinaryOperator createLogicalAnd(int precedence, String symbol) {
        return createLogicalAnd(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createLogicalAnd(int precedence, String symbol, Associativity association) {
        return new BooleanBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Boolean left, Boolean right) {
                return left && right;
            }
        };
    }

    public static BinaryOperator createLogicalOr(int precedence, String symbol) {
        return createLogicalOr(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createLogicalOr(int precedence, String symbol, Associativity association) {
        return new BooleanBinaryOperator(precedence, symbol, association) {

            @Override
            protected Object evaluate(Boolean left, Boolean right) {
                return left || right;
            }
        };
    }

    public static BinaryOperator createDirectAssignment(int precedence, String symbol) {
        return createDirectAssignment(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createDirectAssignment(int precedence, String symbol, Associativity association) {
        return new BinaryOperator(precedence, symbol, association) {

            @Override
            public Object evaluate(Object left, Object right, VariableSource source) {
                if (left instanceof String == false)
                    throw new VariableNameExpectedInLeftParameterException(left, right);
                source.setData((String) left, right);
                return right;
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentBySum(int precedence, String symbol) {
        return createCompoundAssignmentBySum(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentBySum(int precedence, String symbol, Associativity association) {
        return new DoubleVariableSourceBinaryOperator(precedence, symbol, association) {
            private DoubleEvaluator evaluator = new DoubleEvaluator() {

                @Override
                public Object evaluate(Float left, Float right) {
                    return left + right;
                }

                @Override
                public Object evaluate(Double left, Double right) {
                    return left + right;
                }

                @Override
                public Object evaluate(Integer left, Integer right) {
                    return left + right;
                }

                @Override
                public Object evaluate(Long left, Long right) {
                    return left + right;
                }
            };

            @Override
            protected DoubleEvaluator getDoubleEvaluator() {
                return evaluator;
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByDifference(int precedence, String symbol) {
        return createCompoundAssignmenetByDifference(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmenetByDifference(int precedence, String symbol,
                                                                       Associativity association) {
        return new DoubleVariableSourceBinaryOperator(precedence, symbol, association) {
            private DoubleEvaluator evaluator = new DoubleEvaluator() {

                @Override
                public Object evaluate(Float left, Float right) {
                    return left - right;
                }

                @Override
                public Object evaluate(Double left, Double right) {
                    return left - right;
                }

                @Override
                public Object evaluate(Integer left, Integer right) {
                    return left - right;
                }

                @Override
                public Object evaluate(Long left, Long right) {
                    return left - right;
                }
            };

            @Override
            protected DoubleEvaluator getDoubleEvaluator() {
                return evaluator;
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByProduct(int precedence, String symbol) {
        return createCompoundAssignmentByProduct(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentByProduct(int precedence, String symbol,
                                                                   Associativity association) {
        return new DoubleVariableSourceBinaryOperator(precedence, symbol, association) {
            private DoubleEvaluator evaluator = new DoubleEvaluator() {

                @Override
                public Object evaluate(Float left, Float right) {
                    return left * right;
                }

                @Override
                public Object evaluate(Double left, Double right) {
                    return left * right;
                }

                @Override
                public Object evaluate(Integer left, Integer right) {
                    return left * right;
                }

                @Override
                public Object evaluate(Long left, Long right) {
                    return left * right;
                }
            };

            @Override
            protected DoubleEvaluator getDoubleEvaluator() {
                return evaluator;
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByQuotient(int precedence, String symbol) {
        return createCompoundAssignmentByQuotient(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentByQuotient(int precedence, String symbol,
                                                                    Associativity association) {
        return new DoubleVariableSourceBinaryOperator(precedence, symbol, association) {
            private DoubleEvaluator evaluator = new DoubleEvaluator() {

                @Override
                public Object evaluate(Float left, Float right) {
                    return left / right;
                }

                @Override
                public Object evaluate(Double left, Double right) {
                    return left / right;
                }

                @Override
                public Object evaluate(Integer left, Integer right) {
                    return left / right;
                }

                @Override
                public Object evaluate(Long left, Long right) {
                    return left / right;
                }
            };

            @Override
            protected DoubleEvaluator getDoubleEvaluator() {
                return evaluator;
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByRemainder(int precedence, String symbol) {
        return createCompoundAssignmentByRemainder(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentByRemainder(int precedence, String symbol,
                                                                     Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left % right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left % right;
                    }
                };
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseLeftShift(int precedence, String symbol) {
        return createCompoundAssignmentByBitwiseLeftShift(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseLeftShift(int precedence, String symbol,
                                                                            Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left << right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left << right;
                    }
                };
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseRightShift(int precedence, String symbol) {
        return createCompoundAssignmentByBitwiseRightShift(precedence, symbol, Associativity.RIGHT);
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseRightShift(int precedence, String symbol,
                                                                             Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left >> right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left >> right;
                    }
                };
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseAnd(int precedence, String symbol) {
        return createCompoundAssignmentByBitwiseAnd(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseAnd(int precedence, String symbol,
                                                                      Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left & right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left & right;
                    }
                };
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseXor(int precedence, String symbol) {
        return createCompoundAssignmentByBitwiseXor(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseXor(int precedence, String symbol,
                                                                      Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left ^ right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left ^ right;
                    }
                };
            }
        };
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseOr(int precedence, String symbol) {
        return createCompoundAssignmentByBitwiseOr(precedence, symbol, Associativity.LEFT);
    }

    public static BinaryOperator createCompoundAssignmentByBitwiseOr(int precedence, String symbol,
                                                                     Associativity association) {
        return new LongVariableSourceBinaryOperator(precedence, symbol, association) {

            @Override
            protected LongEvaluator getLongEvaluator() {
                return new LongEvaluator() {

                    @Override
                    public Object evaluate(Integer left, Integer right) {
                        return left | right;
                    }

                    @Override
                    public Object evaluate(Long left, Long right) {
                        return left | right;
                    }
                };
            }
        };
    }

    private OperatorFacade() {
    }
}
