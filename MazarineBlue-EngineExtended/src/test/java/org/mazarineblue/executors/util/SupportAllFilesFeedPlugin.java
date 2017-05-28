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
package org.mazarineblue.executors.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.feeds.DummyFeed;
import org.mazarineblue.plugins.FeedPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SupportAllFilesFeedPlugin
        implements FeedPlugin {

    private Feed feed;
    private String[] sheets;

    public SupportAllFilesFeedPlugin() {
        this.sheets = sheets;
    }

    public SupportAllFilesFeedPlugin setFeed(Feed feed) {
        this.feed = feed;
        return this;
    }

    public SupportAllFilesFeedPlugin setSheets(String... sheets) {
        this.sheets = sheets;
        return this;
    }

    @Override
    public boolean canProcess(String mimeType) {
        return true;
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return Arrays.copyOf(sheets, sheets.length);
    }

    @Override
    public Feed createFeed(InputStream input, String sheetName)
            throws IOException {
        return feed != null ? feed : new DummyFeed();
    }
}
