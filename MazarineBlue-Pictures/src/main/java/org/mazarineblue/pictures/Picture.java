/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.mazarineblue.pictures.compounders.CompareCompounder;
import org.mazarineblue.pictures.compounders.CompoundMethod;
import org.mazarineblue.pictures.compounders.PictureCompounder;
import org.mazarineblue.pictures.compounders.PixelComperator;
import org.mazarineblue.pictures.compounders.comperators.FullPixelComperator;
import org.mazarineblue.pictures.compounders.methods.DiffFilter;
import org.mazarineblue.pictures.compounders.methods.SameFilter;
import org.mazarineblue.pictures.exceptions.OutputStreamMissingException;
import org.mazarineblue.pictures.exceptions.PictureMissingException;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

/**
 * A {@code Picture} is a representation of an image.
 *
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Picture {

    private final Raster raster;

    Picture(Raster raster) {
        this.raster = raster;
    }

    /**
     * Creates a picture with the specified width and height.
     *
     * @param width  the width of the picture.
     * @param height the height of the picture.
     */
    public Picture(int width, int height) {
        raster = new Raster(width, height);
    }

    /**
     * Construct a {@code Picture} by reading the bytes from the input stream
     * and convert it into a {@link BufferedImage}.
     *
     * @param input the input stream to read.
     * @throws IOException when the stream could not be read for any reason,
     *                     other then the end of the stream, when the stream
     *                     has been closed or when some other IO error occurs.
     */
    public Picture(InputStream input)
            throws IOException {
        raster = convertToRaster(ImageIO.read(input));
    }

    /**
     * Construct a {@code Picture} by reading the bytes.
     *
     * @param arr the bytes to convert into a {@code BufferedImage}.
     */
    public Picture(byte[] arr) {
        try {
            raster = convertToRaster(ImageIO.read(new ByteArrayInputStream(arr)));
        } catch (IOException ex) {
            throw new NeverThrownException(ex); // Because we use a ByteArrayOutputStream
        }
    }

    private Raster convertToRaster(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Raster r = new Raster(width, height);
        image.getRGB(0, 0, width, height, r.rgbArray, 0, width);
        return r;
    }

    @Override
    public String toString() {
        return "width=" + raster.getWidth() + ", " + "height=" + raster.getHeight();
    }

    public byte[] getData() {
        try {
            BufferedImage image = convertToBufferedImage(raster);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new NeverThrownException(ex); // Because we use a ByteArrayOutputStream
        }
    }

    public Dimension getDimension() {
        return raster.getDimension();
    }

    public final int getWidth() {
        return raster.getWidth();
    }

    public final int getHeight() {
        return raster.getHeight();
    }

    /**
     * Gets the pixel at the specified location.
     *
     * @param x the x coordinate of the pixel.
     * @param y the y coordinate of the pixel.
     * @return the color value of the pixel in RGBA.
     */
    public int getPixel(int x, int y) {
        return raster.getRGB(x, y);
    }

    /**
     * Sets the pixel at the specified location to the specified RGB value.
     *
     * @param x   the x coordinate of the pixel.
     * @param y   the y coordinate of the pixel.
     * @param rgb the color value of the pixel.
     */
    public void setPixel(int x, int y, int rgb) {
        raster.setRGB(x, y, rgb);
    }

    /**
     * Returns the pixels of this {@code Picture}.
     * <p>
     * Returned is an array of interters. The pixels are in the default RGB
     * color model of {@link BufferedImage.TYPE_INT_ARGB}. There are 8 bytes
     * for each color component.
     *
     * @return the pixels of this {@code picture}.
     */
    public int[] getPixels() {
        return raster.getRGB(0, 0, raster.getWidth(), raster.getHeight(), null, 0);
    }

    /**
     * Gets pixels starting at location ({@code startX}, {@code startY}) and
     * ending at location ({@code startX + width}, {@code startY + height}) and
     * stores them in the specified array of integers.
     *
     * @param startX   the starting X coordinate.
     * @param startY   the starting Y coordinate.
     * @param width    width of region.
     * @param height   height of region.
     * @param rgbArray if not {@code null}, the rgb pixels are written here.
     * @param offset   offset into the {@code rgbArray}.
     * @return array of RGB pixels.
     */
    public int[] getPixels(int startX, int startY, int width, int height, int[] rgbArray, int offset) {
        return raster.getRGB(startX, startY, width, height, rgbArray, offset);
    }

    /**
     * Sets pixels starting at location ({@code startX}, {@code startY}) and
     * ending at location ({@code startX + width}, {@code startY + height}).
     *
     * @param startX   the starting X coordinate.
     * @param startY   the starting Y coordinate.
     * @param width    width of region.
     * @param height   height of region.
     * @param rgbArray if not {@code null}, the rgb pixels are written here.
     * @param offset   offset into the {@code rgbArray}.
     */
    public void setPixels(int startX, int startY, int width, int height, int[] rgbArray, int offset) {
        raster.setRGB(startX, startY, width, height, rgbArray, offset);
    }

    /**
     * Clips the picture to the specified dimensions.
     *
     * @param width  the width to clip the image to.
     * @param height the height to clip the image to.
     * @return the clipped picture.
     */
    public Picture clip(int width, int height) {
        if (isThisImageSmallerOrEqual(width, height))
            return this;

        Raster r = new Raster(width, height);
        r.copyAndClip(this.raster);
        return new Picture(r);
    }

    private boolean isThisImageSmallerOrEqual(int width, int height) {
        return raster.getWidth() <= width && raster.getHeight() <= height;
    }

    /**
     * Scales this {@code Picture} to the specified with and height.
     *
     * @param width  the width to scale to.
     * @param height the height to scale to.
     * @return the scaled picture.
     */
    public Picture scale(int width, int height) {
        BufferedImage src = convertToBufferedImage(raster);
        BufferedImage dst = createBufferedImage(width, height);
        copyAndScale(src, dst);
        return convertToPicture(width, height, dst);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for scale()">
    private BufferedImage convertToBufferedImage(Raster raster) {
        BufferedImage src = createBufferedImage(raster.getWidth(), raster.getHeight());
        src.setRGB(0, 0, raster.getWidth(), raster.getHeight(), raster.rgbArray, 0, raster.getWidth());
        return src;
    }

    private static BufferedImage createBufferedImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    private void copyAndScale(BufferedImage src, BufferedImage dst) {
        double sw = (double) dst.getWidth() / src.getWidth();
        double sh = (double) dst.getHeight() / src.getHeight();
        AffineTransform transform = AffineTransform.getScaleInstance(sw, sh);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(src, dst);
    }

    private Picture convertToPicture(int width, int height, BufferedImage dst) {
        Raster r = new Raster(width, height);
        dst.getRGB(0, 0, width, height, r.rgbArray, 0, width);
        return new Picture(r);
    }
    // </editor-fold>

    /**
     * Serializes the picture to the specified output stream.
     *
     * @param output the output stream to serialize the picture to.
     * @throws IOException when an IO error occurred during serialization.
     */
    public void write(OutputStream output)
            throws IOException {
        if (output == null)
            throw new OutputStreamMissingException();
        ImageIO.write(convertToBufferedImage(raster), "png", output);
    }

    /**
     * Compares two pictures and returns the result.
     *
     * @param other the other picture to compare with.
     * @return {@code EQUAL} when both pictures have identical content;
     *         {@code DIFF_WIDTH}, {@code DIFF_HEIGHT} or
     *         {@code DIFF_WIDTH_AND_HEIGH} when both pictures have different
     *         dimensions; {@code DIFF_CONTENT} when both pictures have
     *         different content; or {@code FAIL} when the comparable where not
     *         comparable.
     */
    public final CompareCompounder.Result compare(Picture other) {
        return compare(other, new FullPixelComperator());
    }

    /**
     * Compares two pictures, using the specified pixel comparator and returns
     * the result.
     *
     * @param other           the other picture to compare with.
     * @param pixelComperator the comparator used to compare both pictures.
     * @return {@code EQUAL} when both pictures have identical content;
     *         {@code DIFF_WIDTH}, {@code DIFF_HEIGHT} or
     *         {@code DIFF_WIDTH_AND_HEIGHT} when both pictures have different
     *         dimensions; {@code DIFF_CONTENT} when both pictures have
     *         different content; or {@code FAIL} when the comparable where not
     *         comparable.
     */
    public final CompareCompounder.Result compare(Picture other, PixelComperator pixelComperator) {
        if (other == null)
            return CompareCompounder.Result.FAIL;
        return new CompareCompounder(this, other, pixelComperator).compound();
    }

    /**
     * Creates a copy of this picture and filters out the identical pixels.
     *
     * @param other the other picture to use for comparison.
     * @return the filtered copy.
     */
    public Picture diff(Picture other) {
        return diff(other, new FullPixelComperator());
    }

    /**
     * Creates a copy of this picture and filters out the identical pixels,
     * according to the specified comparator.
     *
     * @param other      the other picture to use for comparison.
     * @param comperator the comparator to use for the comparison.
     * @return the filtered copy.
     */
    public Picture diff(Picture other, PixelComperator comperator) {
        return compound(other, new DiffFilter(comperator));
    }

    /**
     * Creates a copy of this picture and filters out the different pixels.
     *
     * @param other the other picture to use for comparison.
     * @return the filtered copy.
     */
    public Picture same(Picture other) {
        return same(other, new FullPixelComperator());
    }

    /**
     * Creates a copy of this picture and filters out the different pixels,
     * according to the specified comparator.
     *
     * @param other      the other picture to use for comparison.
     * @param comperator the comparator to use for the comparison.
     * @return the filtered copy.
     */
    public Picture same(Picture other, PixelComperator comperator) {
        return compound(other, new SameFilter(comperator));
    }

    /**
     * Combines this picture with the specified other picture, using the
     * compound method.
     *
     * @param other  the other picture to compound with.
     * @param method the method to use for compounding.
     * @return the compounded picture.
     */
    public final Picture compound(Picture other, CompoundMethod method) {
        if (other == null)
            throw new PictureMissingException();
        return new PictureCompounder(this, other, method).compound();
    }

    @Override
    public int hashCode() {
        return 7 * 53
                + Objects.hashCode(this.raster);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.raster, ((Picture) obj).raster);
    }
}
