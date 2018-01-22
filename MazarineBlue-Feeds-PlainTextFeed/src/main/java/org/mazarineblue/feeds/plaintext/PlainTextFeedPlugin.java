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
package org.mazarineblue.feeds.plaintext;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.plugins.FeedPlugin;
import org.openide.util.lookup.ServiceProvider;

/**
 * A {@code PlainTextFeed} is a feed plugin, which has the capability to read
 * plain text files. The file must hold a table, with each row on a new line,
 * starting and ending with a pipe symbol, and each column separated by the
 * pipe symbol i.e. {@code | namespace.keyword | argument 1 | argument 2 |}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@ServiceProvider(service = FeedPlugin.class)
public class PlainTextFeedPlugin
        implements FeedPlugin {

    @Override
    public boolean canProcess(String mimeType) {
        return "text/plain".equals(mimeType);
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return new String[]{"Main"};
    }

    @Override
    public Feed createFeed(InputStream input, String sheet) {
        return new PlainTextFeed(new InputStreamReader(input));
    }
}
