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
package org.mazarineblue.keyworddriven;

import java.util.Arrays;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InstructionLine {

    private final String lineIdentifier;
    private final String namespace;
    private final String keyword;
    private final Object[] parameter;

    public InstructionLine(String lineIdentifier, String namespace, String keyword, Object... parameters) {
        this.lineIdentifier = lineIdentifier.trim();
        this.namespace = namespace.trim();
        this.keyword = keyword.trim();
        this.parameter = trimEmptyParameters(parameters);
    }

    private Object[] trimEmptyParameters(Object[] parameters) {
        int n = getLength(parameters);
        Object[] output = new Object[n];
        for (int i = 0; i < n; ++i)
            if (parameters[i] instanceof String)
                output[i] = ((String) parameters[i]).trim();
            else
                output[i] = parameters[i];
        return output;
    }

    private int getLength(Object[] parameters) {
        int n = 0;
        for (int i = 0; i < parameters.length; ++i)
            if (parameters[i].toString().isEmpty() == false)
                n = i + 1;
        return n;
    }

    @Override
    public String toString() {
        String str;
        if (isPathEmpty())
            str = parameter.length > 0 ? "Comment" : "";
        else
            str = namespace + '.' + keyword;

        for (int i = 0; i < parameter.length; ++i) {
            str += i == 0 ? " (" : ", ";
            str += parameter[i].toString();
        }
        if (parameter.length != 0)
            str += ")";
        return str;
    }

    public boolean isEmpty() {
        return isPathEmpty() && parameter.length <= 0;
    }

    public String getLineIdentifier() {
        return lineIdentifier;
    }

    public boolean isComment() {
        if (parameter.length <= 0)
            return false;
        return isPathEmpty();
    }

    public final boolean isPathEmpty() {
        return keyword.isEmpty() && namespace.isEmpty();
    }

    public String getPath() {
        return namespace + '.' + keyword;
    }

    public static String getNamespace(String path) {
        int pos = path.lastIndexOf('.');
        return pos < 1 ? "" : path.substring(0, pos);
    }

    public static String getKeyword(String path) {
        int pos = path.lastIndexOf('.');
        return pos < 0 ? path : path.substring(pos + 1);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKeyword() {
        return keyword;
    }

    public int countParameters() {
        return parameter.length;
    }

    public Object getParameter(int index) {
        return parameter[index];
    }

    public Object[] getParameters() {
        return Arrays.copyOf(parameter, parameter.length);
    }

    public InstructionLine cloneAndReplaceParameters(Object[] convertedParameters) {
        String k = isComment() ? "Comment" : keyword;
        return new InstructionLine(lineIdentifier, namespace, k, convertedParameters);
    }
}
