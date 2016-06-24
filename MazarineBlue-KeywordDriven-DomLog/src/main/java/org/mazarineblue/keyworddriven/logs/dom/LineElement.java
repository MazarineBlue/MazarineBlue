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
import java.util.Date;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LineElement
        extends Composite {

    private final InstructionLine line;
    private final ProcessingType processingType;
    private Object[] actualParameter;

    public LineElement(InstructionLine line, Date startDate, ProcessingType processingType) {
        super(startDate);
        this.line = line;
        this.processingType = processingType;
    }

    @Override
    public String toString() {
        return getStatus() + " : " + line;
    }

    public ProcessingType getProcessingType() {
        return processingType;
    }

    public InstructionLine getInstructionLine() {
        return line;
    }

    public String getLineIdentifier() {
        return line.getLineIdentifier();
    }

    public String getPath() {
        return line.getPath();
    }

    public String getNamespace() {
        return line.getNamespace();
    }

    public String getKeyword() {
        return line.getKeyword();
    }

    public String getParameters() {
        Object[] parameter = line.getParameters();
        if (parameter.length == 0)
            return "";
        String str = "(" + getParameter(0, parameter);
        for (int i = 1; i < parameter.length; ++i)
            str += ", " + getParameter(i, parameter);
        return str + ")";
    }

    private String getParameter(int index, Object[] parameter) {
        String str = parameter[index].toString();
        if (skipActualParameters(index, parameter))
            return str;
        str += " => '" + actualParameter[index] + "'";
        return str;
    }

    private boolean skipActualParameters(int index, Object[] parameter) {
        if (actualParameter == null || index >= actualParameter.length)
            return true;
        if (actualParameter[index] == null || actualParameter[index].equals(""))
            return true;
        if (parameter[index].equals(actualParameter[index]))
            return true;
        return false;
    }

    public void setActualParameters(Object[] actualParameter) {
        Object[] parameter = line.getParameters();
        this.actualParameter = actualParameter;
    }

    /*
     * public Object[] getActualParameters() { if (actualParameter != null)
     * return actualParameter;
     *
     * Object[] arr = new Object[parameter.length]; for (int i = 0; i <
     * arr.length; ++i) arr[i] = ""; return arr; }
     */
    @Override
    public void accept(LogVisitor visitor)
            throws IOException {
        visitor.openLine(this);
        super.accept(visitor);
        visitor.endLine(this);
    }
}
