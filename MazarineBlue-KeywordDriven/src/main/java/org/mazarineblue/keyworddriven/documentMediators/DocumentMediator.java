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
package org.mazarineblue.keyworddriven.documentMediators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Date;
import org.mazarineblue.util.Timestamp;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public interface DocumentMediator {

    default Timestamp getDefaultTimestamp() {
        return Timestamp.getDefaultInstance("yyyy-MM-dd_HH-mm-ss");
    }

    default String getDefaultFolder(Date date) {
        return getDefaultTimestamp().getTimestamp(date);
    }

    public String getInputLocation();

    public InputStream getInputStream()
            throws IOException;

    public InputStream getInputStream(Path path)
            throws IOException;

    default void writeLogOutput(String directory, String dummyName, String input)
            throws IOException {
        if (input == null)
            return;
        try (OutputStream output = getLogOutputStream(directory, dummyName)) {
            output.write(input.getBytes());
        }
    }

    default void writeReportOutput(String folder, String reportName, String input)
            throws IOException {
        if (input == null)
            return;
        try (OutputStream output = getReportOutputStream(folder, reportName)) {
            output.write(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    default void writeOutput(Path path, String input)
            throws IOException {
        if (input == null)
            return;
        try (OutputStream output = getOutputStream(path)) {
            output.write(input.getBytes());
        }
    }

    public OutputStream getLogOutputStream(String folder, String dummyName)
            throws IOException;

    public OutputStream getReportOutputStream(String folder, String reportName)
            throws IOException;

    public OutputStream getOutputStream(Path path)
            throws IOException;
}
