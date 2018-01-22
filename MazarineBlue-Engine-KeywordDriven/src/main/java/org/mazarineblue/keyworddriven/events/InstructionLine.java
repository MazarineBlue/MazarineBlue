/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.utilities.ArgumentList;

/**
 * An {@code InstructionLine} is a container for a {@link Path} and arguments.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Path
 */
class InstructionLine
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Path path;
    private final ArgumentList parameters;

    InstructionLine(Path path, Object... parameters) {
        this.path = path;
        this.parameters = new ArgumentList(parameters);
    }

    @Override
    public String toString() {
        return parameters.getArguments().length == 0 ? path.toString() : path + ", " + parameters;
    }

    String getPath() {
        return path.toString();
    }

    String getNamespace() {
        return path.getNamespace();
    }

    String getKeyword() {
        return path.getKeyword();
    }

    protected ArgumentList getArgumentList() {
        return parameters;
    }

    int indexOf(Predicate<Object> condition) {
        return parameters.indexOf(condition);
    }

    int lastIndexOf(Predicate<Object> condition) {
        return parameters.lastIndexOf(condition);
    }

    Object getArgument(int index) {
        return parameters.getArgument(index);
    }

    Object[] getArguments() {
        return parameters.getArguments();
    }

    Object[] getArguments(int start) {
        return parameters.getArguments(start);
    }

    Object[] getArguments(int start, int stop) {
        return parameters.getArguments(start, stop);
    }

    void setArgument(int index, Object argument) {
        parameters.setArgument(index, argument);
    }

    void setArguments(Object... arguments) {
        parameters.setArguments(arguments);
    }

    @Override
    public int hashCode() {
        return 7 * 83 * 83
                + 83 * Objects.hashCode(path)
                + Objects.hashCode(parameters);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.path, ((InstructionLine) obj).path)
                && Objects.equals(this.parameters, ((InstructionLine) obj).parameters);
    }
}
