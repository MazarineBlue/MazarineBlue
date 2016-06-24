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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven.validators.Validator;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public final class ValidationProfile {

    private String testcaseDescription;
    private final Map<String, Validator> map = new HashMap();

    ValidationProfile() {
    }

    public void setTestcaseDescription(String description) {
        this.testcaseDescription = description;
    }

    public String getTestcaseDescription() {
        return testcaseDescription;
    }

    public void addValidation(String column, Validator validator) {
        map.put(column, validator);
    }

    void validateLine(DataSource source, ValidationContext context, List<ValidationMessage> conflicts) {
        context.setRow(source.getLineIdentifiers());
        for (Map.Entry<String, Validator> entry : map.entrySet()) {
            String column = entry.getKey();
            context.setColumn(column);
            Object data = source.getData(column);
            Validator validator = entry.getValue();
            ValidationMessage message = validator.validate(data, context);
            if (message != null)
                conflicts.add(message);
        }
    }
}
