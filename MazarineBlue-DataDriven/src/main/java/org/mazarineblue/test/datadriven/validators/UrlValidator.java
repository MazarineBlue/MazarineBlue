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
package org.mazarineblue.test.datadriven.validators;

import java.util.logging.Level;
import org.mazarineblue.test.datadriven.ValidationContext;
import org.mazarineblue.test.datadriven.ValidationMessage;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class UrlValidator
        extends Validator {

    static private final String[] schemes = {"http", "https", "ftp", "ftps"};
    static private final org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(
            schemes);

    static public boolean isValid(String url) {
        return urlValidator.isValid(url);
    }

    public UrlValidator(Level level) {
        super(level);
    }

    @Override
    public ValidationMessage validate(Object data, ValidationContext context) {
        if (data == null)
            return new ValidationMessage(context, getTemplate(), Level.SEVERE);
        String str = data.toString();
        if (str == null || str.isEmpty())
            return new ValidationMessage(context, getTemplate(), Level.SEVERE);
        if (urlValidator.isValid(str) == false)
            return new ValidationMessage(context, getTemplate(), Level.SEVERE);
        return null;
    }

    @Override
    protected String getTemplate() {
        return getTemplate("validators.UrlValidator.template");
    }
}
