/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.mazarineblue.libraries.web;

import java.io.File;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import static org.mazarineblue.mazarineblue.libraries.web.WebDriverLibraryPlugin.NAMESPACE;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeLibrary
        extends Library {

    private ChromeOptions options = new ChromeOptions();

    public ChromeLibrary() {
        super(NAMESPACE);
    }

    @Keyword("Add argument")
    @Parameters(min = 1)
    public void addArgument(String... arguments) {
        options.addArguments(arguments);
    }

    @Keyword("Add encoded extentions")
    @Parameters(min = 1)
    public void addEncodedExtensions(String... encoded) {
        options.addEncodedExtensions(encoded);
    }

    @Keyword("Add extentions")
    @Parameters(min = 1)
    public void addExtensions(File... paths) {        
        options.addExtensions(paths);
    }

    @Keyword("Start chrome driver")
    public void start() {
        WebDriver driver = new ChromeDriver(options);
    }
}
