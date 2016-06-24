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
package org.mazarineblue.test.datadriven.keywords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mazarineblue.keyworddriven.InstructionLine;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class Testcase {

    private Collection<InstructionLine> lines;
    private List<Testcase> dependencies;
    private String testcase;

    public Testcase(String testcase) {
        this.testcase = testcase;
    }

    public Testcase(Collection<InstructionLine> lines,
                    List<Testcase> dependencies) {
        this(lines);
        this.dependencies = dependencies;
    }

    public Testcase(Collection<InstructionLine> lines) {
        this.lines = lines;
        this.testcase = "";
    }

    public String getName() {
        return testcase;
    }

    void setInstructionLines(Collection<InstructionLine> lines) {
        this.lines = lines;
    }

    void addDependency(Testcase dependency) {
        if (dependencies == null)
            dependencies = new ArrayList();
        dependencies.add(dependency);
    }

    boolean isEmpty() {
        return lines.isEmpty();
    }

    Collection<InstructionLine> getInstructionLines() {
        return lines;
    }
}
