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
package org.mazarineblue.plugins.util;

import java.io.InputStream;
import java.util.Arrays;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.plugins.FeedPlugin;

public class FeedPluginStub
        implements FeedPlugin {

    private final boolean canProcess;
    private final String[] sheetNames;
    private final Feed feed;

    public FeedPluginStub(boolean canProcess, String... sheetNames) {
        this.canProcess = canProcess;
        this.sheetNames = sheetNames;
        this.feed = null;
    }

    public FeedPluginStub(boolean canProcess, Feed feed, String... sheetNames) {
        this.canProcess = canProcess;
        this.sheetNames = sheetNames;
        this.feed = feed;
    }

    @Override
    public boolean canProcess(String mimeType) {
        return canProcess;
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return Arrays.copyOf(sheetNames, sheetNames.length);
    }

    @Override
    public Feed createFeed(InputStream input, String sheet) {
        return feed;
    }
}
