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

import java.io.Reader;
import static java.util.Arrays.copyOfRange;
import java.util.Iterator;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.AbstractInstructionFeed;

/**
 * A {@code PlainTextFeed} is a feed, with the capability to transform a row in
 * a {@link PlainTextTable} into an event.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PlainTextFeed
        extends AbstractInstructionFeed
        implements Feed {

    private final PlainTextTable table;
    private Iterator<String[]> it;

    /**
     * Constructs a {@code PlainTextFeed} using the specified reader.
     *
     * @param input the reader to read the content from.
     */
    public PlainTextFeed(Reader input) {
        this(new PlainTextReader(input).readTable());
    }

    /**
     * Constructs a {@code PlainTextFeed} using the specified table content.
     *
     * @param table the content to use for the feed.
     */
    public PlainTextFeed(PlainTextTable table) {
        this.table = table;
        it = table.iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Event next() {
        String[] column = it.next();
        return createEvent(column[0], (Object[]) copyOfRange(column, 1, column.length));
    }

    @Override
    public void reset() {
        super.reset();
        it = table.iterator();
    }
}
