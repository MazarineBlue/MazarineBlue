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
package org.mazarineblue.utilities.swing;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JMenu;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaultChilderenFetcher<T extends Component>
        implements SwingUtil.Fetcher<T> {

    @Override
    public Component[] getChilderen(Component parent) {
        return (parent instanceof JMenu) ? ((JMenu) parent).getMenuComponents() : (parent instanceof Container) ? ((Container) parent).getComponents() : new Component[0];
    }
}
