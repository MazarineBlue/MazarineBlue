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

public class AbstractMutableCapabilitiesCapabilitiesLibraryTest
        extends AbstractSetupLibraryTestHelper {

    @Test
    public void setCapability() {
        openAndClose(new ExecuteInstructionLineEvent("Set capability", "foo", "bar"));
    }

    @Test
    public void setPlatform_Android() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "android"));
    }

    @Test
    public void setPlatform_Any() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "any"));
    }

    @Test
    public void setPlatform_ElCapitan() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "el capitan"));
    }

    @Test
    public void setPlatform_HighSierra() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "high sierra"));
    }

    @Test
    public void setPlatform_Ios() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "IOS"));
    }

    @Test
    public void setPlatform_Linux() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "  linux"));
    }

    @Test
    public void setPlatform_Mac() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "mac  "));
    }

    @Test
    public void setPlatform_Mavericks() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "mavericks"));
    }

    @Test
    public void setPlatform_Majave() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "mojave"));
    }

    @Test
    public void setPlatform_MountainLion() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "mountain lion"));
    }

    @Test
    public void setPlatform_Sierra() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "sierra"));
    }

    @Test
    public void setPlatform_SnowLeopard() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "snow leopard"));
    }

    @Test
    public void setPlatform_Unix() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "unix"));
    }

    @Test
    public void setPlatform_Vista() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "vista"));
    }

    @Test
    public void setPlatform_Windows() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "windows"));
    }

    @Test
    public void setPlatform_Windows10() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "windows 10"));
    }

    @Test
    public void setPlatform_Windows8() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "windows 8"));
    }

    @Test
    public void setPlatform_Windows8_1() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "windows 8.1"));
    }

    @Test
    public void setPlatform_XP() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "windows xp"));
    }

    @Test
    public void setPlatform_Yosemite() {
        openAndClose(new ExecuteInstructionLineEvent("Set platform", "foo", "yosemite"));
    }

    private void openAndClose(ExecuteInstructionLineEvent event) {
        openAndClose("chrome", event, "tab A");
    }
}
