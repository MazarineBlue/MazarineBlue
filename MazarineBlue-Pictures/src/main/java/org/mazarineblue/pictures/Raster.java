/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.awt.Dimension;
import java.util.Arrays;

class Raster {

    final int[] rgbArray;
    private final int width;
    private final int height;

    Raster(int width, int height) {
        this.rgbArray = new int[width * height];
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "width=" + width + ", height=" + height;
    }

    Dimension getDimension() {
        return new Dimension(width, height);
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int[] getRGB(int startX, int startY, int width, int height, int[] rgbArray, int offset) {
        if (areParametersOutOfBounds(startX, width, startY, height, rgbArray, offset))
            throw new ArrayIndexOutOfBoundsException();
        int[] arr = rgbArray != null ? rgbArray : new int[width * height + offset];
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                arr[offset + i + j * width] = getRGB(startX + i, startY + j);
        return arr;
    }

    int getRGB(int x, int y) {
        if (x < 0 || x > width || y < 0 || y > height)
            throw new ArrayIndexOutOfBoundsException();
        return rgbArray[x + y * width];
    }

    void setRGB(int startX, int startY, int width, int height, int[] rgbArray, int offset) {
        if (areParametersOutOfBounds(startX, width, startY, height, rgbArray, offset))
            throw new ArrayIndexOutOfBoundsException();
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                setRGB(startX + i, startY + j, rgbArray[offset + i + j * width]);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for scale()">
    private boolean areParametersOutOfBounds(int x, int width, int y, int height, int[] rgbArray, int offset) {
        return isXOrWidthOutOfBounds(x, width) || isYOrHeightOutOfBounds(y, height)
                || isOffsetOutOfBoundsOrRgbArrayToSmall(width, height, rgbArray, offset);
    }

    private boolean isXOrWidthOutOfBounds(int x, int width) {
        return x < 0 || width < 0 || x + width > this.width;
    }

    private boolean isYOrHeightOutOfBounds(int y, int height) {
        return y < 0 || height < 0 || y + height > this.height;
    }

    private boolean isOffsetOutOfBoundsOrRgbArrayToSmall(int width, int height, int[] rgbArray, int offset) {
        return offset < 0 || rgbArray != null && offset + width * height > rgbArray.length;
    }
    // </editor-fold>

    void setRGB(int x, int y, int rgb) {
        if (x < 0 || x > width || y < 0 || y > height)
            throw new ArrayIndexOutOfBoundsException();
        rgbArray[x + y * width] = rgb;
    }

    void copyAndClip(Raster raster) {
        for (int y = 0; y < height; ++y)
            System.arraycopy(raster.rgbArray, y * raster.width, rgbArray, y * width, width);
    }

    @Override
    public int hashCode() {
        return 7 * 61 * 61 * 61
                + 61 * 61 * Arrays.hashCode(this.rgbArray)
                + 61 * this.width
                + this.height;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && this.width == ((Raster) obj).width && this.height == ((Raster) obj).height
                && Arrays.equals(this.rgbArray, ((Raster) obj).rgbArray);
    }
}
