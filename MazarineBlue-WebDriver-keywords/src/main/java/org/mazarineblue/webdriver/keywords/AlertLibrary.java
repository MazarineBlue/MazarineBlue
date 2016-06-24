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
package org.mazarineblue.webdriver.keywords;

import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Beta;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebToolkit;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class AlertLibrary
        extends Library {

    private final WebToolkit.AlertInstructions backend;

    AlertLibrary(WebToolkit.AlertInstructions backend, Library library) {
        super("org.mazarineblue.webdriver", library);
        this.backend = backend;
    }

    @Override
    protected void setup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void teardown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Keyword("Accept alert")
    public void accept() {
        backend.accept();
    }

    @Keyword("Dismiss alert")
    public void dismiss() {
        backend.dismiss();
    }

    @Keyword("Copy value")
    @Parameters(min = 1)
    public void copyValue(String variableName) {
        String text = backend.getText();
        blackboard().setData(variableName, text);
    }

    @Keyword("Validate value")
    @Parameters(min = 1)
    public void validateValue(String regex) {
        String text = backend.getText();
        boolean flag = BrowserLibrary.check(text, regex);
        publish(new SetStatusEvent(flag));
        if (flag)
            return;
        String format = "The alert %s had the text '%s' that not match regex: %s";
        String error = String.format(format, text, regex);
        log().error(error);
    }

    @Beta
    @Keyword("Basic authenticate")
    public void basicAuthenticate(String username, String password) {
        backend.basicAuthenticate(username, password);
    }

    @Keyword("Type")
    public void type(String text) {
        backend.type(text);
    }
}
