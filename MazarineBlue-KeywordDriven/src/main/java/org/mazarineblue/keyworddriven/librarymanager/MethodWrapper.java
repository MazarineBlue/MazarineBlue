/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.keyworddriven.librarymanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.ConsumableException;
import org.mazarineblue.keyworddriven.exceptions.ToFewParametersException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class MethodWrapper {

    private final Method method;
    private final Parameters parameters;

    MethodWrapper(Method method, String path) {
        this.method = method;
        this.parameters = method.getAnnotation(Parameters.class);
    }

    public void check(Object[] parameters) {
        if (parameters == null)
            throw new IllegalArgumentException("Parameters required");
        checkParamtersSize(parameters);
        // @TODO Implement a better check
        // checkParameterTypes(parameters);
    }

    private void checkParamtersSize(Object[] parameters) {
        int min = this.parameters == null ? 0 : this.parameters.min();
        int max = this.parameters == null ? parameters.length : this.parameters.max();
        int found = method.getParameterTypes().length;
        if (parameters.length < min || max < found)
            throw new ToFewParametersException(min, max, found);
    }

    private void checkParameterTypes(Object[] parameters) {
        Class<?>[] expectedTypes = method.getParameterTypes();
        Object[] arr = ConvertingObjectsFacade.getInstance().convert(parameters, expectedTypes);
        arr = convert(arr, expectedTypes);
        for (int i = 0; i < arr.length; ++i)
            if (expectedTypes[i].equals(arr[i].getClass()) == false)
                throw new ConsumableException("Invalid parameter at index:" + i);
    }

    private Object[] convert(Object[] input, Class<?>[] expectedTypes) {
        if (expectedTypes.length == 0)
            return new Object[0];

        Object[] output = new Object[expectedTypes.length];

        int last = expectedTypes.length - 1;
        for (int i = 0; i < last; ++i)
            output[i] = input[i];

        if (expectedTypes[last].equals(String[].class))
            output[last] = convertToStrings(getRemainer(input));
        else if (expectedTypes[last].equals(Object[].class))
            output[last] = getRemainer(input);
        else
            output[last] = input[last];
        return output;
    }

    private Object[] getRemainer(Object[] parameters) {
        Class<?>[] expectedTypes = method.getParameterTypes();
        int m = parameters.length - expectedTypes.length + 1;
        int offset = parameters.length - m;
        Object[] arr = new Object[m];
        for (int i = 0; i < m; ++i)
            arr[i] = parameters[i + offset];
        return arr;
    }

    private String[] convertToStrings(Object[] input) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; ++i)
            if (input[i] instanceof String)
                output[i] = (String) input[i];
            else
                return null;
        return output;
    }

    public Object invoke(Object obj, Object[] parameters)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        parameters = clipParameters(parameters);
        Class<?>[] expectedTypes = getParameterTypes();
        Object[] arr = ConvertingObjectsFacade.getInstance().convert(parameters, expectedTypes);
        arr = convert(arr, expectedTypes);
        return method.invoke(obj, arr);
    }

    private Object[] clipParameters(Object[] parameters) {
        if (this.parameters != null && parameters.length > this.parameters.max())
            parameters = Arrays.copyOf(parameters, this.parameters.max());
        return parameters;
    }

    private Class<?>[] getParameterTypes() {
        Class<?>[] expectedTypes = method.getParameterTypes();
        expectedTypes = ConvertingClassesFacade.getInstance().convert(
                expectedTypes);
        return expectedTypes;
    }
}
