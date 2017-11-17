/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.mbt.expressions;

import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.mazarineblue.mbt.Pair;
import org.mazarineblue.mbt.Variable;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.StringPrecedenceClimbingParser;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.Associativity;
import org.mazarineblue.parser.analyser.syntax.precedenceclimbing.storage.BinaryOperator;

public abstract class Expression
        implements Comparable<Expression> {

    private static final Parser<String, Expression> PARSER = new StringPrecedenceClimbingParser<>(Expression::leaf)
            .addOperator("->", new BinaryOperator(19 - 17, Associativity.RIGHT), Expression::implies)
            .addOperator("=", new BinaryOperator(19 - 16, Associativity.RIGHT), Expression::set)
            .addOperator("&&", new BinaryOperator(19 - 13, Associativity.LEFT), Expression::and)
            .addOperator("||", new BinaryOperator(19 - 14, Associativity.LEFT), Expression::or)
            .addOperator("==", new BinaryOperator(19 - 9, Associativity.LEFT), Expression::equal)
            .addOperator("<", new BinaryOperator(19 - 8, Associativity.LEFT), Expression::less)
            .addOperator("<=", new BinaryOperator(19 - 8, Associativity.LEFT), Expression::lessOrEqual)
            .addOperator(">", new BinaryOperator(19 - 8, Associativity.LEFT), Expression::greater)
            .addOperator(">=", new BinaryOperator(19 - 8, Associativity.LEFT), Expression::greaterOrEqual)
            .addOperator("+", new BinaryOperator(19 - 6, Associativity.LEFT), Expression::add)
            .addOperator("-", new BinaryOperator(19 - 6, Associativity.LEFT), Expression::subtract)
            .addOperator("*", new BinaryOperator(19 - 5, Associativity.LEFT), Expression::multiply)
            .addOperator("/", new BinaryOperator(19 - 5, Associativity.LEFT), Expression::divide)
            .addGroupCharacters("(", ")");

    public static Expression buildExpression(String expression) {
        return PARSER.parse(expression);
    }

    public static Expression not(Object ac) {
        return new Not(ac);
    }

    public static Expression leaf(String value) {
        String str = value.toLowerCase().trim();
        if (str.contains("false"))
            return new False();
        if (str.contains("true"))
            return new True();
        if (str.startsWith("$"))
            return new VariableLeaf(str.substring(1));
        return new Leaf(value);
    }

    public static Expression getTrue() {
        return new True();
    }

    public static Expression getFalse() {
        return new False();
    }

    public static Expression and(Object left, Object right) {
        return new And(left, right);
    }

    public static Expression implies(Object left, Object right) {
        return new Implies(left, right);
    }

    public static Expression set(Object left, Object right) {
        return new Assignment(left, right);
    }

    public static Expression or(Object left, Object right) {
        return new Or(left, right);
    }

    public static Expression equal(Object left, Object right) {
        return new Equal(left, right);
    }

    public static Expression notEqual(Object left, Object right) {
        return new NotEqual(left, right);
    }

    public static Expression less(Object left, Object right) {
        return new Less(left, right);
    }

    public static Expression lessOrEqual(Object left, Object right) {
        return new LessOrEqual(left, right);
    }

    public static Expression greater(Object left, Object right) {
        return new Greater(left, right);
    }

    public static Expression greaterOrEqual(Object left, Object right) {
        return new GreaterOrEqual(left, right);
    }

    public static Expression add(Object left, Object right) {
        return new Add(left, right);
    }

    public static Expression subtract(Object left, Object right) {
        return new Subtract(left, right);
    }

    public static Expression multiply(Object left, Object right) {
        return new Multiply(left, right);
    }

    public static Expression divide(Object left, Object right) {
        return new Divide(left, right);
    }

    private final String operator;
    private final Object left;
    private final Object right;

    protected Expression(Object left, String operator, Object right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        String str = "";
        if (left != null)
            if (left instanceof Expression && !(left instanceof Leaf))
                str += "(" + left.toString() + ')';
            else
                str += left.toString();
        if (operator != null)
            str += operator;
        if (right != null)
            if (right instanceof Expression && !(right instanceof Leaf))
                str += "(" + right.toString() + ')';
            else
                str += right.toString();
        return str;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public Expression getLeftExpression() {
        return (Expression) left;
    }

    public Expression getRightExpression() {
        return (Expression) right;
    }

    public Double getLeftNumber() {
        if (left instanceof Exception)
            return ((Expression) left).evaluateNumber();
        return Double.parseDouble(left.toString());
    }

    public Double getRightNumber() {
        if (right instanceof Exception)
            return ((Expression) right).evaluateNumber();
        return Double.parseDouble(right.toString());
    }

    protected String getLeftString() {
        return left.toString().trim();
    }

    protected String getRightString() {
        return right.toString().trim();
    }

    public Set<Variable> getVariables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Expression other) {
        int c = this.getClass().getCanonicalName().compareTo(other.getClass().getCanonicalName());
        if (c == 0)
            c = compare(left, other.left);
        if (c == 0)
            c = compare(right, other.right);
        return c;
    }

    private int compare(Object t, Object u) {
        return t == null && u == null ? 0
                : isExpression(t) && isExpression(u) ? ((Expression) t).compareTo((Expression) u)
                : t == null || isExpression(t) ? -1
                : u == null || isExpression(u) ? 1
                : t.equals(u) ? 0 : t.toString().compareTo(u.toString());
    }

    private static boolean isExpression(Object t) {
        return t instanceof Expression;
    }

    public Boolean evaluateBoolean() {
        throw new UnsupportedOperationException("Not supported");
    }

    public Double evaluateNumber() {
        throw new UnsupportedOperationException("Not supported");
    }

    public Expression not() {
        throw new UnsupportedOperationException("Not supported");
    }

    public Expression convertToDNF() {
        return this;
    }

    public List<Expression> getTerms() {
        return asList(this);
    }

    public Expression swap() {
        if (left != null && right != null)
            return create(right, left);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Expression replace(Assignment condition) {
        Object l = left != null && left instanceof Expression ? getLeftExpression().replace(condition) : getLeft();
        Object r = right != null && right instanceof Expression ? getRightExpression().replace(condition) : getRight();
        if (l == null && r == null)
            return null;
        if (l == null && getRight().equals(r) || r == null && getLeft().equals(l))
            return this;
        if (getLeft().equals(l) && getRight().equals(r))
            return this;
        return create(l, r);
    }

    public Expression replace(Assignment condition, Expression replacement) {
        Object l = left != null && left instanceof Expression ? getLeftExpression().replace(condition, replacement) : getLeft();
        Object r = right != null && right instanceof Expression ? getRightExpression().replace(condition, replacement) : getRight();
        if (l == null && r == null)
            return null;
        if (l == null && getRight().equals(r) || r == null && getLeft().equals(l))
            return this;
        if (getLeft().equals(l) && getRight().equals(r))
            return this;
        Expression e = create(l, r);
        return e.evaluateBoolean() ? replacement : e;
    }

    public Expression append(Assignment condition, Expression replacement) {
        Object l = left != null && left instanceof Expression ? getLeftExpression().append(condition, replacement) : getLeft();
        Object r = right != null && right instanceof Expression ? getRightExpression().append(condition, replacement) : getRight();
        if (l == null || r == null)
            return null;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected Expression create(Object l, Object r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Expression simplify() {
        return left == null && right == null ? null
                : left == null && right instanceof Expression ? create(simplifyLeft(), right)
                : right == null && left instanceof Expression ? create(left, simplifyRight())
                : left instanceof Expression && right instanceof Expression ? simplifyBoth()
                : this;
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for simplify()">
    private Expression simplifyBoth() {
        return create(((Expression) left).simplify(), ((Expression) right).simplify());
    }

    private Expression simplifyLeft() {
        return ((Expression) right).simplify();
    }

    private Expression simplifyRight() {
        return ((Expression) left).simplify();
    }
    //</editor-fold>

    Pair<?> simplify(Expression other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> Set<T> breakdown(Class<T> type) {
        Set<T> set = new HashSet<>();
        visit(expression -> {
            boolean flag = type.isAssignableFrom(expression.getClass());
            if (flag)
                set.add(type.cast(expression));
            return flag;
        });
        return set;
    }

    public void visit(ExpressionVisitor visitor) {
        if (visitor.visitNode(this))
            return;
        if (left instanceof Expression)
            getLeftExpression().visit(visitor);
        if (right instanceof Expression)
            getRightExpression().visit(visitor);
    }

    public boolean isVariableDefined(Variable var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
// define = postcondition
// use = precondition

    public boolean isVariableUsed(Variable var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return 3 * 97 * 97
                + 97 * Objects.hashCode(this.left)
                + Objects.hashCode(this.right);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.left, ((Expression) obj).left)
                && Objects.equals(this.right, ((Expression) obj).right);
    }
}
