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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.runners.swingrunner.screens.about.ImagePanel;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;
import static org.mazarineblue.utilities.util.TestUtil.copy;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class ImagePanelHCAE
            extends TestHashCodeAndEquals<ImagePanel> {

        @Test
        public void serialize()
                throws Exception {
            ImagePanel expected = getObject();
            ImagePanel actual = copy(expected);
            assertEquals(expected, actual);
        }

        @Override
        protected ImagePanel getObject() {
            return new ImagePanel(new BufferedImage(100, 100, TYPE_3BYTE_BGR));
        }

        @Override
        protected ImagePanel getDifferentObject() {
            return new ImagePanel(new BufferedImage(100, 101, TYPE_3BYTE_BGR));
        }
    }
}
