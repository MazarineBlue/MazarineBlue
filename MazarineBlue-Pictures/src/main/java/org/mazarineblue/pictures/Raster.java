/*
 * Copyright (c) 2015 Alex de Kruijff
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
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Raster {

    final int[] rgbArray;
    final int width;
    final int height;

    public Raster(int width, int height) {
        this.rgbArray = new int[width * height];
        this.width = width;
        this.height = height;
    }

    public Raster(int[] arr, int width, int height) {
        this.rgbArray = arr;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "width=" + width + ", height=" + height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getRGB(int x, int y) {
        return rgbArray[x + y * width];
    }
    
    public void setRGB(int x, int y, int rgb) {
        rgbArray[x + y * width] = rgb;
    }

    void copyAndClip(Raster raster) {
        for (int y = 0; y < height; ++y)
            System.arraycopy(raster.rgbArray, y * raster.width,
                             rgbArray, y * width, width);
    }
}
