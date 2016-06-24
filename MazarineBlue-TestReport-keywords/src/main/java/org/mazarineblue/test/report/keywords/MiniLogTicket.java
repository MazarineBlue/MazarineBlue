/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue.test.report.keywords;

import java.io.IOException;
import java.io.OutputStream;
import org.mazarineblue.keyworddriven.logs.dom.Composite;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;
import org.mazarineblue.keyworddriven.logs.visitors.XmlLogVisitor;
import org.mazarineblue.test.links.MiniLogLink;
import org.mazarineblue.test.report.Ticket;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class MiniLogTicket
        extends Ticket {

    private final Composite composite;

    MiniLogTicket(MiniLogLink.Key key, Composite composite) {
        super(key.getSuite(), key.getTestcase(), key.getPlatform());
        this.composite = composite;
    }

    @Override
    public void writeNestedXml(OutputStream output)
            throws IOException {
        writeOpenTag(output);
        writeContent(output);
        writeCloseTag(output);
    }

    private void writeOpenTag(OutputStream output)
            throws IOException {
        output.write("<minilog>".getBytes());
    }

    private void writeContent(OutputStream output)
            throws IOException {
        LogVisitor vistor = new XmlLogVisitor(output);
        composite.accept(vistor);
    }

    private void writeCloseTag(OutputStream output)
            throws IOException {
        output.write("</minilog>".getBytes());
    }
}
