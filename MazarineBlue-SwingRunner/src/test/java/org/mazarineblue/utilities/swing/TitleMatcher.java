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
package org.mazarineblue.utilities.swing;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TitleMatcher<T extends Window>
        implements SwingUtil.Matcher<T> {

    private final String title;
    private final Class<T> type;

    public TitleMatcher(String title, Class<T> type) {
        this.title = title;
        this.type = type;
    }

    @Override
    public boolean childMatches(Component child) {
        if (!type.isAssignableFrom(child.getClass()))
            return false;

        try {
            Class<?>[] parameterTypes = new Class<?>[0];
            Class<?> returnType = String.class;
            for (Method m : child.getClass().getMethods())
                if (methodMatches(m, "getTitle", parameterTypes, returnType))
                    return title.equals((String) m.invoke(child));
            return false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean methodMatches(Method m, String name, Class<?>[] parameterTypes, Class<?> returnType) {
        return m.getName().equals(name)
                && Arrays.equals(m.getParameterTypes(), parameterTypes)
                && m.getReturnType().isAssignableFrom(returnType);
    }
}
