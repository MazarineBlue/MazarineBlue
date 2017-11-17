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
package org.mazarineblue.procedures.exceptions;

import org.mazarineblue.procedures.ProcedureLink;
import org.mazarineblue.procedures.events.CallProcedureEvent;
import org.mazarineblue.utililities.Immutable;

/**
 * A {@code ProcedureNotFoundException} is thrown by {@link ProcedureLink} when
 * a {@link CallProcedureEvent} was published but there ware no procedure
 * registered under the specified name.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@Immutable
public class ProcedureNotFoundException
        extends ProcedureException {

    private static final long serialVersionUID = 1L;

    public ProcedureNotFoundException(String name) {
        super(name);
    }
}
