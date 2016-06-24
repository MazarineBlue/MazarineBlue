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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.pictures.compounders.EqualCompounder;
import org.mazarineblue.pictures.exceptions.BufferedImageMissingException;
import org.mazarineblue.pictures.exceptions.BytesMissingException;
import org.mazarineblue.pictures.exceptions.DimensionMissingException;
import org.mazarineblue.pictures.exceptions.InputStreamMissingException;
import org.mazarineblue.pictures.exceptions.OutputStreamMissingException;
import org.mazarineblue.pictures.exceptions.PictureMissingException;
import org.mazarineblue.pictures.compounders.CompoundMethod;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class PictureTest {

    static private final int BUFFER_CAPACITY = 4096;
    static private final int RED = (255 << 24) + (255 << 16);
    static private final int GREEN = (255 << 24) + (255 << 8);
    static private final int BLUE = (255 << 24) + 255;
    static private final int TRANSPARENT = 0;

    static private Dimension dimension3x3, dimension3x4, dimension4x3;
    static private Dimension dimension4x4, dimension4x5, dimension5x4;
    static private Dimension dimension200x150, dimension400x300;
    static private Picture image3x3, image3x4, image4x3, image4x4diff;
    static private Picture image4x4trans, image4x4, image4x4b, image4x4c;
    static private Picture image4x5, image5x4;
    static private Picture image200x150, image400x300;

    @BeforeClass
    static public final void setupClass()
            throws Exception {
        dimension3x3 = new Dimension(3, 3);
        dimension3x4 = new Dimension(3, 4);
        dimension4x3 = new Dimension(4, 3);
        dimension4x4 = new Dimension(4, 4);
        dimension4x5 = new Dimension(4, 5);
        dimension5x4 = new Dimension(5, 4);
        dimension200x150 = new Dimension(200, 150);
        dimension400x300 = new Dimension(400, 300);
        image3x3 = createPictureByImage(dimension3x3, RED);
        image3x4 = createPictureByImage(dimension3x4, RED);
        image4x3 = createPictureByImage(dimension4x3, RED);
        image4x4 = createPictureByBytes(dimension4x4, RED);
        image4x4diff = createPictureByImage(dimension4x4, BLUE);
        image4x4trans = createPictureByImage(dimension4x4, TRANSPARENT);
        image4x4b = createPictureByImage(dimension4x4, RED);
        image4x4c = createPictureByInputStream(dimension4x4, RED);
        image4x5 = createPictureByImage(dimension4x5, RED);
        image5x4 = createPictureByImage(dimension5x4, RED);
        image200x150 = createPictureByImage(dimension200x150, RED, BLUE);
        image400x300 = createPictureByImage(dimension400x300, RED, BLUE);
    }

    @AfterClass
    static public final void teardownClass() {
        image4x4 = image4x4b = image4x4c = null;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Picture creations">
    static private Picture createPictureByBytes(Dimension dimension, int... rgb)
            throws Exception {
        return new BufferedImagePicture(createBytes(dimension, rgb));
    }

    static public Picture createPictureByImage(Dimension dimension, int... rgb)
            throws Exception {
        return new BufferedImagePicture(createImage(dimension, rgb));
    }

    static private Picture createPictureByInputStream(Dimension dimension,
                                                      int... rgb)
            throws Exception {
        return new BufferedImagePicture(createInputStream(dimension, rgb));
    }

    static private InputStream createInputStream(Dimension dimension, int... rgb)
            throws Exception {
        byte[] data = createBytes(dimension, rgb);
        return new ByteArrayInputStream(data);
    }

    static private byte[] createBytes(Dimension dimension, int... rgb)
            throws Exception {
        BufferedImage image = createImage(dimension, rgb);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }

    static private BufferedImage createImage(Dimension dimension, int... rgb) {
        BufferedImage image = new BufferedImage(dimension.width,
                                                dimension.height,
                                                BufferedImage.TYPE_4BYTE_ABGR);
        int y = 0;
        for (int i = 0; i < rgb.length; ++i)
            for (; y < dimension.height * (i + 1) / rgb.length; ++y)
                for (int x = 0; x < dimension.width; ++x)
                    image.setRGB(x, y, rgb[i]);
        return image;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper methods for constructors">
    static private class PictureResource {

        private Picture createPictureWithBufferedImage(String file)
                throws Exception {
            InputStream input = getClass().getResourceAsStream(file);
            BufferedImage image = ImageIO.read(input);
            return new BufferedImagePicture(image);
        }

        private Picture createPictureWithInputStream(String file)
                throws Exception {
            InputStream input = getClass().getResourceAsStream(file);
            return new BufferedImagePicture(input);
        }

        private Picture createPictureWithBytes(String file)
                throws Exception {
            InputStream input = getClass().getResourceAsStream(file);
            byte[] data = read(input);
            return new BufferedImagePicture(data);
        }
    }

    static private byte[] read(InputStream input)
            throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        copy(input, output);
        return output.toByteArray();
    }

    static private void copy(InputStream input, OutputStream output)
            throws Exception {
        int n = BUFFER_CAPACITY;
        byte[] buffer = new byte[BUFFER_CAPACITY];

        while (n == BUFFER_CAPACITY) {
            n = input.read(buffer);
            output.write(buffer, 0, n);
        }
    }
    // </editor-fold>

    @Test(expected = InputStreamMissingException.class)
    public void constructor_NullBufferedImage_ThrowsInputStreamMissingException()
            throws Exception {
        new BufferedImagePicture((InputStream) null);
    }

    @Test(expected = BytesMissingException.class)
    public void constructor_NullBufferedImage_ThrowsBytesMissingException()
            throws Exception {
        new BufferedImagePicture((byte[]) null);
    }

    @Test(expected = BufferedImageMissingException.class)
    public void constructor_NullBufferedImage_ThrowsBufferedImageMissingException()
            throws Exception {
        new BufferedImagePicture((BufferedImage) null);
    }

    @Test
    public void getDimension_IsEqual() {
        assertEquals(dimension4x4, image4x4.getDimension());
        assertEquals(dimension4x4.width, image4x4.getWidth());
        assertEquals(dimension4x4.height, image4x4.getHeight());
    }

    @Test
    public void equals_TwoIdenticalPictures_PictureAre()
            throws Exception {
        assertEquals(true, image4x4.equals(image4x4b));
        assertEquals(true, image4x4.equals(image4x4c));
    }

    @Test
    public void equals_TwoDifferentPicturesDueWidth_PictureAreNot()
            throws Exception {
        assertEquals(false, image4x4.equals(image5x4));
    }

    @Test
    public void equals_TwoDifferentPicturesDueHeight_PictureAreNot()
            throws Exception {
        assertEquals(false, image4x4.equals(image4x5));
    }

    @Test
    public void equals_TwoDifferentPicturesDueWithAndHeight_PictureAreNot()
            throws Exception {
        assertEquals(false, image4x4.equals(image3x3));
    }

    @Test
    public void equals_Null_ReturnsFalse()
            throws Exception {
        assertEquals(false, image4x4.equals(null));
    }

    @Test
    public void equals_Object_ReturnsFalse()
            throws Exception {
        assertEquals(false, image4x4.equals(new Object()));
    }

    @Test
    public void hashCode_EqualPictures_Succeeds()
            throws Exception {
        Map map = new HashMap();
        map.put(image4x4, image4x4b);
    }

    @Test
    public void getData_EqualPictures_Equals()
            throws Exception {
        assertArrayEquals(image4x4.getData(), image4x4b.getData());
        assertArrayEquals(image4x4.getData(), image4x4c.getData());
    }

    @Test
    public void getData_UnequalPictures_EqualsNot()
            throws Exception {
        assertEquals(false, Arrays.equals(image4x4.getData(),
                                          image4x5.getData()));
    }

    @Test
    public void getImage_EqualPictures_Equals()
            throws Exception {
        assertEquals(true, compareImages(image4x4, image4x4b));
        assertEquals(true, compareImages(image4x4, image4x4c));
    }

    @Test
    public void getImage_UnequalPictures_EqualsNot()
            throws Exception {
        assertEquals(false, compareImages(image4x4, image4x5));
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for getImage tests">
    private boolean compareImages(Picture left, Picture right)
            throws Exception {
        return Arrays.equals(left.getData(), right.getData());
    }

    private byte[] convert(BufferedImage image)
            throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }
    // </editor-fold>

    @Test(expected = DimensionMissingException.class)
    public void clip_NullImage_ThrowsDimensionMissingException()
            throws Exception {
        image4x4.clip(null);
    }

    @Test
    public void clip_SameSizeImage_ReturnsTheSamePicture()
            throws Exception {
        assertEquals(true, image4x4 == image4x4.clip(dimension4x4));
    }

    @Test
    public void clip_LargerWith_ReturnsAnEqualPicture()
            throws Exception {
        assertEquals(image4x4, image4x4.clip(dimension5x4));
    }

    @Test
    public void clip_LargerHeight_ReturnsAnEqualPicture()
            throws Exception {
        assertEquals(image4x4, image4x4.clip(dimension4x5));
    }

    @Test
    public void clip_SmallWidth_ReturnsClippedPicture()
            throws Exception {
        assertEquals(image3x4, image4x4.clip(dimension3x4));
    }

    @Test
    public void clip_SmallHeight_ReturnsClippedPicture()
            throws Exception {
        assertEquals(image4x3, image4x4.clip(dimension4x3));
    }

    @Test
    public void fetchThumbnail_Picture_ReturnThumbnail()
            throws Exception {
        assertEquals(image200x150, image400x300.fetchThumbnail());
    }

    @Test(expected = OutputStreamMissingException.class)
    public void write_Null_ThrowsOutputStreamMissingException()
            throws Exception {
        image4x4.write(null);
    }

    @Test
    public void write_Picture_EqualByteArrays()
            throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        image4x4.write(output);
        assertArrayEquals(image4x4.getData(), output.toByteArray());
    }

    @Test
    public void compare_Null_ReturnsFalse()
            throws Exception {
        EqualCompounder.Result compare = image4x4.compare(null);
        assertEquals(EqualCompounder.Result.FAIL, compare);
    }

    private Picture createImage(int pixel)
            throws Exception {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pixel);
        return new BufferedImagePicture(image);
    }

    @Test(expected = PictureMissingException.class)
    public void compound_Null_ThrowsPictureMissingException() throws Exception {
        image4x4.compound(null, method);
    }

    private final CompoundMethod method = new CompoundMethod() {

        static private final int ALPHA = 24;
        static private final int RED = 16;
        static private final int GREEN = 8;
        static private final int BLUE = 0;

        @Override
        public int compute(int leftPixel, int rightPixel) {
            int a = leftPixel & (255 << ALPHA);
            int b = leftPixel & 255;
            int c = rightPixel & 255;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int computeLeft(int leftPixel) {
            int a = leftPixel & (255 << ALPHA);
            int b = leftPixel & 255;
            int c = 255 - b;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int computeRigth(int rightPixel) {
            int a = rightPixel & (255 << ALPHA);
            int b = rightPixel & 255;
            int c = 255 - b;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int computeNeither() {
            int a = 255 << ALPHA;
            int b = 0 & 255;
            int c = 255 - 0;
            int pixel = a + b + c;
            return pixel;
        }
    };
    
    @Test
    public void same_SamePictures_ResultsInSecondPicture()
            throws Exception {
        Picture same = image4x4.same(image4x4diff);
        assertEquals(image4x4trans, same);
    }
    
    private Picture createRedWithTransparentBorderPicture(int width, int height)
            throws Exception {
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_4BYTE_ABGR);
        return new BufferedImagePicture(image);
    }
    
    private void dwawRedAndTransparent(BufferedImage image, int width, int height) {
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y)
                image.setRGB(x, y, getRedOrTransparentPixel(x, y));
    }
    
    private int getRedOrTransparentPixel(int x, int y) {
        return x < 3 && y < 3 ? RED : TRANSPARENT;
    }
}
