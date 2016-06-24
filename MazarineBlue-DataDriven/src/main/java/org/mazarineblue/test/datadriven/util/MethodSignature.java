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
package org.mazarineblue.test.datadriven.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodParameterTypesException;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodPostfixException;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodReturnTypeException;

public class MethodSignature {

    private final String expectedPostfix;
    private Class[] expectedAnnotationTypes = new Class[0];
    private Class expectedReturnType = void.class;
    private Class[] expectedParameterTypes = new Class[0];

    public MethodSignature() {
        this("");
    }

    public MethodSignature(String postfix) {
        this.expectedPostfix = postfix;
    }

    public String getPostfix() {
        return expectedPostfix;
    }

    public void setAnnotationTypes(Class... annotationTypes) {
        this.expectedAnnotationTypes = Arrays.copyOf(annotationTypes,
                                                     annotationTypes.length);
    }

    public void setReturnType(Class returnType) {
        this.expectedReturnType = returnType;
    }

    public void setParameterTypes(Class... parameterTypes) {
        this.expectedParameterTypes = Arrays.copyOf(parameterTypes,
                                                    parameterTypes.length);
    }

    public boolean isMethod(Method method)
            throws IllegalMethodException {
        String name = method.getName();
        if (equalsAnnotations(method) == false)
            return false;
        if (name.endsWith(expectedPostfix) == false)
            throw new IllegalMethodPostfixException(method, this);
        if (equalsParameterTypes(method) == false)
            throw new IllegalMethodParameterTypesException(method, this);
        if (equalsReturnTypes(method) == false)
            throw new IllegalMethodReturnTypeException(method, this);
        return true;
    }

    private boolean equalsAnnotations(Method method) {
        boolean[] flags = createFlags();
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annocation : annotations) {
            Class actual = annocation.annotationType();
            for (int i = 0; i < expectedAnnotationTypes.length; ++i)
                if (expectedAnnotationTypes[i].equals(actual))
                    flags[i] = true;
        }
        return andFlags(flags);
    }

    private boolean[] createFlags() {
        boolean[] flags = new boolean[expectedAnnotationTypes.length];
        for (int i = 0; i < flags.length; ++i)
            flags[i] = false;
        return flags;
    }

    private boolean andFlags(boolean[] flags) {
        for (boolean flag : flags)
            if (flag == false)
                return false;
        return true;
    }

    private boolean equalsParameterTypes(Method method) {
        return Arrays.equals(method.getParameterTypes(), expectedParameterTypes);
    }

    private boolean equalsReturnTypes(Method method) {
        Class<?> actualReturnType = method.getReturnType();
        return expectedReturnType == actualReturnType;
    }
}
