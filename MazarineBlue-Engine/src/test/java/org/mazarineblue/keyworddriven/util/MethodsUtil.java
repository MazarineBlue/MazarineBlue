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
package org.mazarineblue.keyworddriven.util;

import java.lang.reflect.Method;

public class MethodsUtil {

    private MethodsUtil() {
    }

    public static Method getPublicMethod() {
        return getMethod("method");
    }

    //<editor-fold defaultstate="collapsed" desc="method()">
    public void method() {
    }
    //</editor-fold>

    public static Method getPublicMethodWithOtherName() {
        return getMethod("otherMethod");
    }

    //<editor-fold defaultstate="collapsed" desc="otherMethod()">
    public void otherMethod() {
    }
    //</editor-fold>

    public static Method getPublicMethodWithReturnValue() {
        return getMethod("methodWithReturnValue");
    }

    //<editor-fold defaultstate="collapsed" desc="methodWithReturnValue()">
    public Object methodWithReturnValue() {
        return "";
    }
    //</editor-fold>

    public static Method getPublicMethodWithOtherParameterTypes() {
        return getMethod("methodWithParameter");
    }

    //<editor-fold defaultstate="collapsed" desc="methodWithParameter()">
    public void methodWithParameter(Object param) {
    }
    //</editor-fold>

    public static Method getPrivateMethod() {
        return getMethod("privateMethod");
    }

    //<editor-fold defaultstate="collapsed" desc="privateMethod()">
    private void privateMethod() {
    }
    //</editor-fold>

    private static Method getMethod(String name) {
        for (Method m : MethodsUtil.class.getDeclaredMethods())
            if (m.getName().equals(name))
                return m;
        return null;
    }
}
