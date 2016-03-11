/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.pictures;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ImageUtilTest {

    @Test
    public void test() {
        int width = 2, height = 2;
        int[] expected = getExpectedPixels(width, height, pixel(255, 127, 63, 31), 0);
        Picture picture = ImageUtil.createPicture(getImage(width, height, expected), false);
        assertEquals(width, picture.getWidth());
        assertEquals(height, picture.getHeight());
        assertArrayEquals(expected, picture.getPixels());
    }

    public static int pixel(int alpha, int red, int green, int blue) {
        return alpha << 24 + red << 16 + green << 8 + blue;
    }

    public static int[] getExpectedPixels(int width, int height, int value, int offset) {
        int[] expected = new int[width * height + offset];
        Arrays.fill(expected, offset, expected.length, value);
        return expected;
    }

    private static Image getImage(int width, int height, int[] expected) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        image.setRGB(0, 0, width, height, expected, 0, width);
        return image;
    }
}
