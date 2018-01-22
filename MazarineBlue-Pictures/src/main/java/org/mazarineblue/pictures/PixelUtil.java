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

/**
 * A {@code PixelUtil} is a utility class that provide basic pixel operations.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class PixelUtil {

    public static final int ALPHA = 24;
    public static final int RED = 16;
    public static final int GREEN = 8;
    public static final int BLUE = 0;

    /**
     * Calculates the pixel color value using the color components.
     *
     * @param alpha 0-255, 0 = transparent, 255 = fully visible.
     * @param red   0-255, 0 = no red, 255 = all red.
     * @param green 0-255, 0 = no green, 255 = all green.
     * @param blue  0-255, 0 = no blue, 255 = all blue.
     * @return the pixel build.
     */
    public static int pixel(int alpha, int red, int green, int blue) {
        return ((alpha & 255) << ALPHA) + ((red & 255) << RED) + ((green & 255) << GREEN) + ((blue & 255) << BLUE);
    }

    /**
     * Returns the alpha component of the specified pixel
     *
     * @param pixel the pixel to return the alpha component of.
     * @return the alpha component of the specified pixel.
     */
    public static int getAlpha(int pixel) {
        return pixel >> ALPHA;
    }

    /**
     * Replaces the alpha value of the specified pixel with the specified alpha
     * value.
     *
     * @param pixel the pixel to alter.
     * @param alpha the replacement value.
     * @return the altered pixel.
     */
    public static int setAlpha(int pixel, int alpha) {
        return (pixel & ~(1 << ALPHA)) + (alpha << ALPHA);
    }

    /**
     * Returns the red component of the specified pixel
     *
     * @param pixel the pixel to return the red component of.
     * @return the red component of the specified pixel.
     */
    public static int getRed(int pixel) {
        return (pixel >> RED) & 255;
    }

    /**
     * Replaces the red value of the specified pixel with the specified red
     * value.
     *
     * @param pixel the pixel to alter.
     * @param red   the replacement value.
     * @return the altered pixel.
     */
    public static int setRed(int pixel, int red) {
        return (pixel & ~(1 << RED)) + (red << RED);
    }

    /**
     * Returns the green component of the specified pixel.
     *
     * @param pixel the pixel to return the green component of.
     * @return the green component of the specified pixel.
     */
    public static int getGreen(int pixel) {
        return (pixel >> GREEN) & 255;
    }

    /**
     * Replaces the green value of the specified pixel with the specified green
     * value.
     *
     * @param pixel the pixel to alter.
     * @param green the replacement value.
     * @return the altered pixel.
     */
    public static int setGreen(int pixel, int green) {
        return (pixel & ~(1 << GREEN)) + (green << GREEN);
    }

    /**
     * Returns the blue component of the specified pixel.
     *
     * @param pixel the pixel to return the blue component of.
     * @return the blue component of the specified pixel.
     */
    public static int getBlue(int pixel) {
        return (pixel >> BLUE) & 255;
    }

    /**
     * Replaces the blue value of the specified pixel with the specified blue
     * value.
     *
     * @param pixel the pixel to alter.
     * @param blue  the replacement value.
     * @return the altered pixel.
     */
    public static int setBlue(int pixel, int blue) {
        return (pixel & ~(1 << BLUE)) + (blue << BLUE);
    }

    private PixelUtil() {
    }
}
