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

public class ChromeSetupLibraryTest
        extends AbstractSetupLibraryTestHelper {

    @Test
    public void addArgument() {
        openAndClose(new ExecuteInstructionLineEvent("Add argument", "foo"), null);
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
        openAndClose(BrowserType.CHROME, event, tab);
    }
}
