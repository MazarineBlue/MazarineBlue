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

public class And
        extends Expression {

    And(Object left, Object right) {
        super(left, "&&", right);
    }

    @Override
    public Boolean evaluateBoolean() {
        return getLeftExpression().evaluateBoolean() && getRightExpression().evaluateBoolean();
    }

    @Override
    public Expression not() {
        return or(getLeftExpression().not(), getRightExpression().not());
    }

    @Override
    public Expression convertToDNF() {
        Object left = getLeft();
        Object rigth = getRight();
        if (left instanceof Or) { // (p || q) && r <=> p && r || q && r
            Expression l = and(getLeftExpression().getLeft(), rigth).convertToDNF();
            Expression r = and(getLeftExpression().getRight(), rigth).convertToDNF();
            return or(l, r);
        }
        if (rigth instanceof Or) { // p && (q || r) <=> p && q || p && r
            Expression l = and(left, getRightExpression().getLeft()).convertToDNF();
            Expression r = and(left, getRightExpression().getRight()).convertToDNF();
            return or(l, r);
        }
        return this;
    }

    @Override
    protected Expression create(Object l, Object r) {
        return new And(l, r);
    }
}
