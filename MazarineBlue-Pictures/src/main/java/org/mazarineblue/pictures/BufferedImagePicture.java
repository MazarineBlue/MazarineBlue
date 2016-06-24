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

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.mazarineblue.pictures.exceptions.BufferedImageMissingException;
import org.mazarineblue.pictures.exceptions.BytesMissingException;
import org.mazarineblue.pictures.exceptions.InputStreamMissingException;
import org.mazarineblue.pictures.exceptions.OutputStreamMissingException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class BufferedImagePicture
        extends Picture {

    static private final int BUFFER_CAPACITY = 4096;
    private final BufferedImage image;

    final byte[] data;

    public BufferedImagePicture(InputStream input)
            throws IOException {
        /* The attributes needs to be set in the same constructor because the
         * data array can be altered by BufferedImage. This is not a defect but
         * something that goes with images.
         */
        this(convertToImage(read(input)));
    }

    public BufferedImagePicture(byte[] data)
            throws IOException {
        /* The attributes needs to be set in the same constructor because the
         * data array can be altered by BufferedImage. This is not a defect but
         * something that goes with images.
         */
        this(convertToImage(data));
    }

    public BufferedImagePicture(BufferedImage image)
            throws IOException {
        super(createRaster(image));
        this.image = image;
        this.data = convertToBytes(image);
    }

    @Override
    protected Picture createPicture(Raster raster)
            throws IOException {
        return new BufferedImagePicture(createBufferedImage(raster));
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for constructors">
    static private byte[] read(InputStream input)
            throws IOException {
        if (input == null) // This check can not be in the constructor.
            throw new InputStreamMissingException();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    static private void copy(InputStream input, OutputStream output)
            throws IOException {
        int n = BUFFER_CAPACITY;
        byte[] buffer = new byte[BUFFER_CAPACITY];

        while (n == BUFFER_CAPACITY) {
            n = input.read(buffer);
            output.write(buffer, 0, n);
        }
    }

    static private BufferedImage convertToImage(byte[] data)
            throws IOException {
        if (data == null) // This check can not be in the constructor.
            throw new BytesMissingException();
        InputStream input = new ByteArrayInputStream(data);
        return ImageIO.read(input);
    }

    static private byte[] convertToBytes(BufferedImage image)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }

    static private Raster createRaster(BufferedImage image) {
        if (image == null)
            throw new BufferedImageMissingException();

        int width = image.getWidth();
        int height = image.getHeight();
        int[] arr = new int[width * height];
        image.getRGB(0, 0, width, height, arr, 0, width);
        return new Raster(arr, width, height);
    }

    private BufferedImage createBufferedImage(Raster raster) {
        BufferedImage image = new BufferedImage(raster.width, raster.height,
                                                BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, raster.width, raster.height, raster.rgbArray, 0,
                     raster.width);
        return image;
    }
    // </editor-fold>

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public void write(OutputStream output)
            throws IOException {
        if (output == null)
            throw new OutputStreamMissingException();
        output.write(data);
        output.flush();
    }

    @Override
    public Picture fetchThumbnail()
            throws IOException {
        Convertor convertor = new Convertor(image);
        return new BufferedImagePicture(convertor.thumbnail());
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for fetchThumbnail()">
    private class Convertor {

        private final BufferedImage original;
        private final int width;
        private final int height;
        private final double scale;
        private final int type;

        Convertor(BufferedImage original) {
            this.original = original;
            type = original.getType();
            scale = (double) THUMNNAIL_WIDTH / original.getWidth();
            width = (int) (scale * original.getWidth());
            height = (int) (scale * original.getHeight());
        }

        private BufferedImage thumbnail() {
            BufferedImage thumbnail = new BufferedImage(width, height, type);
            AffineTransform transform = AffineTransform.getScaleInstance(scale,
                                                                         scale);
            AffineTransformOp op = new AffineTransformOp(transform,
                                                         AffineTransformOp.TYPE_BILINEAR);
            return op.filter(original, thumbnail);
        }
    }
    // </editor-fold>
}
