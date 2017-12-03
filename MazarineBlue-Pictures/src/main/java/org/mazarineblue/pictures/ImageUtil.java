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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A {@code ImageUtil} is a utility class that provides basic operations in
 * relation to images.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class ImageUtil {

    /**
     * Constructs a {@code Picture} from a {@code BufferedImage}.
     *
     * @param image the image to read.
     * @return a {@code Picture} with the contends of the specified
     *         {@code BufferedImage}.
     */
    public static Picture createPicture(Image image) {
        return createPicture(image, true);
    }

    static Picture createPicture(Image image, boolean cast) {
        BufferedImage img = convert(image, cast);
        Raster r = new Raster(img.getWidth(), img.getHeight());
        img.getRGB(0, 0, r.getWidth(), r.getHeight(), r.rgbArray, 0, r.getWidth());
        return new Picture(r);
    }

    private static BufferedImage convert(Image src, boolean cast) {
        if (src instanceof BufferedImage && cast)
            return (BufferedImage) src;
        BufferedImage dst = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        copyImageByDrawing(src, dst);
        return dst;
    }

    private static void copyImageByDrawing(Image src, BufferedImage dst) {
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
    }

    private ImageUtil() {
    }
}
