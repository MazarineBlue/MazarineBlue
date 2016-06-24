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
package org.mazarineblue.runner.swing;

import java.io.File;
import org.mazarineblue.keyworddriven.documentMediators.FileDocumentMediator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class FileDocumentManager
        extends FileDocumentMediator
        implements DocumentManager {

    public FileDocumentManager(File input) {
        super(input);
    }

    @Override
    public void openLogOutput() {
        File file = getLogFile();
        WebDriver driver = new FirefoxDriver();
        driver.get(file.getAbsolutePath());
    }

    @Override
    public void emailLogOutput() {
        File file = getLogFile();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void openReportOutput() {
        File file = getReportFile();
        WebDriver driver = new FirefoxDriver();
        driver.get(file.getAbsolutePath());
    }

    @Override
    public void emailReportOutput() {
        File file = getReportFile();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
