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

public class Leaf
        extends Expression {

    Leaf(String value) {
        super(value.trim(), null, null);
    }

    @Override
    public Boolean evaluateBoolean() {
        return toString().toLowerCase().equals("true") ? true
                : toString().toLowerCase().equals("false") ? false
                : super.evaluateBoolean();
    }

    @Override
    public Double evaluateNumber() {
        return getLeftNumber();
    }

    @Override
    public Expression not() {
        return toString().toLowerCase().equals("true") ? leaf("false")
                : toString().toLowerCase().equals("false") ? leaf("true")
                : super.not();
    }

    @Override
    public Expression replace(Assignment condition) {
        return equals(condition.getLeft()) ? condition.getRightExpression() : this;
    }

    @Override
    public Expression replace(Assignment condition, Expression replacement) {
        return equals(condition.getLeft()) ? condition.getRightExpression() : this;
    }

    @Override
    public Expression append(Assignment condition, Expression replacement) {
        return equals(condition.getLeft()) ? condition.getRightExpression() : this;
    }
}
