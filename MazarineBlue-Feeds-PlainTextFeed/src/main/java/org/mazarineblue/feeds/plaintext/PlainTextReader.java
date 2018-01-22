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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import static java.util.Arrays.copyOfRange;
import org.mazarineblue.utilities.exceptions.UnknownIssueException;

/**
 * A {@code PlainTextReader} is a reader, which turns a plain text file into a
 * {@link PlainTextTable}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class PlainTextReader {

    private static final int BUFFER = 4096;
    private final PlainTextTable table;
    private final BufferedReader input;

    PlainTextReader(Reader input) {
        this.input = new BufferedReader(input);
        this.table = new PlainTextTable();
    }

    PlainTextTable readTable() {
        while (hasNext())
            table.addLine(next());
        return table;
    }

    private boolean hasNext() {
        try {
            input.mark(BUFFER);
            String[] columns = readColumns();
            input.reset();
            return columns.length != 0;
        } catch (IOException ex) {
            throw new UnknownIssueException(ex);
        }
    }

    private String[] next() {
        try {
            return readColumns();
        } catch (IOException ex) {
            throw new UnknownIssueException(ex);
        }
    }

    private String[] readColumns()
            throws IOException {
        String line = readLine();
        String[] columns = splitColumns(line, "\\|");
        return trimColumns(columns);
    }

    private String readLine()
            throws IOException {
        String line = input.readLine();
        return line == null ? "" : line;
    }

    private String[] splitColumns(String line, String regex) {
        String[] split = line.split(regex);
        return split.length == 0 ? split : copyOfRange(split, 1, split.length);
    }

    private String[] trimColumns(String[] columns) {
        String[] output = new String[columns.length];
        for (int i = 0; i < columns.length; ++i)
            output[i] = columns[i].trim();
        return output;
    }
}
