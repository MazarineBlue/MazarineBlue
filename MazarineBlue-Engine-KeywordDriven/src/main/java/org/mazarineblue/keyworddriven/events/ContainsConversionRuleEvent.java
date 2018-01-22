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

import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.utilities.TypeConvertor;

/**
 * An {@code RegisterConversionRuleEvent} is an event tot adds a conversion rule to
 * a running {@link Processor}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> The input type this rule can read
 * @param <R> The output type this rule can produce
 */
public class ContainsConversionRuleEvent<T, R>
        extends ConversionRuleEvent<T,R> {

    private static final long serialVersionUID = 1L;

    private boolean result;

    public ContainsConversionRuleEvent(Class<T> inputType, Class<R> outputType) {
        super(inputType, outputType);
    }

    public void setConversionMethod(TypeConvertor convertor) {
        result = convertor.hasConversionMethod(inputType, outputType);
    }

    public boolean isConversionMethodAvailable() {
        return result;
    }
}
