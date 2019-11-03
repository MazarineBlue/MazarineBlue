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

import org.mazarineblue.libraries.web.util.WebDriverSetupChecker.SetupCheckOption;
import static org.mazarineblue.libraries.web.util.WebDriverSetupChecker.SetupCheckOption.ONCE;

public abstract class CheckSetup {

    private SetupCheckOption option = ONCE;
    private boolean flag = true;
    
    public void checkSetup() {
        try {
            switch (option) {
                case NEVER:
                    break;
                case ALWAYS:
                    doRun();
                    flag = false;
                    break;
                case ONCE:
                default:
                    if (flag)
                        doRun();
                    flag = false;
            }

        } catch (RuntimeException ex) {
            // If we faill, then we try to run the browser anyway.
        }
    }

    protected abstract void doRun();

    void setOption(SetupCheckOption option) {
        this.option = option;
    }
}
