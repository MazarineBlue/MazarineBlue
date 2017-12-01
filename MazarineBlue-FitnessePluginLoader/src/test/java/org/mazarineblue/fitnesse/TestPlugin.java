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
package org.mazarineblue.fitnesse;

import fitnesse.plugins.PluginException;
import fitnesse.plugins.PluginFeatureFactoryBase;
import fitnesse.reporting.FormatterRegistry;
import fitnesse.responders.ResponderFactory;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.wiki.WikiPageFactoryRegistry;
import fitnesse.wikitext.parser.SymbolProvider;

public class TestPlugin
        extends PluginFeatureFactoryBase {

    public static final String NAME = "test";

    @Override
    public void registerCustomComparators(CustomComparatorRegistry customComparatorRegistry)
            throws PluginException {
        super.registerCustomComparators(customComparatorRegistry);
    }

    @Override
    public void registerSlimTables(SlimTableFactory slimTableFactory)
            throws PluginException {
        super.registerSlimTables(slimTableFactory);
    }

    @Override
    public void registerTestSystemFactories(TestSystemFactoryRegistry registry)
            throws PluginException {
        TestSystemFactory factory = new TestFactory();
        registry.registerTestSystemFactory(NAME, factory);
    }

    @Override
    public void registerFormatters(FormatterRegistry registrar)
            throws PluginException {
        super.registerFormatters(registrar);
    }

    @Override
    public void registerWikiPageFactories(WikiPageFactoryRegistry wikiPageFactoryRegistry)
            throws PluginException {
        super.registerWikiPageFactories(wikiPageFactoryRegistry);
    }

    @Override
    public void registerSymbolTypes(SymbolProvider symbolProvider)
            throws PluginException {
        super.registerSymbolTypes(symbolProvider);
    }

    @Override
    public void registerResponders(ResponderFactory responderFactory)
            throws PluginException {
        super.registerResponders(responderFactory);
    }
}
