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

public class AbstractDesiredCapabilitiesLibraryTest
        extends AbstractSetupLibraryTestHelper {

    @Test
    public void setAcceptInsecureCerts_True() {
        openAndClose(new ExecuteInstructionLineEvent("Set accept insecure certs", true));
    }

    @Test
    public void setBrowserName_Chrome() {
        openAndClose(new ExecuteInstructionLineEvent("Set browser name", "htmlunit"));

    }

    @Test
    public void setJavascriptEnabled_True() {
        openAndClose(new ExecuteInstructionLineEvent("Set javascript enabled", true));
    }

    @Test
    public void setPlatform_Android() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "android"));
    }

    @Test
    public void setPlatform_Any() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "any"));
    }

    @Test
    public void setPlatform_ElCapitan() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "el capitan"));
    }

    @Test
    public void setPlatform_HighSierra() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "high sierra"));
    }

    @Test
    public void setPlatform_Ios() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "IOS"));
    }

    @Test
    public void setPlatform_Linux() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "  linux"));
    }

    @Test
    public void setPlatform_Mac() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "mac  "));
    }

    @Test
    public void setPlatform_Mavericks() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "mavericks"));
    }

    @Test
    public void setPlatform_Majave() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "mojave"));
    }

    @Test
    public void setPlatform_MountainLion() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "mountain lion"));
    }

    @Test
    public void setPlatform_Sierra() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "sierra"));
    }

    @Test
    public void setPlatform_SnowLeopard() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "snow leopard"));
    }

    @Test
    public void setPlatform_Unix() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "unix"));
    }

    @Test
    public void setPlatform_Vista() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "vista"));
    }

    @Test
    public void setPlatform_Windows() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "windows"));
    }

    @Test
    public void setPlatform_Windows10() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "windows 10"));
    }

    @Test
    public void setPlatform_Windows8() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "windows 8"));
    }

    @Test
    public void setPlatform_Windows8_1() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "windows 8.1"));
    }

    @Test
    public void setPlatform_XP() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "windows xp"));
    }

    @Test
    public void setPlatform_Yosemite() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "yosemite"));
    }

    @Test
    public void setVersion() {
        openAndClose(new ExecuteInstructionLineEvent("Set version", "1.0"));
    }

    private void openAndClose(ExecuteInstructionLineEvent event) {
        openAndClose("htmlunit", event, "tab A");
    }
}
