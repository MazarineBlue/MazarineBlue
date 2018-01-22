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
import static java.util.Arrays.stream;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.exceptions.FileNotFoundException;
import org.mazarineblue.plugins.exceptions.FileNotSupportedException;
import org.mazarineblue.plugins.exceptions.FileUnreadableException;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

/**
 * {@code FeedService} provide support for loading feeds.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedService {

    /**
     * Tests if the specified feed can be processed by any of the feed plugins.
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
        } catch (java.io.FileNotFoundException ex) {
            throw new FileNotFoundException(file, ex);
        } catch (IOException ex) {
            throw new FileUnreadableException(file, ex);
        }
    }

    /**
     * Read the specified stream to find the names of the first sheet within.
     *
     * @param fs   the {@code FileSystem} to read from.
     * @param file the file to turn into a feed.
     * @return the names of all sheets found within the specified input stream,
     *         or an empty array when there no sheets available.
     *
     * @throws FileUnreadableException when an error occurred when trying to
     *                                 read the file.
     */
    public static String getFirstSheetName(FileSystem fs, File file) {
        String[] sheets = getSheetNames(fs, file);
        if (sheets.length == 0)
            throw new SheetNotFoundException(file);
        return sheets[0];
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
    public static String[] getSheetNames(FileSystem fs, File file) {
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
     * @throws FileNotFoundException     if the file was not on the filesystem.
     * @throws SheetNotFoundException    if the file has no sheets.
     * @throws FileUnreadableException   when an error occurred when trying to
     *                                   read the file.
     * @throws FileNotSupportedException when there is no support for the file.
     */
    public static Feed createFeed(FileSystem fs, File file) {
        FeedPlugin plugin = getPlugin(fs, file);
        String sheet = plugin.getFirstSheetName(fs, file);
        return createFeed(fs, file, sheet);
    }

    /**
     * Creates a feed using the available FeedPlugins.
     *
     * @param fs    the {@code FileSystem} to read from.
     * @param file  the file to turn into a feed.
     * @param sheet the sheet containing the instructions to read for the feed.
     * @return the found feed.
     *
     * @throws FileNotFoundException     if the file was not on the filesystem.
     * @throws SheetNotFoundException    if the file has no sheets.
     * @throws FileUnreadableException   when an error occurred when trying to
     *                                   read the file.
     * @throws FileNotSupportedException when there is no support for the file.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public static Feed createFeed(FileSystem fs, File file, String sheet) {
        try {
            FeedPlugin plugin = getPlugin(fs, file);
            if (!isSheetAvailable(plugin, fs.getInputStream(file), sheet))
                throw new SheetNotFoundException(file, sheet);
            return plugin.createFeed(fs.getInputStream(file), sheet);
        } catch (IOException ex) {
            throw new NeverThrownException();
        }
    }

    private static FeedPlugin getPlugin(FileSystem fs, File file) {
        if (!fs.exists(file))
            throw new FileNotFoundException(file);
        try {
            InputStream input = fs.getInputStream(file);
            String format = detectFormat(input, file.getName());
            FeedPlugin plugin = findPlugin(format);
            if (!plugin.canProcess(format))
                throw new FileNotSupportedException(file);
            return plugin;
        } catch (IOException ex) {
            throw new FileUnreadableException(file, ex);
        }
    }

    private static boolean isSheetAvailable(FeedPlugin plugin, InputStream input, String sheet) {
        String[] sheets = plugin.readSheetNames(input);
        if (sheet == null)
            return false;
        if (sheets.length == 0)
            return false;
        return sheet != null && sheets.length != 0 && stream(sheets).anyMatch(s -> s.equals(sheet));
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

    private FeedService() {
    }
}
