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
public class NotEmptyValidator
        extends Validator {

    public NotEmptyValidator(Level level) {
        super(level);
    }

    @Override
    public ValidationMessage validate(Object obj, ValidationContext context) {
        if (obj == null)
            return new ValidationMessage(context, getTemplate(), level);
        String str = obj.toString();
        if (str.isEmpty())
            return new ValidationMessage(context, getTemplate(), level);
        return null;
    }

    @Override
    protected String getTemplate() {
        return getTemplate("validators.NotEmptyValidator.template");
    }
}
