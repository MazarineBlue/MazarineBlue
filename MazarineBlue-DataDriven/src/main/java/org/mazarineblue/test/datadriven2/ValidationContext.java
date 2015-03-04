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
package org.mazarineblue.test.datadriven2;

import java.util.List;
import org.mazarineblue.test.datadriven3.annotations.Validation;
import org.mazarineblue.test.report.Report;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class ValidationContext extends AbstractContext {

    private List<ValidationMessage> conflicts;
    private Validation validation;

    ValidationContext(Report report, String suite) {
        super(report, suite);
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public Validation getValidation() {
        return validation;
    }

    void resetValidation() {
        validation = null;
    }

    void setConflictList(List<ValidationMessage> conflicts) {
        this.conflicts = conflicts;
    }

    public List<ValidationMessage> getConflictList() {
        return conflicts;
    }

    void addConflict(String msg) {
//        conflicts.add(new ValidationMessage(msg));
    }

    public Validation[] getValidations() {
        Class<?> clazz = getMethod().getClass();
        return getMethod().getAnnotationsByType(Validation.class);
    }

    String getColumn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    <T> T getParameter(int parameter, Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
