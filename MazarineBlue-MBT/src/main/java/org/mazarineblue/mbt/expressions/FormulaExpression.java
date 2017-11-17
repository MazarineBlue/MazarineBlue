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

import org.mazarineblue.mbt.Pair;

public abstract class FormulaExpression
        extends Expression {

    private static enum OPTION {
        LEFT,
        RIGHT,
        NONE
    }

    FormulaExpression(Object left, String operator, Object right) {
        super(left, operator, right);
    }

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public Pair<?> simplify(Expression other) {
        Object o = other.getLeft();
        switch (selectNode()) {
            case LEFT:
                return new Pair<>(doSimplify(getLeft(), o).simplify(), getRight());
            case RIGHT:
                return new Pair<>(getLeft(), doSimplify(o, getRight()).simplify());
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private OPTION selectNode() {
        if (getLeft() instanceof Leaf && getRight() instanceof Leaf)
            try {
                getLeftNumber();
                return OPTION.LEFT;
            } catch (RuntimeException ex) {
                return OPTION.RIGHT;
            }
        if (getLeft() instanceof Leaf)
            return OPTION.LEFT;
        if (getRight() instanceof Leaf)
            return OPTION.RIGHT;
        return OPTION.NONE;
    }

    protected abstract Expression doSimplify(Object left, Object right);
}
