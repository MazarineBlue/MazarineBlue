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
package org.mazarineblue.webdriver.keywords;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Date;
import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.UnwritableFileException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.pictures.BufferedImagePicture;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.pictures.compounders.EqualCompounder;
import org.mazarineblue.pictures.compounders.comperator.IgnoreAlphaComperator;
import org.mazarineblue.util.Timestamp;
import org.mazarineblue.webdriver.WebToolkit;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ScreenshotLibrary
        extends AbstractWebToolkitLibrary {

    ScreenshotLibrary(Library library, WebToolkit toolkit,
                      TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Validate screenshot")
    @Parameters(min = 1)
    public void validateScreenshot(String inputFile, Integer width,
                                   Integer height)
            throws IOException {
        if (width == null || width <= 0 || height == null || height <= 0)
            throw new org.mazarineblue.webdriver.exceptions.WrongScreenshotFormatException(
                    width, height);

        java.awt.Dimension dimension
                = (width == null || height == null)
                        ? null : new java.awt.Dimension(width, height);
        Path actualPath = getOutputPath(inputFile, null);
        Path expectedPath = new File(inputFile).toPath();
        Picture actualScreenshot = toolkit.screenshotInstructions().takeScreenshot();
        writeScreenshot(actualScreenshot, actualPath, ScreenshotType.WARNING);
        Picture expectedScreenshot = readScreenshot(expectedPath,
                                                       ScreenshotType.EXCEPTION);

        /*
         * The actualScreenshot and expectedScreenshot are available at this
         * point because the first is an taken screenshot and when the latter
         * can not be read an exception is thrown.
         */
        try {
            EqualCompounder.Result compare = expectedScreenshot.compare(actualScreenshot);
            boolean match = compare == EqualCompounder.Result.EQUAL;
            publish(new SetStatusEvent(match));
            if (match)
                return;

            if (dimension != null) {
                actualScreenshot = actualScreenshot.clip(dimension);
                expectedScreenshot = expectedScreenshot.clip(dimension);
            }
            createDiff(inputFile, actualScreenshot, expectedScreenshot);
            createSame(inputFile, actualScreenshot, expectedScreenshot);
            message(errorMessage(compare) + ": " + inputFile,
                    ScreenshotType.ERROR, null);
        } catch (IOException ex) {
            String format = "Comparing could not be done with %s and %s";
            String msg = String.format(format, expectedPath, actualPath);
            message(msg, ScreenshotType.EXCEPTION, ex);
        }
    }

    private String errorMessage(EqualCompounder.Result compare) {
        String error = "Validation schreenshot failed";
        switch (compare) {
            case DIFF_CONTENT:
                error += ", due to content";
                break;
            case DIFF_HEIGHT:
                error += ", due to height";
                break;
            case DIFF_WIDTH:
                error += ", due to width";
                break;
            case DIFF_WIDTH_AND_HEIGHT:
                error += ", due to width and height";
                break;
            case FAIL:
                break;
        }
        return error;
    }

    // <editor-fold defaultstate="collapsed" desc="First used in validateScreenshot()">
    private Path getOutputPath(String inputPath, String post) {
        Date date = executor().getStartDate();
        String folder = documentMediator().getDefaultFolder(date);
        String path = processTimestamp(inputPath, post);
        File file = new File(folder, path);
        return file.toPath();
    }

    private String processTimestamp(String inputFile, String post) {
        Timestamp timestamp = documentMediator().getDefaultTimestamp();
        if (inputFile == null)
            return timestamp.getTimestamp();
        int pos = inputFile.lastIndexOf('.');
        String outputFile;
        if (pos > 0) {
            outputFile = inputFile.substring(0, pos) + "-";
            outputFile += getTimestamp(timestamp, post);
            outputFile += inputFile.substring(pos);
        } else if (pos == 0) {
            outputFile = getTimestamp(timestamp, post);
            outputFile += inputFile;
        } else {
            outputFile = inputFile + "-";
            outputFile += getTimestamp(timestamp, post);
        }
        return outputFile;
    }

    private String getTimestamp(Timestamp timestamp, String post) {
        String str = timestamp.getTimestamp();
        if (post != null)
            str += "-" + post;
        return str;
    }

    private void createDiff(String inputFile, Picture actual, Picture expected) {
        try {
            Path diffPath = getOutputPath(inputFile, "diff");
            Picture diffScreenshot = actual.diff(expected, new IgnoreAlphaComperator());
            writeScreenshot(diffScreenshot, diffPath, ScreenshotType.WARNING);
        } catch (Exception ex) {
            // Best effort
        }
    }

    private void createSame(String inputFile, Picture actual, Picture expected) {
        try {
            Path samePath = getOutputPath(inputFile, "same");
            Picture sameScreenshot = actual.same(expected, new IgnoreAlphaComperator());
            writeScreenshot(sameScreenshot, samePath, ScreenshotType.WARNING);
        } catch (Exception ex) {
            // Best effort
        }
    }

    private enum ScreenshotType {

        EXCEPTION,
        ERROR,
        WARNING,
    }

    private void writeScreenshot(Picture screenshot, Path path,
                                 ScreenshotType type) {
        try (OutputStream out = documentMediator().getOutputStream(path)) {
            screenshot.write(out);
        } catch (IOException ex) {
            message("Could not write to file: " + path, type, ex);
        }
    }

    private Picture readScreenshot(Path path, ScreenshotType type) {
        try (InputStream input = documentMediator().getInputStream(path)) {
            return new BufferedImagePicture(input);
        } catch (IOException ex) {
            message("Could not write to file: " + path, type, ex);
        }
        return null;

    }

    private void message(String message, ScreenshotType type, Throwable cause) {
        switch (type) {
            default:
                throw new UnsupportedOperationException("Not supported type!");
            case EXCEPTION:
                if (cause == null)
                    throw new UnwritableFileException(message);
                else
                    throw new UnwritableFileException(message, cause);
            case ERROR:
                log().error(message);
                break;
            case WARNING:
                log().warning(message);
        }
    }
    // </editor-fold>
}
