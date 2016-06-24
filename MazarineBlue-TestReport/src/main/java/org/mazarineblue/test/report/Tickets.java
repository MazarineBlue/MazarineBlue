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
package org.mazarineblue.test.report;

import org.mazarineblue.test.report.visitors.TestObjectVisitorException;
import org.mazarineblue.test.report.visitors.TestObjectVisitor;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class Tickets {

    private final Collection<Ticket> tickets = new ArrayList<>();

    void add(final Ticket ticket) {
        if (ticket == null)
            throw new NullPointerException();
        tickets.add(ticket);
    }

    Collection<Ticket> getTickets() {
        return new ArrayList(tickets);
    }

    public void output(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        for (Ticket t : tickets)
            visitor.ticket(t);
    }
}
