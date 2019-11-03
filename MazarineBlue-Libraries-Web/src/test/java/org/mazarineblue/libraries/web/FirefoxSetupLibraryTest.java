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
package org.mazarineblue.libraries.web;

import org.junit.Test;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.openqa.selenium.remote.BrowserType;

public class FirefoxSetupLibraryTest
        extends AbstractSetupLibraryTestHelper {

    @Test
    public void addArgument() {
        openAndClose(new ExecuteInstructionLineEvent("Add argument", "foo"), null);
    }

    @Test
    public void addBooleanPreference() {
        openAndClose(new ExecuteInstructionLineEvent("Add boolean preference", "foo", true), "tab A");
    }

    @Test
    public void addIntegerPreference() {
        openAndClose(new ExecuteInstructionLineEvent("Add integer preference", "foo", 1), "tab A");
    }

    @Test
    public void addPreference_String_OK() {
        openAndClose(new ExecuteInstructionLineEvent("Add preference", "foo", "bar"), "tab A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPreference_Other_FAIL() {
        openAndClose(new ExecuteInstructionLineEvent("Add preference", "foo", new Object()), "tab A");
    }
    
    @Test
    public void setAcceptInsecureCerts_True() {
        openAndClose(new ExecuteInstructionLineEvent("Set accept insecure certs", true), "tab A");
    }

    @Test
    public void setHeadless_True() {
        openAndClose(new ExecuteInstructionLineEvent("Set headless", true), "tab A");
    }

    private void openAndClose(ExecuteInstructionLineEvent event, String tab) {
        openAndClose(BrowserType.FIREFOX, event, tab);
    }
}
