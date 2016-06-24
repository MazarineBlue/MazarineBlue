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
package org.mazarineblue.keyworddriven.librarymanager;

import java.util.Collection;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.ProcedureOpenOnStartException;
import org.mazarineblue.keyworddriven.links.RecordingLibraryLink;
import org.mazarineblue.keyworddriven.proceduremanager.Procedure;
import org.mazarineblue.keyworddriven.proceduremanager.ProcedureManager;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ProcedureLibraryLink
        extends RecordingLibraryLink {

    private final ProcedureManager procedureManager;
    private final String name;
    private final String[] parameters;
    boolean running = true;

    public ProcedureLibraryLink(Library library,
                                ProcedureManager procedureManager, String name,
                                String... parameters) {
        super(library);
        this.procedureManager = procedureManager;
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    @Keyword("Procedure")
    @Parameters(min = 1)
    public void procedure(String name, String... paramters) {
        throw new ProcedureOpenOnStartException(name);
    }

    @Keyword("End procedure")
    @Parameters(min = 0, max = 0)
    public void endProcedure() {
        executor().chain().remove(this);
        Collection<InstructionLine> instructions = getRecording();
        Procedure procedure = new Procedure(parameters, instructions);
        procedureManager.register(name, procedure);
        running = false;
    }

    public boolean recoding() {
        return running;
    }
}
