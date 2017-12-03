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
package org.mazarineblue.swingrunner;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.swingrunner.screens.about.ImagePanel;
import org.mazarineblue.utililities.util.SerializeUtil;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

public class ImagePanelTest
        extends TestHashCodeAndEquals<ImagePanel> {

    @Test
    public void serialize()
            throws Exception {
        ImagePanel expected = getObject();
        ImagePanel actual = SerializeUtil.copy(expected);
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
