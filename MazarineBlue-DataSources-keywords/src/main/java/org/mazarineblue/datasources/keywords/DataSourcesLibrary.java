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
package org.mazarineblue.datasources.keywords;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.SourceChain;
import org.mazarineblue.datasources.SourceChain;
import org.mazarineblue.datasources.exceptions.DataSourceNotFoundException;
import org.mazarineblue.datasources.exceptions.SheetTypeUnsupportedException;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.events.GetDataSourceEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DataSourcesLibrary
        extends Library {

    private final Map<String, DataSource> sources = new HashMap<>(4);
    private String activeName;
    private DataSource activeSource;
    private Subscriber<Event> subscriber;

    public DataSourcesLibrary() {
        super("org.mazarineblue.datasources");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @EventHandler
    public void eventHandler(GetDataSourceEvent event) {
        String name = event.getName();
        DataSource source = name == null ? activeSource : sources.get(name);
        event.set(source);
        event.setConsumed();
    }

    @Keyword("Select source")
    @Parameters(min = 1, max = 1)
    public void selectSource(String name) {
        if (sources.containsKey(name) == false)
            throw new DataSourceNotFoundException(
                    "Source '" + name + "' not found.");
        activeName = name;
        activeSource = sources.get(name);
        if (activeSource == null)
            throw new DataSourceNotFoundException(name);
        executor().setSource(activeSource);
    }

    @Keyword("Next source row")
    @Parameters(min = 0, max = 0)
    public void nextSourceRow() {
        activeSource.next();
    }

    @Keyword("Data sheet")
    @Parameters(min = 3, max = 3)
    public void dataSheet(String name, String type, String sheetName) {
        if (sources.containsKey(name))
            log().warning("Alreay contains a sheet with the name: " + name);

        DataSource source;
        switch (type) {
            case "array":
                source = sheetFactory().getArraySource(name, sheetName);
                break;
            case "matrix":
                source = sheetFactory().getMatrixSource(name, sheetName);
                break;
            default:
                throw new SheetTypeUnsupportedException(type);
        }
        if (source == null)
            throw new DataSourceNotFoundException(name);
        sources.put(name, source);
    }

    @Keyword("Chain sources")
    @Parameters(min = 2)
    public void chainSources(String name, String... param) {
        DataSource[] arr = new DataSource[param.length];
        for (int i = 0; i < param.length; ++i) {
            if (this.sources.containsKey(param[i]) == false)
                throw new DataSourceNotFoundException(param[i]);
            arr[i] = sources.get(param[i]);
        }        
        sources.put(name, new SourceChain(arr));
    }
}
