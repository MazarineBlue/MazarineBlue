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
package org.mazarineblue.test.datadriven;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ValidationContext {

    private Method method;
    private List<String> row;
    private String column;
    private String[] arguments;

    public ValidationContext() {
    }

    ValidationContext(ValidationContext other) {
        this.method = other.method;
        this.row = other.row;
        this.column = other.column;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValidationContext) {
            ValidationContext other = (ValidationContext) obj;
            return equalsMethod(other)
                    && equalsLineIdentifier(other)
                    && equalsColumn(other);
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in equals()">
    private boolean equalsMethod(ValidationContext other) {
        if (method == null)
            return other.method == null;
        return method.equals(other.method);
    }

    private boolean equalsLineIdentifier(ValidationContext other) {
        if (row == null)
            return other.row == null;
        return row.equals(other.row);
    }

    private boolean equalsColumn(ValidationContext other) {
        if (column == null)
            return other.column == null;
        return column.equals(other.column);
    }
    // </editor-fold">

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.method);
        hash = 43 * hash + Objects.hashCode(this.row);
        hash = 43 * hash + Objects.hashCode(this.column);
        return hash;
    }

    void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    void setRow(List<String> row) {
        this.row = row;
    }

    public List<String> getRow() {
        return row;
    }

    void setColumn(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    String[] getArguments() {
        return arguments;
    }

    public void setArguments(String... arguments) {
        this.arguments = arguments;
    }

    void reset() {
        this.arguments = null;
    }
}
