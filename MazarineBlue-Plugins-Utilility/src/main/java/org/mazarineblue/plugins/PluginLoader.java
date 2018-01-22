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

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.openide.util.Lookup;

/**
 * The PluginLoader loads feed plugins, library plugins and other plugins
 * dynamically. The PluginLoader makes it possible to process new file formats
 * and take advantage of libraries in a plug and play fashion.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see FeedPlugin
 * @see LibraryPlugin
 * @see https://docs.oracle.com/javase/tutorial/ext/basics/spi.html
 */
public class PluginLoader {

    private static PluginLoader service;
    private final List<Plugin> injectedPlugins = new ArrayList<>();

    public static synchronized PluginLoader getInstance() {
        if (service == null)
            service = new PluginLoader();
        return service;
    }

    private PluginLoader() {
    }

    /**
     * Fetches a list of plugins with the specified type.
     *
     * @param <T>  the type of plugins to return.
     * @param type the type of plugins to return.
     * @return a list of plugins.
     */
    public <T extends Plugin> List<T> getPlugins(Class<T> type) {
        List<T> list = new ArrayList<>();
        list.addAll(getInjectedPluginOfType(type));
        list.addAll(Lookup.getDefault().lookupAll(type));
        return list;
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods of getPlugins">
    @SuppressWarnings("unchecked")
    private <T extends Plugin> List<T> getInjectedPluginOfType(Class<T> type) {
        List<Plugin> list = injectedPlugins.stream()
                .filter(plugin -> type.isAssignableFrom(plugin.getClass()))
                .collect(toList());
        return (List<T>) list;
    }
    //</editor-fold>

    /**
     * Clear this loaders cache so that the plugins be reloaded.
     *
     * <p>
     * After invoking this method, all the injected plugins will be removed
     * and the installed plugins be reload upon the usage of {@link #getPlugins}
     *
     * <p>
     * This method is intended for use in situations in which new plugins
     * can be installed into a running Java virtual machine.
     */
    public void reload() {
        injectedPlugins.clear();
    }

    /**
     * Injects a plugin using code. This method is intended for use in unit
     * tests.
     *
     * @param plugin
     */
    public void injectPlugin(Plugin plugin) {
        injectedPlugins.add(plugin);
    }
}
