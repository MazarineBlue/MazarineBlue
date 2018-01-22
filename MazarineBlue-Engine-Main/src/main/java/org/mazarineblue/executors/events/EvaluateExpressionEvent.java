/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors.events;

import java.util.Objects;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.expressions.Expression;
import org.mazarineblue.utilities.SerializableClonable;

public class EvaluateExpressionEvent
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private Expression expression;
    private transient Object result;

    public EvaluateExpressionEvent(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "expression=" + expression + ", result=" + result;
    }

    @Override
    public String message() {
        return "expression=" + expression;
    }

    @Override
    public String responce() {
        return "result=" + result;
    }

    public void evaluate(VariableSource<Object> source) {
        result = expression.evaluate(source);
    }

    public Object getResult() {
        return result;
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T src) {
        super.copyTransient(src);
        expression = ((EvaluateExpressionEvent) src).expression;
        result = ((EvaluateExpressionEvent) src).result;
    }

    @Override
    public int hashCode() {
        return 41 * 41 * 5
                + 41 * Objects.hashCode(this.expression)
                + Objects.hashCode(this.result);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.expression, ((EvaluateExpressionEvent) obj).expression)
                && Objects.equals(this.result, ((EvaluateExpressionEvent) obj).result);
    }
}
