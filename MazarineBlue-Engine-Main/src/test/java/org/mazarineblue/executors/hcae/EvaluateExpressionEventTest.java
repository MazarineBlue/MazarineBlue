/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors.hcae;

import org.junit.Assert;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.parser.expressions.Expression;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/*
 * ********************************************************************** *
 * Libraries *
 * ********************************************************* ,,^..^,, ***
 */
 /*
 * ********************************************************************** *
 * Events *
 * ********************************************************* ,,^..^,, ***
 */
@SuppressWarnings(value = "PublicInnerClass")
public class EvaluateExpressionEventTest
        extends TestHashCodeAndEquals<Event> {

    @Override
    protected Event getObject() {
        EvaluateExpressionEvent e = new EvaluateExpressionEvent(Expression.leaf("1"));
        e.evaluate(null);
        return e;
    }

    @Override
    protected Event getDifferentObject() {
        EvaluateExpressionEvent e = new EvaluateExpressionEvent(Expression.leaf("2"));
        e.evaluate(null);
        return e;
    }

    public void hashCode_DifferentExpressions() {
        int a = new EvaluateExpressionEvent(Expression.leaf("1")).hashCode();
        int b = new EvaluateExpressionEvent(Expression.leaf("2")).hashCode();
        Assert.assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentExpressions() {
        Event a = new EvaluateExpressionEvent(Expression.leaf("1"));
        Event b = new EvaluateExpressionEvent(Expression.leaf("2"));
        Assert.assertFalse(a.equals(b));
    }
}
