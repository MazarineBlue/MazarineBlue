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
package org.mazarineblue.runner.sheetFactorySelector;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.datasources.util.SourceUtil;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class AbstractSheetFactory
        implements SheetFactory {

    private final FeedBuilder feedBuilder;
    private final String location;

    public AbstractSheetFactory(FeedBuilder feedBuilder, DocumentMediator mediator) {
        this.feedBuilder = feedBuilder;
        this.location = mediator.getInputLocation();
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Feed getSheetFeed(String sheetName) {
        DataSource source = getSource(sheetName, sheetName, false);
        return feedBuilder.createFeed(source);
    }

    abstract protected DataSource getSource(String sourceIdentifier, String sheetName, boolean headers);

    @Override
    public DataSource getMatrixSource(String sourceIdentifier, String sheetName) {
        return getSource(sourceIdentifier, sheetName, true);
    }

    @Override
    public final DataSource getArraySource(String sourceIdentifier, String sheetName) {
        DataSource src = getSource(sourceIdentifier, sheetName, true);
        ObjectArraySource dst = new ObjectArraySource(sourceIdentifier, false);
        SourceUtil.copyAndTransform(src, dst);
        return dst;
    }
}
