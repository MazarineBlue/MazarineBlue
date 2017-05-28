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
package org.mazarineblue.plugins;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;

public class FeedService {

    /**
     * Tests if the specified can be processed by any of the feed plugins.
     *
     * @param fs   the {@code FileSystem} to read from.
     * @param file the file to turn into a feed.
     * @return {@code true} if the specified file can be processed by one of
     *         feed plugins.
     */
    public static boolean canProcess(FileSystem fs, File file) {
        try {
            InputStream input = fs.getInputStream(file);
            String format = detectFormat(input, file.getName());
            FeedPlugin plugin = findPlugin(format);
            return plugin.canProcess(format);
        } catch (IOException ex) {
            throw new FileUnreadableException(file, ex);
        }
    }

    /**
     * Read the specified stream to find the names of the sheets within.
     *
     * @param fs   the {@code FileSystem} to read from.
     * @param file the file to turn into a feed.
     * @return the names of all sheets found within the specified input stream,
     *         or an empty array when there no sheets available.
     *
     * @throws FileUnreadableException when an error occurred when trying to
     *                                 read the file.
     */
    public static String[] readSheetNames(FileSystem fs, File file) {
        try {
            InputStream input = fs.getInputStream(file);
            String format = detectFormat(input, file.getName());
            FeedPlugin plugin = findPlugin(format);
            if (!plugin.canProcess(format))
                throw new FileNotSupportedException(file);
            return plugin.readSheetNames(input);
        } catch (java.io.FileNotFoundException ex) {
            throw new FileNotFoundException(file, ex);
        } catch (IOException ex) {
            throw new FileUnreadableException(file, ex);
        }
    }

    /**
     * Creates a feed using the available FeedPlugins.
     *
     * @param fs   the {@code FileSystem} to read from.
     * @param file the file to turn into a feed.
     * @return the found feed.
     *
     * @throws FileUnreadableException   when an error occurred when trying to
     *                                   read the file.
     * @throws FileNotSupportedException when there is no support for the file.
     */
    public static Feed createFeed(FileSystem fs, File file) {
        return createFeed(fs, file, null);
    }

    /**
     * Creates a feed using the available FeedPlugins.
     *
     * @param fs    the {@code FileSystem} to read from.
     * @param file  the file to turn into a feed.
     * @param sheet the sheet containing the instructions to read for the feed.
     * @return the found feed.
     *
     * @throws FileUnreadableException   when an error occurred when trying to
     *                                   read the file.
     * @throws FileNotSupportedException when there is no support for the file.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public static Feed createFeed(FileSystem fs, File file, String sheet) {
        try {
            InputStream input = fs.getInputStream(file);
            String format = detectFormat(input, file.getName());
            FeedPlugin plugin = findPlugin(format);
            if (!plugin.canProcess(format))
                throw new FileNotSupportedException(file);
            if (sheet == null)
                sheet = getFirstSheetName(plugin, input);
            return plugin.createFeed(input, sheet);
        } catch (java.io.FileNotFoundException ex) {
            throw new FileNotFoundException(file, ex);
        } catch (IOException ex) {
            throw new FileUnreadableException(file, ex);
        }
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private static String detectFormat(InputStream input, String filename)
            throws IOException {
        Detector detector = new AutoDetectParser().getDetector();
        Metadata md = new Metadata();
        md.add(Metadata.RESOURCE_NAME_KEY, filename);
        if (!input.markSupported())
            input = new BufferedInputStream(input);
        return detector.detect(input, md).toString();
    }

    private static FeedPlugin findPlugin(String format) {
        return PluginLoader.getInstance().getPlugins(FeedPlugin.class).stream()
                .filter(plugin -> plugin.canProcess(format))
                .findAny()
                .orElse(new DefaultFeedPlugin());
    }

    private static String getFirstSheetName(FeedPlugin plugin, InputStream input)
            throws IOException {
        String[] sheets = plugin.readSheetNames(input);
        input.reset();
        if (sheets.length == 0)
            throw new IOException();
        return sheets[0];
    }

    private FeedService() {
    }
}
