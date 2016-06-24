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
import org.mazarineblue.keyworddriven.exceptions.InvalidBlackboardVariableName;
import org.mazarineblue.keyworddriven.exceptions.InvalidRegularExpression;
import org.mazarineblue.keyworddriven.exceptions.MalformedURLException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebToolkit;
import static org.mazarineblue.webdriver.keywords.BrowserLibrary.check;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class NavigationLibrary
        extends AbstractWebToolkitLibrary {

    NavigationLibrary(Library library, WebToolkit toolkit,
                      TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Load URL")
    @Parameters(min = 1, max = 1)
    public void loadURL(String url) {
        if (url == null || url.equals(""))
            throw new MalformedURLException(url);
        toolkit.navigationInstructions().loadUrl(url);
    }

    @Keyword("Page refresh")
    @Parameters(min = 0, max = 0)
    public void pageRefresh() {
        toolkit.navigationInstructions().pageRefresh();
    }

    @Keyword("Page back")
    @Parameters(min = 0, max = 1)
    public void pageBack(Double count) {
        if (count == null)
            toolkit.navigationInstructions().pageBack();
        else
            toolkit.navigationInstructions().pageBack(Math.round((double) count));
    }

    @Keyword("Page forward")
    @Parameters(min = 0, max = 1)
    public void pageForward(Double count) {
        if (count == null)
            toolkit.navigationInstructions().pageForward();
        else
            toolkit.navigationInstructions().pageForward(Math.round(
                    (double) count));
    }

    @Keyword("Copy current URL")
    @Parameters(min = 1)
    public void copyCurrentURL(String variableName) {
        if (variableName == null || variableName.equals(""))
            throw new InvalidBlackboardVariableName(variableName);
        String currentUrl = toolkit.navigationInstructions().getCurrentUrl();
        blackboard().setData(variableName, currentUrl);
    }

    @Keyword("Validate current URL")
    @Parameters(min = 1, max = 2)
    public void validateCurrentURL(String regex) {
        if (regex == null || regex.equals(""))
            throw new InvalidRegularExpression(regex);
        String currentUrl = toolkit.navigationInstructions().getCurrentUrl();
        boolean flag = check(currentUrl, regex);
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "Current URL '%s' doesn't match regex: %s";
            String error = String.format(format, currentUrl, regex);
            log().error(error);
        }
    }
}
