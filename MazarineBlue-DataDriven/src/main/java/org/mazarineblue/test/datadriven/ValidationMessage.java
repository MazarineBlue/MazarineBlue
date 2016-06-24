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
package org.mazarineblue.test.datadriven;

import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ValidationMessage {

    private final String template;
    private final Level level;
    private final Date date;
    private final ValidationContext context;

    public ValidationMessage(ValidationContext context, String template,
                             Level level) {
        this.context = new ValidationContext(context);
        this.template = template;
        this.level = level;
        this.date = new Date();
    }

    @Override
    public String toString() {
        String msg = getMessage();
        return msg;
    }

    public String getMessage() {
        List<String> row = context.getRow();
        String column = context.getColumn();
        String[] args = context.getArguments();
        String msg = String.format(template, row.toString(), context.getColumn(), args);
        return msg;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValidationMessage) {
            ValidationMessage other = (ValidationMessage) obj;
            return equalsTemplates(other)
                    && equalsLevels(other)
                    && equalsContexts(other);
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in equals()">
    private boolean equalsTemplates(ValidationMessage other) {
        if (template == null)
            return other.template == null;
        return template.equals(other.template);
    }

    private boolean equalsLevels(ValidationMessage other) {
        if (level == null)
            return other.level == null;
        return level.equals(other.level);
    }

    private boolean equalsContexts(ValidationMessage other) {
        if (context == null)
            return other.context == null;
        return context.equals(other.context);
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.template);
        hash = 79 * hash + Objects.hashCode(this.level);
        return hash;
    }

    void print(PrintStream out) {
        out.println("Line: " + context.getRow());
        out.println("Columns: " + context.getColumn());
        out.println("Method: " + context.getMethod());
        out.println("Template: " + template);
        out.println("Level: " + level);
        out.println("--------------------------------------------------------");
    }
}
