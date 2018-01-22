/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.feeds.plaintext;

import static java.lang.System.arraycopy;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@code PlainTextTable} contains a table, where each line is represented as
 * an array of columns
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PlainTextTable {

    private List<String[]> lines = lines = new ArrayList<>();

    @Override
    public String toString() {
        return lines.stream().map(line -> toString(line) + "\\n ").reduce("", String::concat);
    }

    private String toString(String[] columns) {
        return stream(columns).map(column -> column + " | ").reduce("| ", String::concat).trim();
    }

    /**
     * Adds a line to the table.
     *
     * @param keyword   the first column, containing the keyword
     * @param arguments the remaining columns, containing the arguments
     * @return
     */
    public boolean addLine(String keyword, String... arguments) {
        String[] columns = new String[arguments.length + 1];
        columns[0] = keyword;
        arraycopy(arguments, 0, columns, 1, arguments.length);
        return lines.add(columns);
    }

    boolean addLine(String[] columns) {
        return lines.add(columns);
    }

    Iterator<String[]> iterator() {
        if (lines instanceof ArrayList)
            lines = unmodifiableList(lines);
        return lines.iterator();
    }
}
