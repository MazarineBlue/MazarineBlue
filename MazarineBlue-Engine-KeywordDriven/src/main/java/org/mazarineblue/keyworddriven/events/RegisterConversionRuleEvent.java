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
import java.util.function.Function;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.utilities.SerializableClonable;
import org.mazarineblue.utilities.TypeConvertor;

/**
 * An {@code RegisterConversionRuleEvent} is an event tot adds a conversion rule to
 * a running {@link Processor}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> The input type this rule can read
 * @param <R> The output type this rule can produce
 */
public class RegisterConversionRuleEvent<T, R>
        extends ConversionRuleEvent<T,R> {

    private static final long serialVersionUID = 1L;

    private transient Function<T, R> func;

    public RegisterConversionRuleEvent(Class<T> inputType, Class<R> outputType, Function<T, R> func) {
        super(inputType, outputType);
        this.func = func;
    }

    public void registerMethod(TypeConvertor convertor) {
        convertor.registerConversionFunction(inputType, outputType, func);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        func = ((RegisterConversionRuleEvent) other).func;
    }

    @Override
    public int hashCode() {
        return 59 * 59 * 7
                + 59 * Objects.hashCode(this.func)
                + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(this.func, ((RegisterConversionRuleEvent<?, ?>) obj).func);
    }
}
