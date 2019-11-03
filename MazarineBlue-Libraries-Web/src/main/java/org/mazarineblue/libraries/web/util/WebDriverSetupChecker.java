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
package org.mazarineblue.libraries.web.util;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverSetupChecker {

    @SuppressWarnings("PublicInnerClass")
    public static enum SetupCheckOption {
        NEVER, ONCE, ALWAYS
    }

    private final CheckSetup chrome = new CheckSetup() {
        @Override
        protected void doRun() {
            WebDriverManager.chromedriver().setup();
        }
    };
    private final CheckSetup edge = new CheckSetup() {
        @Override
        protected void doRun() {
            WebDriverManager.edgedriver().setup();
        }
    };
    private final CheckSetup firefox = new CheckSetup() {
        @Override
        protected void doRun() {
            WebDriverManager.firefoxdriver().setup();
        }
    };
    private final CheckSetup ie = new CheckSetup() {
        @Override
        protected void doRun() {
            WebDriverManager.iedriver().setup();
        }
    };
    private final CheckSetup opera = new CheckSetup() {
        @Override
        protected void doRun() {
            WebDriverManager.operadriver().setup();
        }
    };

    public void setRun(SetupCheckOption option) {
        chrome.setOption(option);
        edge.setOption(option);
        firefox.setOption(option);
        ie.setOption(option);
        opera.setOption(option);
    }

    public void chromeDriverSetup() {
        chrome.checkSetup();
    }

    public void edgeDriverSetup() {
        edge.checkSetup();
    }

    public void firefoxDriverSetup() {
        firefox.checkSetup();
    }

    public void ieDriverSetup() {
        ie.checkSetup();
    }

    public void operaDriverSetup() {
        opera.checkSetup();
    }
}
