/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.pictures.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class AdapterImage
        extends Image {

    private final Image adaptee;

    public AdapterImage(Image adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public int getWidth(ImageObserver observer) {
        return adaptee.getWidth(observer);
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return adaptee.getHeight(observer);
    }

    @Override
    public ImageProducer getSource() {
        return adaptee.getSource();
    }

    @Override
    public Graphics getGraphics() {
        return adaptee.getGraphics();
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return adaptee.getProperty(name, observer);
    }

    @Override
    public Image getScaledInstance(int width, int height, int hints) {
        return adaptee.getScaledInstance(width, height, hints);
    }

    @Override
    public void flush() {
        adaptee.flush();
    }

    @Override
    public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
        return adaptee.getCapabilities(gc);
    }

    @Override
    public void setAccelerationPriority(float priority) {
        adaptee.setAccelerationPriority(priority);
    }

    @Override
    public float getAccelerationPriority() {
        return adaptee.getAccelerationPriority();
    }

    @Override
    public int hashCode() {
        return adaptee.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return adaptee.equals(obj);
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }
}
