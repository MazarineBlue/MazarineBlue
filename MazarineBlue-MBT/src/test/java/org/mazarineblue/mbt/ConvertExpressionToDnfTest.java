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
package org.mazarineblue.mbt;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.mbt.expressions.Expression;
import static org.mazarineblue.mbt.expressions.Expression.and;
import static org.mazarineblue.mbt.expressions.Expression.not;
import static org.mazarineblue.mbt.expressions.Expression.or;

public class ConvertExpressionToDnfTest {

    @Test // !!p <=> p
    public void convert_NotNotPorQ_PorQ() {
        Expression expected = or("p", "q");
        Expression e = not(not(expected));
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }

    @Test // !(p && q) <==> !p || !q
    public void convert_NotPandQ_NotPorNotQ() {
        Expression expected = or(not("p"), not("q"));
        Expression e = not(and("p", "q"));
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }

    @Test // !(p || q) <==> !p && !q
    public void convert_NotPorQ_NotPandNotQ() {
        Expression expected = and(not("p"), not("q"));
        Expression e = not(or("p", "q"));
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }

    @Test // (p || q) && r <=> p && r || q && r
    public void convert_PorQandR_PandRorQandR() {
        Expression expected = or(and("p", "r"), and("q", "r"));
        Expression e = and(or("p", "q"), "r");
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }

    @Test // p && (q || r) <=> p && q || p && r
    public void convert_PandQorR_PandQorPandR() {
        Expression expected = or(and("p", "q"), and("p", "r"));
        Expression e = and("p", or("q", "r"));
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }

    @Test // (p || q) && (r || s) <=> p && r || p && s || q && r || q && s
    public void convert_PorQandRorS_PandQorPandR() {
        Expression t1 = and("p", "r");
        Expression t2 = and("p", "s");
        Expression t3 = and("q", "r");
        Expression t4 = and("q", "s");
        Expression expected = or(or(t1, t2), or(t3, t4));
        Expression e = and(or("p", "q"), or("r", "s"));
        Expression actual = e.convertToDNF();
        assertEquals(expected, actual);
    }
}
