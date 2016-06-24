/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue.filesystem.keywords;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.util.Timestamp;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class FileSystemLibrary
        extends Library {

    public FileSystemLibrary() {
        super("org.mazarineblue.filesystem");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @Keyword("Delete file")
    @Parameters(min = 1)
    public void deleteFile(String path)
            throws IOException {
        File file = new File(path);
        Files.delete(file.toPath());
    }

    @Keyword("Mkdir")
    @Parameters(min = 1)
    public void mkdir(String path)
            throws IOException {
        File file = new File(path);
        file.mkdir();
    }

    @Keyword("Mkdirs")
    @Parameters(min = 1)
    public void mkdirs(String path)
            throws IOException {
        File file = new File(path);
        file.mkdirs();
    }

    @Keyword("Copy file")
    @Parameters(min = 1)
    public void copyFile(String source, String destination)
            throws IOException {
        Path sourcePath = new File(source).toPath();
        Path destinationPath = new File(destination).toPath();
        Files.copy(sourcePath, destinationPath, null);
    }

    @Keyword("Move file")
    @Parameters(min = 1)
    public void moveFile(String source, String destination)
            throws IOException {
        Path sourcePath = new File(source).toPath();
        Path destinationPath = new File(destination).toPath();
        Files.move(sourcePath, destinationPath, null);
    }

    @Keyword("Validate file exists")
    @Parameters(min = 1)
    public void validateFileExists(String path) {
        validateFileExists(path, true);
    }

    @Keyword("Validate file exists not")
    @Parameters(min = 1)
    public void validateFileExistsNot(String path) {
        validateFileExists(path, false);
    }

    private void validateFileExists(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.exists();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s was %s found";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Validate file executable")
    @Parameters(min = 1)
    public void validateFileExecutable(String path) {
        validateFileExecutable(path, true);
    }

    @Keyword("Validate file not executable")
    @Parameters(min = 1)
    public void validateFileNotExecutable(String path) {
        validateFileExecutable(path, false);
    }

    private void validateFileExecutable(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.canExecute();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s could %s be executed";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Validate file readable")
    @Parameters(min = 1)
    public void validateFileReadable(String path) {
        validateFileReadable(path, true);
    }

    @Keyword("Validate file not readable")
    @Parameters(min = 1)
    public void validateFileNotReadable(String path) {
        validateFileReadable(path, false);
    }

    private void validateFileReadable(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.canRead();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s could %s be read";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Validate file writable")
    @Parameters(min = 1)
    public void validateFileWritable(String path) {
        validateFileWritable(path, true);
    }

    @Keyword("Validate file not writable")
    @Parameters(min = 1)
    public void validateFileNotWritable(String path) {
        validateFileWritable(path, false);
    }

    private void validateFileWritable(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.canWrite();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s could %s be read";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Copy free space")
    @Parameters(min = 2)
    public void copyFreeSpace(String variableName, String path) {
        File file = new File(path);
        long freeSpace = file.getFreeSpace();
        blackboard().setData(variableName, freeSpace);
    }

    @Keyword("Copy total space")
    @Parameters(min = 2)
    public void copyTotalSpace(String variableName, String path) {
        File file = new File(path);
        long totalSpace = file.getTotalSpace();
        blackboard().setData(variableName, totalSpace);
    }

    @Keyword("Validate absolute free space")
    @Parameters(min = 2)
    public void validateFreeSpace(String path, long min) {
        File file = new File(path);
        long freeSpace = file.getFreeSpace();
        boolean flag = freeSpace >= min;
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "The space on %d was %d which is lower than %d";
            String error = String.format(format, path, freeSpace, min);
            log().error(error);
        }
    }

    @Keyword("Validate percentage free space")
    @Parameters(min = 2)
    public void validateFreeSpace(String path, double minPercentage) {
        File file = new File(path);
        long freeSpace = file.getFreeSpace();
        long totalSpace = file.getTotalSpace();
        boolean flag = freeSpace >= totalSpace * minPercentage;
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "The space on %d was %d which is lower than %f of %d";
            String error = String.format(format, path, freeSpace, minPercentage,
                                         totalSpace);
            log().error(error);
        }
    }

    @Keyword("Validate path is directory")
    @Parameters(min = 1)
    public void validatePathIsDirectory(String path) {
        validatePathIsDirectory(path, true);
    }

    @Keyword("Validate path is no directory")
    @Parameters(min = 1)
    public void validatePathIsNoDirectory(String path) {
        validatePathIsDirectory(path, false);
    }

    private void validatePathIsDirectory(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.isDirectory();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s is %s a directory";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Validate path is file")
    @Parameters(min = 1)
    public void validatePathIsFile(String path) {
        validatePathIsFile(path, false);
    }

    private void validatePathIsFile(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.isFile();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s is %s a file";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Validate path is hidden")
    @Parameters(min = 1)
    public void validateFileIsHidden(String path) {
        validateFileIsHidden(path, true);
    }

    @Keyword("Validate path is not hidden")
    @Parameters(min = 1)
    public void validateFileIsNotHidden(String path) {
        validateFileIsHidden(path, false);
    }

    public void validateFileIsHidden(String path, boolean expected) {
        File file = new File(path);
        boolean flag = file.isHidden();
        publish(new SetStatusEvent(flag));
        if (flag != expected) {
            String format = "Path %s is %s hidden";
            String error = String.format(format, path, expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Copy path modification date")
    @Parameters(min = 2)
    public void copyFileModifiedDate(String variableName, String path) {
        File file = new File(path);
        long lastModified = file.lastModified();
        blackboard().setData(variableName, lastModified);
    }

    @Keyword("Validate path was modified after")
    @Parameters(min = 2)
    public void validatePathModifiedAfter(String path, Date modified) {
        File file = new File(path);
        long lastModified = file.lastModified();
        boolean flag = lastModified > modified.getTime();
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            Timestamp t = Timestamp.getDefaultInstance();
            String format = "Path %s was modified after $s";
            String error = String.format(format, path, t.getTimestamp(modified));
            log().error(error);
        }
    }

    @Keyword("Validate file was modified before")
    @Parameters(min = 2)
    public void validatePathModifiedBefore(String path, Date modified) {
        File file = new File(path);
        long lastModified = file.lastModified();
        boolean flag = lastModified < modified.getTime();
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            Timestamp t = Timestamp.getDefaultInstance();
            String format = "Path %s was modified before $s";
            String error = String.format(format, path, t.getTimestamp(modified));
            log().error(error);
        }
    }

    @Keyword("Validate paths are equal")
    @Parameters(min = 2)
    public void validateFilesAreEqual(String left, String right)
            throws IOException {
        validateFilesAreEqual(left, right, true);
    }

    @Keyword("Validate paths are not equal")
    @Parameters(min = 2)
    public void validateFilesAreNotEqual(String left, String right)
            throws IOException {
        validateFilesAreEqual(left, right, false);
    }

    private void validateFilesAreEqual(String left, String right,
                                       boolean expected)
            throws IOException {
        Path leftPath = new File(left).toPath();
        Path rightPath = new File(right).toPath();
        boolean flag = Files.isSameFile(leftPath, rightPath);
        publish(new SetStatusEvent(flag));
        if (flag == expected) {
            String format = "Path %s and %s point %s to the same file";
            String error = String.format(format, left, right,
                                         expected ? "not" : "");
            log().error(error);
        }
    }

    @Keyword("Copy file size")
    @Parameters(min = 2)
    public void copyFileSize(String variableName, String path)
            throws IOException {
        File file = new File(path);
        long size = Files.size(file.toPath());
        blackboard().setData(variableName, size);
    }

    @Keyword("Validate minimum file size")
    @Parameters(min = 2)
    public void valideFileSizeMinumum(String path, long expected)
            throws IOException {
        File file = new File(path);
        long size = Files.size(file.toPath());
        boolean flag = size >= expected;
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "File size of %s is %d which is lower than %d";
            String error = String.format(format, path, size, expected);
            log().error(error);
        }
    }

    @Keyword("Validate file size")
    @Parameters(min = 2)
    public void valideFileSizeEquals(String path, long expected)
            throws IOException {
        File file = new File(path);
        long size = Files.size(file.toPath());
        boolean flag = size == expected;
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "File size of %s is %d which is lower than %d";
            String error = String.format(format, path, size, expected);
            log().error(error);
        }
    }

    @Keyword("Validate maximum file size")
    @Parameters(min = 2)
    public void valideFileSizeMaximum(String path, long expected)
            throws IOException {
        File file = new File(path);
        long size = Files.size(file.toPath());
        boolean flag = size <= expected;
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "File size of %s is %d which is lower than %d";
            String error = String.format(format, path, size, expected);
            log().error(error);
        }
    }
}
