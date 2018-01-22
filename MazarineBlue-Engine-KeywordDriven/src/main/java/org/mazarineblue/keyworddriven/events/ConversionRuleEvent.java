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
package org.mazarineblue.keyworddriven.events;

import java.util.Objects;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * An {@code RegisterConversionRuleEvent} is an event tot adds a conversion rule to
 * a running {@link Processor}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> The input type this rule can read
 * @param <R> The output type this rule can produce
 */
public class ConversionRuleEvent<T, R>
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    protected Class<T> inputType;
    protected Class<R> outputType;

    public ConversionRuleEvent(Class<T> inputType, Class<R> outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    @Override
    public String toString() {
        return "inputType=" + inputType.getSimpleName()
                + ", outputType=" + outputType.getSimpleName();
    }

    @Override
    public String message() {
        return "inputType=" + inputType.getName()
                + ", outputType=" + outputType.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        inputType = ((ConversionRuleEvent) other).inputType;
        outputType = ((ConversionRuleEvent) other).outputType;
    }

    @Override
    public int hashCode() {
        return 61 * 61 * 5
                + 61 * Objects.hashCode(this.inputType)
                + Objects.hashCode(this.outputType);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.inputType, ((ConversionRuleEvent<?, ?>) obj).inputType)
                && Objects.equals(this.outputType, ((ConversionRuleEvent<?, ?>) obj).outputType);
    }
}
