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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mazarineblue.pictures.ImageUtil.createPicture;
import static org.mazarineblue.pictures.ImageUtilTest.getExpectedPixels;
import static org.mazarineblue.pictures.PixelUtil.pixel;
import org.mazarineblue.pictures.compounders.CompareCompounder;
import org.mazarineblue.pictures.compounders.CompoundMethod;
import org.mazarineblue.pictures.exceptions.OutputStreamMissingException;
import org.mazarineblue.pictures.exceptions.PictureMissingException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class PictureTest {

    private static final int BUFFER_CAPACITY = 4096;
    private static final int RED = (255 << 24) + (255 << 16);
    private static final int GREEN = (255 << 24) + (255 << 8);
    private static final int BLUE = (255 << 24) + 255;
    private static final int TRANSPARENT = 0;

    private static Dimension dimension3x3, dimension3x4, dimension4x3;
    private static Dimension dimension4x4, dimension4x5, dimension5x4;
    private static Picture image3x3, image3x4, image4x3, image4x4diff;
    private static Picture image4x4transparent, image4x4, image4x4b, image4x4c;
    private static Picture image4x5, image5x4;

    @BeforeClass
    public static final void setupClass()
            throws IOException {
        dimension3x3 = new Dimension(3, 3);
        dimension3x4 = new Dimension(3, 4);
        dimension4x3 = new Dimension(4, 3);
        dimension4x4 = new Dimension(4, 4);
        dimension4x5 = new Dimension(4, 5);
        dimension5x4 = new Dimension(5, 4);
        image3x3 = createPictureByImage(dimension3x3, RED);
        image3x4 = createPictureByImage(dimension3x4, RED);
        image4x3 = createPictureByImage(dimension4x3, RED);
        image4x4 = createPictureByBytes(dimension4x4, RED);
        image4x4diff = createPictureByImage(dimension4x4, BLUE);
        image4x4transparent = createPictureByImage(dimension4x4, TRANSPARENT);
        image4x4b = createPictureByImage(dimension4x4, RED);
        image4x4c = createPictureByInputStream(dimension4x4, RED);
        image4x5 = createPictureByImage(dimension4x5, RED);
        image5x4 = createPictureByImage(dimension5x4, RED);
    }

    @AfterClass
    @SuppressWarnings("NestedAssignment")
    public static final void teardownClass() {
        image4x4 = image4x4b = image4x4c = null;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Picture creations">
    private static Picture createPictureByBytes(Dimension dimension, int... rgb)
            throws IOException {
        return new Picture(createBytes(dimension, rgb));
    }

    public static Picture createPictureByImage(Dimension dimension, int... rgb)
            throws IOException {
        return createPicture(createImage(dimension, rgb));
    }

    private static Picture createPictureByInputStream(Dimension dimension, int... rgb)
            throws IOException {
        return new Picture(createInputStream(dimension, rgb));
    }

    private static InputStream createInputStream(Dimension dimension, int... rgb)
            throws IOException {
        byte[] data = createBytes(dimension, rgb);
        return new ByteArrayInputStream(data);
    }

    private static byte[] createBytes(Dimension dimension, int... rgb)
            throws IOException {
        BufferedImage image = createImage(dimension, rgb);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }

    private static BufferedImage createImage(Dimension dimension, int... rgb) {
        BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_4BYTE_ABGR);
        int y = 0;
        for (int i = 0; i < rgb.length; ++i)
            for (; y < dimension.height * (i + 1) / rgb.length; ++y)
                for (int x = 0; x < dimension.width; ++x)
                    image.setRGB(x, y, rgb[i]);
        return image;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper methods for constructors">
    private static class PictureResource {

        private Picture createPictureWithBufferedImage(String file)
                throws IOException {
            InputStream input = getClass().getResourceAsStream(file);
            BufferedImage image = ImageIO.read(input);
            return ImageUtil.createPicture(image);
        }

        private Picture createPictureWithInputStream(String file)
                throws IOException {
            InputStream input = getClass().getResourceAsStream(file);
            return new Picture(input);
        }

        private Picture createPictureWithBytes(String file)
                throws IOException {
            InputStream input = getClass().getResourceAsStream(file);
            byte[] data = read(input);
            return new Picture(data);
        }
    }

    private static byte[] read(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        copy(input, output);
        return output.toByteArray();
    }

    private static void copy(InputStream input, OutputStream output)
            throws IOException {
        int n = BUFFER_CAPACITY;
        byte[] buffer = new byte[BUFFER_CAPACITY];
        while (n == BUFFER_CAPACITY) {
            n = input.read(buffer);
            output.write(buffer, 0, n);
        }
    }
    // </editor-fold>

    @Test
    public void toString_() {
        assertEquals("width=" + 3 + ", height=" + 4, image3x4.toString());
    }

    @SuppressWarnings("PublicInnerClass")
    public class GetAndSetPixelTest {

        private final int width = 2, height = 2, offset = 2;

        private Picture picture;

        @Before
        public void setup() {
            picture = new Picture(width, height);
        }

        @After
        public void teardown() {
            picture = null;
        }

        public class GetPixel_Rainy {

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixel_NegetiveStartX_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixel(-1, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixel_StartXToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixel(width + 1, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixel_NegetiveStartY_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixel(0, -1);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixel_StartYToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixel(0, height + 1);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_NegetiveStartX_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(-1, 0, width, height, null, height);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_StartXToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(1, 0, width, height, null, height);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_NegetiveStartY_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, -1, width, height, null, height);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_StartYToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 1, width, height, null, height);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_NegetiveWidth_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 0, -1, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_WidthToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 0, width + 1, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_NegativeHeight_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 0, width, -1, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_HeightToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 0, width, height + 1, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_NegetiveOffset_ThrowsArrayIndexOutOfBoundsException() {
                picture.getPixels(0, 0, width, height, null, -1);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void getPixels_OffsetToLarge_ThrowsArrayIndexOutOfBoundsException() {
                int[] arr = new int[width * height + offset - 1];
                picture.getPixels(0, 0, width, height, arr, offset + 1);
            }
        }

        public class SetPixel_Rainy {

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixel_NegetiveStartX_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixel(-1, 0, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixel_StartXToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixel(width + 1, 0, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixel_NegetiveStartY_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixel(0, -1, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixel_StartYToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixel(0, height + 1, 0);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_NegetiveStartX_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(-1, 0, width, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_StartXToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(1, 0, width, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_NegetiveStartY_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, -1, width, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_StartYToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 1, width, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_NegetiveWidth_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 0, -1, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_WidthToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 0, width + 1, height, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_NegativeHeight_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 0, width, -1, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_HeightToLarge_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 0, width, height + 1, null, offset);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_NegetiveOffset_ThrowsArrayIndexOutOfBoundsException() {
                picture.setPixels(0, 0, width, height, null, -1);
            }

            @Test(expected = ArrayIndexOutOfBoundsException.class)
            public void setPixels_OffsetToLarge_ThrowsArrayIndexOutOfBoundsException() {
                int[] expected = getExpectedPixels(width, height, pixel(255, 127, 63, 31), offset);
                picture.setPixels(0, 0, width, height, expected, offset + 1);
            }
        }

        @Test
        public void getAndSetPixels_Happy() {
            int[] actual = new int[width * height + offset];
            int[] expected = getExpectedPixels(width, height, pixel(255, 127, 63, 31), offset);
            picture.setPixels(0, 0, width, height, expected, offset);
            int[] arr = picture.getPixels(0, 0, width, height, actual, offset);
            assertArrayEquals(expected, actual);
            assertArrayEquals(expected, arr);
            assertArrayEquals(arr, actual);
        }
    }

    @Test
    public void getDimension_IsEqual() {
        assertEquals(dimension4x4, image4x4.getDimension());
        assertEquals(dimension4x4.width, image4x4.getWidth());
        assertEquals(dimension4x4.height, image4x4.getHeight());
    }

    @Test
    public void equals_TwoIdenticalPictures_PictureAre() {
        assertTrue(image4x4.equals(image4x4b));
        assertTrue(image4x4.equals(image4x4c));
    }

    @Test
    public void equals_TwoDifferentPicturesDueToWidth_PictureAreNot() {
        assertFalse(image4x4.equals(image5x4));
    }

    @Test
    public void equals_TwoDifferentPicturesDueToHeight_PictureAreNot() {
        assertFalse(image4x4.equals(image4x5));
    }

    @Test
    public void equals_TwoDifferentPicturesDueWithAndHeight_PictureAreNot() {
        assertFalse(image4x4.equals(image3x3));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null_ReturnsFalse() {
        assertFalse(image4x4.equals(null));
    }

    @Test
    public void equals_Object_ReturnsFalse() {
        assertFalse(image4x4.equals(new Object()));
    }

    @Test
    public void hashCode_EqualPictures_Succeeds() {
        Map<Picture, Picture> map = new HashMap<>(4);
        map.put(image4x4, image4x4b);
    }

    @Test
    public void getData_EqualPictures_Equals() {
        assertArrayEquals(image4x4.getData(), image4x4b.getData());
        assertArrayEquals(image4x4.getData(), image4x4c.getData());
    }

    @Test
    public void getData_UnequalPictures_EqualsNot() {
        assertFalse(Arrays.equals(image4x4.getData(), image4x5.getData()));
    }

    @Test
    public void getImage_EqualPictures_Equals() {
        assertTrue(compareImages(image4x4, image4x4b));
        assertTrue(compareImages(image4x4, image4x4c));
    }

    @Test
    public void getImage_UnequalPictures_EqualsNot() {
        assertFalse(compareImages(image4x4, image4x5));
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for getImage tests">
    private boolean compareImages(Picture left, Picture right) {
        return Arrays.equals(left.getData(), right.getData());
    }

    private byte[] convert(BufferedImage image)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }
    // </editor-fold>

    @Test
    public void clip_SameSizeImage_ReturnsTheSamePicture()
            throws IOException {
        assertTrue(image4x4 == image4x4.clip(dimension4x4.width, dimension4x4.height));
    }

    @Test
    public void clip_LargerWith_ReturnsAnEqualPicture()
            throws IOException {
        assertEquals(image4x4, image4x4.clip(image4x4.getWidth() + 1, image4x4.getHeight()));
    }

    @Test
    public void clip_LargerHeight_ReturnsAnEqualPicture()
            throws IOException {
        assertEquals(image4x4, image4x4.clip(image4x4.getWidth(), image4x4.getHeight() + 1));
    }

    @Test
    public void clip_SmallWidth_ReturnsClippedPicture()
            throws IOException {
        assertEquals(image3x4, image4x4.clip(image3x4.getWidth(), image3x4.getHeight()));
    }

    @Test
    public void clip_SmallHeight_ReturnsClippedPicture()
            throws IOException {
        assertEquals(image4x3, image4x4.clip(image4x3.getWidth(), image4x3.getHeight()));
    }

    @Test
    public void fetchThumbnail_Picture_ReturnThumbnail()
            throws IOException {
        Picture image200x150 = createPictureByImage(new Dimension(200, 150), RED, BLUE);
        Picture image400x300 = createPictureByImage(new Dimension(400, 300), RED, BLUE);
        assertEquals(image200x150, image400x300.scale(image200x150.getWidth(), image200x150.getHeight()));
    }

    @Test(expected = OutputStreamMissingException.class)
    public void write_Null_ThrowsOutputStreamMissingException()
            throws IOException {
        image4x4.write(null);
    }

    @Test
    public void write_Picture_EqualByteArrays()
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_CAPACITY);
        image4x4.write(output);
        assertArrayEquals(image4x4.getData(), output.toByteArray());
    }

    @Test
    public void compare_Null_ReturnFail() {
        assertEquals(CompareCompounder.Result.FAIL, image4x4.compare(null));
    }

    @Test
    public void compare_DifferentContent_ReturnDiffWidth() {
        assertEquals(CompareCompounder.Result.DIFF_WIDTH, image4x4.compare(image3x4));
    }

    @Test
    public void compare_DifferentContent_ReturnDiffHeight() {
        assertEquals(CompareCompounder.Result.DIFF_HEIGHT, image4x4.compare(image4x3));
    }

    @Test
    public void compare_DifferentContent_ReturnDiffWidthAndHeight() {
        assertEquals(CompareCompounder.Result.DIFF_WIDTH_AND_HEIGHT, image4x4.compare(image3x3));
    }

    @Test
    public void compare_DifferentContent_ReturnDiffContent() {
        assertEquals(CompareCompounder.Result.DIFF_CONTENT, image4x4.compare(image4x4diff));
    }

    @Test
    public void compare_IdenticalPictures_ReturnEqual() {
        assertEquals(CompareCompounder.Result.EQUAL, image4x4.compare(image4x4b));
    }

    private Picture createImage(int pixel)
            throws IOException {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pixel);
        return createPicture(image);
    }

    @Test(expected = PictureMissingException.class)
    public void compound_Null_ThrowsPictureMissingException()
            throws IOException {
        image4x4.compound(null, method);
    }

    private final CompoundMethod method = new CompoundMethod() {

        private static final int ALPHA = 24;
        private static final int RED = 16;
        private static final int GREEN = 8;
        private static final int BLUE = 0;

        @Override
        public int compoundBoth(int leftPixel, int rightPixel) {
            int a = leftPixel & (255 << ALPHA);
            int b = leftPixel & 255;
            int c = rightPixel & 255;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int compoundLeft(int leftPixel) {
            int a = leftPixel & (255 << ALPHA);
            int b = leftPixel & 255;
            int c = 255 - b;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int compoundRigth(int rightPixel) {
            int a = rightPixel & (255 << ALPHA);
            int b = rightPixel & 255;
            int c = 255 - b;
            int pixel = a + b + c;
            return pixel;
        }

        @Override
        public int compoundNeither() {
            int a = 255 << ALPHA;
            int b = 0 & 255;
            int c = 255 - 0;
            int pixel = a + b + c;
            return pixel;
        }
    };

    @Test
    public void diff_SamePictures_ResultInTransparantPicture()
            throws IOException {
        assertEquals(image4x4transparent, image4x4.diff(image4x4b));
    }

    @Test
    public void same_DifferentPictures_ResultsTrransparentPicture()
            throws IOException {
        assertEquals(image4x4transparent, image4x4.same(image4x4diff));
    }
}
