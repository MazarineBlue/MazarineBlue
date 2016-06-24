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
package org.mazarineblue.keyworddriven.util.old;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.NullDataSource;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class NullSheetFactory
        implements SheetFactory {

    private final FeedBuilder feedBuilder;

    public NullSheetFactory(FeedBuilder feedBuilder) {
        this.feedBuilder = feedBuilder;
    }

    @Override
    public String getLocation(){
        return "NullSheetFactory: no location";
    }

    @Override
    public String[] getSheetNames(){
        return new String[0];
    }

    @Override
    public Feed getSheetFeed(String sheetName){
        return feedBuilder.createFeed();
    }

    @Override
    public DataSource getArraySource(String sourceIdentifier, String sheetName){
        return new NullDataSource();
    }

    @Override
    public DataSource getMatrixSource(String sourceIdentifier, String sheetName){
        return new NullDataSource();
    }
}
