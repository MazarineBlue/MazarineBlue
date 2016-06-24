/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.datasources;

import java.util.function.Consumer;
import org.mazarineblue.datasources.exceptions.ReachedEndOfDataSourceException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SourceChain
        extends BackedSource {

    private boolean initialized = false;

    public SourceChain(DataSource... sources) {
        super(sources);
    }

    @Override
    public void add(DataSource src) {
        super.add(src);
    }

    @Override
    public final boolean hasNext() {
        return stream().anyMatch(source -> source.hasNext());
     }

    @Override
    public void reset() {
        initialized = false;
        stream().forEach(source -> source.reset());
    }

    @Override
    public void next() {
        if (hasNext() == false)
            throw new ReachedEndOfDataSourceException();
        if (initialized == false) {
            initialize();
            return;
        }
        nextProduct();
    }

    private void initialize() {
        initialized = true;
        stream().filter(source -> source.hasNext()).forEach((source) -> {
            source.next();
        });
    }

    private void nextProduct() {
        stream().forEach(new Consumer<DataSource>() {
            private boolean skip = false;

            @Override
            public void accept(DataSource source) {
                if (skip)
                    return;
                if (isAtEnd(source))
                    gotoFirstRow(source);
                else {
                    source.next();
                    skip = true;
                }
            }
            
            private boolean isAtEnd(DataSource source) {
                return source.hasNext() == false;
            }
            
            private void gotoFirstRow(DataSource source) {
                source.reset();
                if (source.hasNext())
                    source.next();
            }
        });
    }
}
