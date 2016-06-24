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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.util.Timestamp;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class FileDocumentMediator
        implements DocumentMediator {

    private final Path baseDirectory;
    private final File input;
    private File logFile, reportFile;

    public FileDocumentMediator(File input) {
        this.input = input = input.getAbsoluteFile();
        File parent = input.getParentFile();
        this.baseDirectory = parent.toPath();
    }

    @Override
    public Timestamp getDefaultTimestamp() {
        return Timestamp.getDefaultInstance("yyyy-MM-dd_HH-mm-ss");
    }

    @Override
    public String getDefaultFolder(Date date) {
        String name = input.getName();
        String timestamp = getDefaultTimestamp().getTimestamp(date);
        return "Logs for " + name + "/" + timestamp;
    }

    @Override
    public String getInputLocation() {
        return input.getAbsolutePath();
    }

    protected File getLogFile() {
        if (logFile == null)
            throw new IllegalStateException("The log should be written first");
        return logFile;
    }

    protected File getReportFile() {
        if (reportFile == null)
            throw new IllegalStateException("The report should be written first");
        return reportFile;
    }

    @Override
    public InputStream getInputStream()
            throws IOException {
        return new FileInputStream(input);
    }

    @Override
    public InputStream getInputStream(Path path)
            throws IOException {
        File file = baseDirectory.resolve(path).toFile();
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getLogOutputStream(String directory, String dummyName)
            throws IOException {
        File dir = new File(baseDirectory.toFile(), directory);
        setupTemplateFiles(dir);
        logFile = new File(dir, "log.xml");
        return new FileOutputStream(logFile);
    }

    @Override
    public OutputStream getReportOutputStream(String directory, String reportName)
            throws IOException {
        File dir = new File(baseDirectory.toFile(), directory);
        setupTemplateFiles(dir);
        reportFile = new File(dir, reportName + ".xml");
        return new FileOutputStream(reportFile);
    }

    private void setupTemplateFiles(File dest)
            throws IOException {
        if (dest == null)
            return;
        if (dest.exists() == false)
            dest.mkdirs();
        File file = new File(dest, "main.css");
        if (file.exists())
            return;
        setupTemplateFiles(dest, "main");
        setupTemplateFiles(dest, "log");
        setupTemplateFiles(dest, "report");
    }

    private void setupTemplateFiles(File dest, String template)
            throws IOException {
        FileUtils.copyFileToDirectory(new File(baseDirectory.toFile(), template + ".xsl"), dest);
        FileUtils.copyFileToDirectory(new File(baseDirectory.toFile(), template + ".css"), dest);
    }

    @Override
    public OutputStream getOutputStream(Path path)
            throws IOException {
        File file = baseDirectory.resolve(path).toFile();
        File parent = file.getParentFile();
        if (parent.exists() == false)
            parent.mkdirs();
        return new FileOutputStream(file);
    }
}
