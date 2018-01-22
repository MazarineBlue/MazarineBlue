/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.runners.swingrunner.screens.about;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * An {@code ImagePanel} is a {@code JPanel} that shows an {@link Image}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ImagePanel
        extends JPanel
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public Image getImage() {
        return image;
    }

    @Override
    public int hashCode() {
        try {
            return 5 * 71
                    + Arrays.hashCode(getData(convertImage(this.image)));
        } catch (IOException ex) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this == obj || obj != null && getClass() == obj.getClass()
                    && Arrays.equals(getData(convertImage(this.image)), getData(convertImage(((ImagePanel) obj).image)));
        } catch (IOException ex) {
            return false;
        }
    }

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.defaultWriteObject();
        writeImage(output);
    }

    private void writeImage(ObjectOutputStream output)
            throws IOException {
        byte[] data = getData(convertImage(image));
        output.writeInt(data.length);
        output.write(data);
    }

    private static BufferedImage convertImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(image, null, null);
        return bufferedImage;
    }

    private static byte[] getData(BufferedImage bufferedImage)
            throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outStream);
        return outStream.toByteArray();
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        this.image = readImage(input);
    }

    private static Image readImage(ObjectInputStream input)
            throws IOException {
        byte[] data = new byte[input.readInt()];
        input.readFully(data);
        return ImageIO.read(new ByteArrayInputStream(data));
    }
}
