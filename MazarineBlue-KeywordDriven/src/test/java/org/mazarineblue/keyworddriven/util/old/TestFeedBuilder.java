/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestFeedBuilder
        implements FeedBuilder {

    public TestFeedBuilder() {
    }

    @Override
    public Feed createFeed(InstructionLine... instructions) {
        List<InstructionLine> list = Arrays.asList(instructions);
        return createFeed(list);
    }

    @Override
    public final Feed createFeed(Collection<InstructionLine> instructions) {
        return new Feed() {
            private Iterator<InstructionLine> iterator;

            @Override
            public String getIdentifier() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean hasNext() {
                return iterator().hasNext();
            }

            @Override
            public InstructionLine next() {
                return iterator().next();
            }

            private Iterator<InstructionLine> iterator() {
                if (iterator == null)
                    iterator = instructions.iterator();
                return iterator;
            }
        };
    }

    @Override
    public Feed createFeed(DataSource source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Feed createFeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
