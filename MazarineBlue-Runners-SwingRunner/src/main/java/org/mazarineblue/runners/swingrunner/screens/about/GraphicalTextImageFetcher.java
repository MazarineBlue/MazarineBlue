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
package org.mazarineblue.runners.swingrunner.screens.about;

import static java.awt.Color.BLACK;
import static java.awt.Color.LIGHT_GRAY;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.util.ArrayList;

/**
 * A {@code GraphicalTextImageFetcher} is a {@code ImageFetcher} that can
 * create an image and draw text on it.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class GraphicalTextImageFetcher
        implements ImageFetcher {

    private final ArrayList<GraphicalMessage> list = new ArrayList<>(4);
    private final int width;
    private final int height;

    /**
     * Constructs a {@code GraphicalTextImageFetcher} with the capability to
     * create an image with the specified dimension.
     *
     * @param width  the image width.
     * @param height the image height.
     */
    public GraphicalTextImageFetcher(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Adds a {@code GraphicalMessageImpl}
     *
     * @param e the element to add to the list.
     */
    public void addGraphicalMessage(GraphicalMessage e) {
        list.add(e);
    }

    @Override
    public Image getImage() {
        Image image = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(LIGHT_GRAY);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(BLACK);
        graphics.setFont(new Font("Arial Black", BOLD, 20));
        list.stream().forEach(line -> graphics.drawString(line.getMessage(), line.getX(), line.getY()));
        return image;
    }
}
