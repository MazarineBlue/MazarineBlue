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

import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.feeds.DummyFeed;
import org.mazarineblue.plugins.util.TestFeedPlugin;
import static org.junit.Assert.assertEquals;

public class PluginLoaderTest {

    private PluginLoader loader;

    @Before
    public void setup() {
        loader = PluginLoader.getInstance();
    }

    @After
    public void teardown() {
        loader.reload();
        loader = null;
    }

    @Test
    public void getFeedPlugins() {
        loader.injectPlugin(new TestFeedPlugin(new DummyFeed(), "main"));
        List<FeedPlugin> plugins = loader.getPlugins(FeedPlugin.class);
        assertEquals(2, plugins.size());
    }

    @Test
    public void getLibraryPlugins() {
        List<LibraryPlugin> plugins = loader.getPlugins(LibraryPlugin.class);
        assertEquals(2, plugins.size());
    }
}
