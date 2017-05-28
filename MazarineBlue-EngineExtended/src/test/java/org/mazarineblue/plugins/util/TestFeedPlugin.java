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

import java.io.IOException;
import java.io.InputStream;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.plugins.FeedPlugin;

public class TestFeedPlugin
        implements FeedPlugin {

    private final Feed passingFeed;
    private final String[] sheets;

    public TestFeedPlugin(Feed passingFeed, String... sheets) {
        this.passingFeed = passingFeed;
        this.sheets = sheets;
    }

    @Override
    public boolean canProcess(String format) {
        return true;
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return sheets;
    }

    @Override
    public Feed createFeed(InputStream input, String sheet)
            throws IOException {
        return passingFeed;
    }
}
