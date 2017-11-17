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
package org.mazarineblue.mbt.gui.list;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

class ListLayoutManager
        implements LayoutManager {

    private static final int HGAP = 2;
    private static final int VGAP = 2;

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Nothing to do here
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // Nothing to do here
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            return new Dimension(getMaxComponentPreferredWidths(parent) + insets.left + insets.right + 2 * HGAP,
                                 getSumComponentPreferredHeights(parent) + insets.top + insets.bottom + 2 * VGAP);
        }
    }

    private int getMaxComponentPreferredWidths(Container parent) {
        int width = 0;
        for (Component c : parent.getComponents())
            if (width < c.getPreferredSize().width)
                width = c.getPreferredSize().width;
        return width;
    }

    private int getSumComponentPreferredHeights(Container parent) {
        int height = 0;
        for (Component c : parent.getComponents())
            height += c.getPreferredSize().height;
        return height;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            return new Dimension(getMaxComponentMinimumWidths(parent) + insets.left + insets.right + 2 * HGAP,
                                 getSumComponentMinimumHeights(parent) + insets.top + insets.bottom + 2 * VGAP);
        }
    }

    private int getMaxComponentMinimumWidths(Container parent) {
        int width = 0;
        for (Component c : parent.getComponents())
            if (width < c.getMinimumSize().width)
                width = c.getMinimumSize().width;
        return width;
    }

    private int getSumComponentMinimumHeights(Container parent) {
        int height = 0;
        for (Component c : parent.getComponents())
            height += c.getMinimumSize().height;
        return height;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int x = insets.left + HGAP;
            int y = insets.top + VGAP;
            int width = parent.getWidth() + 1 - insets.left - insets.right - 2 * HGAP;
            for (Component c : parent.getComponents()) {
                Dimension p = c.getPreferredSize();
                c.setBounds(x, y, width, (int) p.getHeight());
                y += p.height + 2 * VGAP - 1;
            }
        }
    }
}
