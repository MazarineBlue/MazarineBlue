/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.swingrunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.runners.swingrunner.exceptions.ImageFetcherNotSetException;
import org.mazarineblue.runners.swingrunner.exceptions.ResourceBundlerNotSetException;
import org.mazarineblue.runners.swingrunner.screens.about.AboutDialogBuilder;
import org.mazarineblue.runners.swingrunner.screens.about.GraphicalTextImageFetcher;

public class AboutDialgBuilderTest {

    private AboutDialogBuilder builder;

    @Before
    public void setup() {
        builder = new AboutDialogBuilder();
        builder.setImageFetcher(new GraphicalTextImageFetcher(0, 0));
        builder.setResourceBundle("MazarineBlue");
    }

    @After
    public void teardown() {
        builder = null;
    }

    @Test(expected = ImageFetcherNotSetException.class)
    public void setImageFetcher_Null_ThrowsException() {
        builder.setImageFetcher(null);
        builder.validate();
    }

    @Test(expected = ResourceBundlerNotSetException.class)
    public void setBundle_Null_ThrowsException() {
        builder.setResourceBundle(null);
        builder.validate();
    }
}
