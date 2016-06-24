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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ExceptionResult
        extends StatusLeaf {

    private final Throwable ex;

    public ExceptionResult(Status status, Throwable ex) {
        super(status, new Date());
        this.ex = ex;
    }

    @Override
    public String toString() {
        return getStatus() + " : " + ex.getMessage();
    }

    public String getMessage() {
        return ex.getMessage();
    }

    public void writeStackTrace(OutputStream out) {
        PrintStream s = new PrintStream(out);
        ex.printStackTrace(s);
    }

    @Override
    public void accept(LogVisitor visitor)
            throws IOException {
        visitor.exception(this);
    }
}
