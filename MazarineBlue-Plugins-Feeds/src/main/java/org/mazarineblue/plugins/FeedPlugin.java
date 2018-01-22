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
package org.mazarineblue.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;

/**
 * {@code FeedPlugin} provides support for converting files into feeds.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface FeedPlugin
        extends Plugin {

    /**
     * Test if there is support for the specified file.
     *
     * @param mimeType the argument to test.
     * @return {@code true} if the format is supported.
     */
    public boolean canProcess(String mimeType);

    /**
     * Reads the specified file to find the name of the first sheet within the
     * file.
     *
     * @param fs   the filesystem to read the file from.
     * @param file the file to read the sheet name from.
     * @return the first sheet name within the file.
     *
     * @throws SheetNotFoundException if the file has no sheets.
     */
    public default String getFirstSheetName(FileSystem fs, File file) {
        try {
            return getFirstSheetName(fs.getInputStream(file));
        } catch (IOException ex) {
            throw new SheetNotFoundException(file);
        }
    }

    /**
     * Reads the specified stream to find the name of the first sheet within
     * the stream.
     *
     * @param input the stream to read for the sheet name.
     * @return the name of the first sheet found within the specified input
     *         stream, or an empty array when there no sheets available.
     *
     * @throws IOException when the feed could not find any sheets.
     */
    public default String getFirstSheetName(InputStream input)
            throws IOException {
        String[] sheets = readSheetNames(input);
        input.reset();
        if (sheets.length == 0)
            throw new IOException();
        return sheets[0];
    }

    /**
     * Reads the specified stream to find the names of the sheets within.
     *
     * @param input the stream to read for the sheet names.
     * @return the names of all sheets found within the specified input stream,
     *         or an empty array when there no sheets available.
     */
    public String[] readSheetNames(InputStream input);

    /**
     * Read the specified stream to create an {@code Feed}.
     *
     * @param input the stream to read for the sheet names.
     * @param sheet the name of the sheet to read.
     * @return the feed created from the specified input stream.
     */
    public Feed createFeed(InputStream input, String sheet);
}
