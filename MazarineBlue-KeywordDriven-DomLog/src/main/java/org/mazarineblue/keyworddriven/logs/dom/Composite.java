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
package org.mazarineblue.keyworddriven.logs.dom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Composite
        extends Component {

    private final List<Component> childeren = new ArrayList<>(0);

    public Composite(Date startDate) {
        super(startDate);
    }

    public StatusLeaf add(Status status, String message) {
        StatusLeaf leaf = new MessageResult(status, message);
        add(leaf);
        return leaf;
    }

    public StatusLeaf add(Status status, Throwable ex) {
        StatusLeaf leaf = new ExceptionResult(status, ex);
        add(leaf);
        return leaf;
    }

    public final void add(Component e) {
        childeren.add(e);
    }

    @Override
    public String toString() {
        return getStatus() + " : size=" + childeren.size();
    }

    @Override
    public Status getStatus() {
        Status status = Status.PASS;
        for (Component child : childeren) {
            Status s = child.getStatus();
            switch (s) {
                case INFO:
                    if (status.equals(Status.PASS))
                        status = s;
                    break;
                case WARNING:
                    status = s;
                    break;
                case ERROR:
                    return s;
            }
        }
        return status;
    }

    @Override
    public void accept(LogVisitor visitor)
            throws IOException {
        for (Component e : childeren)
            e.accept(visitor);
    }
}
