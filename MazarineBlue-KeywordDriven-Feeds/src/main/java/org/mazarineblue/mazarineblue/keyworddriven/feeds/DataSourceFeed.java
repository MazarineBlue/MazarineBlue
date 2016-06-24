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
package org.mazarineblue.mazarineblue.keyworddriven.feeds;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.exceptions.IllegalFeedStateException;
import org.mazarineblue.keyworddriven.feeds.Feed;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DataSourceFeed
        implements Feed {

    private final DataSource source;

    public DataSourceFeed(DataSource source) {
        this.source = source;
    }

    @Override
    public String getIdentifier() {
        return toString(source.getSourceIdentifiers());
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public InstructionLine next() {
        source.next();
        String lineIdentifier = toString(source.getLineIdentifiers());
        String namespace = getNamespace();
        String keyword = getKeyword();
        Object[] parameters = getParameters();
        return new InstructionLine(lineIdentifier, namespace, keyword, parameters);
    }

    private String toString(List<String> identifiers) {
        String str = null;
        for (String identifier : identifiers)
            if (str == null)
                str = identifier;
            else
                str += ", " + identifier;
        return str;
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in next()">
    private String getNamespace() {
        String path = getPath();
        return InstructionLine.getNamespace(path);
    }

    private String getPath() {
        String path = source.getData(0, String.class);
        return path == null ? "" : path;
    }

    private String getKeyword() {
        String path = getPath();
        return InstructionLine.getKeyword(path);
    }

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private Object[] getParameters()
            throws IllegalFeedStateException {
        int index = 0;
        List<Object> list = new ArrayList<>();
        while (true) {
            Object obj = source.getData(++index);
            if (obj == null)
                break;
            list.add(obj);
        }
        return list.toArray();
    }
}
