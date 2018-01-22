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
package org.mazarineblue.plugins;

import org.junit.Test;
import org.mazarineblue.plugins.util.MyTestPlugin;
import org.mazarineblue.plugins.util.TestPlugin;
import static org.mazarineblue.utilities.util.TestUtil.assertClasses;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PluginLoaderTest {

    @Test
    public void getPlugins() {
        PluginLoader loader = PluginLoader.getInstance();
        assertClasses(loader.getPlugins(TestPlugin.class), MyTestPlugin.class);

        loader.injectPlugin(new MyTestPlugin());
        assertClasses(loader.getPlugins(TestPlugin.class), MyTestPlugin.class, MyTestPlugin.class);

        loader.reload();
        assertClasses(loader.getPlugins(TestPlugin.class), MyTestPlugin.class);
    }
}
