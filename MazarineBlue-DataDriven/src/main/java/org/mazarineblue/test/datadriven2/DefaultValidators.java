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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.exceptions.IllegalSourceStateException;
import org.mazarineblue.test.datadriven3.annotations.Validator;
import org.mazarineblue.util.ResourceBundleRegistry;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
class DefaultValidators {

    @Validator("Not null")
    public boolean validateNotNull(DataSource source, ValidationContext context)
            throws IllegalSourceStateException {
        Object value = getData(source, context);
        if (value != null)
            return false;
        return addConflict(context, "datadriven.validate.notNull");
    }

    private Object getData(DataSource source, ValidationContext context) throws IllegalSourceStateException {
        String column = context.getColumn();
        return source.getData(column);
    }

    private boolean addConflict(ValidationContext context, String format) {
        String column = context.getColumn();
        String suite = context.getSuite();
        String test = context.getTestName();
        String template = ResourceBundleRegistry.getString("datadriven", format);
        String msg = String.format(template, suite, test, column);
        context.addConflict(msg);
        return true;
    }

    @Validator("Not a string")
    public boolean validateNoString(DataSource source, ValidationContext context)
            throws IllegalSourceStateException {
        if (validateNotNull(source, context))
            return true;

        Object value = getData(source, context);
        if (value instanceof String == false)
            return addConflict(context, "datadriven.validate.notAString");
        return false;
    }

    @Validator("Not empty")
    public boolean validateNotEmpty(DataSource source, ValidationContext context)
            throws IllegalSourceStateException {
        if (validateNoString(source, context))
            return true;

        Object value = getData(source, context);
        if (value.equals(""))
            return addConflict(context, "datadriven.validate.emptyString");
        return false;
    }

    @Validator("Pattern")
    public boolean validateNoPatternMatch(DataSource source, ValidationContext context)
            throws IllegalSourceStateException {
        if (validateNoString(source, context))
            return true;

        String str = (String) getData(source, context);
        String regex = context.getParameter(1, String.class);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
            return addConflict(context, "datadriven.validate.noRegexMatch");
        return false;
    }

    static private final String[] schemes = {"http", "https", "ftp", "ftps"};
    static private final org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(schemes);

    @Validator("URL")
    public boolean url(DataSource source, ValidationContext context) throws IllegalSourceStateException {
        if (validateNoString(source, context))
            return true;

        String str = (String) getData(source, context);
        if (urlValidator.isValid(str) == false)
            return addConflict(context, "datadriven.validate.noUrl");
        return false;
    }
}
