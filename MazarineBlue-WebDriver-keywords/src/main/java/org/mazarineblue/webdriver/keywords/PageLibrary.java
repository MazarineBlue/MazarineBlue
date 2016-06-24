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

import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebToolkit;
import static org.mazarineblue.webdriver.keywords.BrowserLibrary.check;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class PageLibrary
        extends AbstractWebToolkitLibrary {

    PageLibrary(Library library, WebToolkit toolkit, TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Copy page title")
    @Parameters(min = 1)
    public void copyPageTitle(String variableName) {
        String title = toolkit.pageInstructions().getPageTitle();
        blackboard().setData(variableName, title);
    }

    @Keyword("Validate page title")
    @Parameters(min = 1, max = 2)
    public void validatePageTitle(String regex) {
        String title = toolkit.pageInstructions().getPageTitle();
        boolean flag = check(title, regex);
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "Title '%s' doesn't match regex: %s";
            String error = String.format(format, title, regex);
            log().error(error);
        }
    }
}
