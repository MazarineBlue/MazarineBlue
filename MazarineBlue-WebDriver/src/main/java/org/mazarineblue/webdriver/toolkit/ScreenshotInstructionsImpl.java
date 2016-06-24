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
package org.mazarineblue.webdriver.toolkit;

import java.io.IOException;
import org.mazarineblue.pictures.BufferedImagePicture;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.webdriver.WebToolkit;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class ScreenshotInstructionsImpl
        implements WebToolkit.ScreenshotInstructions {

    private final TakesScreenshot takeScreenshot;

    ScreenshotInstructionsImpl(TakesScreenshot takeSchreenshot) {
        this.takeScreenshot = takeSchreenshot;
    }

    @Override
    public Picture takeScreenshot()
            throws IOException {
        byte[] data = takeScreenshot.getScreenshotAs(OutputType.BYTES);
        return new BufferedImagePicture(data);
    }
}
