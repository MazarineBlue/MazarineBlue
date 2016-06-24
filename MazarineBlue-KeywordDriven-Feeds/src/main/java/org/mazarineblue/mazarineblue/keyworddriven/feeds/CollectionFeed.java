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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.exceptions.IllegalFeedStateException;
import org.mazarineblue.keyworddriven.feeds.Feed;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class CollectionFeed
        implements Feed {

    private final Collection<InstructionLine> collection;
    private Iterator<InstructionLine> iterator;
    private InstructionLine current;

    public CollectionFeed(InstructionLine... lines) {
        this(Arrays.asList(lines));
    }

    public CollectionFeed(Collection<InstructionLine> collection) {
        this.collection = new ArrayList<>(collection);
    }

    @Override
    public String getIdentifier() {
        return "CollectionFeed";
    }

    @Override
    public boolean hasNext() {
        if (iterator == null)
            iterator = collection.iterator();
        return iterator.hasNext();
    }

    @Override
    public InstructionLine next() {
        try {
            if (iterator == null)
                iterator = collection.iterator();
            return current = iterator.next();
        } catch (NoSuchElementException ex) {
            throw new IllegalFeedStateException(ex);
        }
    }
}
