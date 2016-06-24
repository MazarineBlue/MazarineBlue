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
import org.mazarineblue.util.ResourceBundleRegistry;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Validator {

    private String template;
    protected final Level level;

    Validator(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public abstract ValidationMessage validate(Object obj,
                                               ValidationContext context);

    protected String getTemplate() {
        return getTemplate(null);
    }

    protected String getTemplate(String key) {
        if (key == null)
            key = "validators.Validator.template";
        String avoidAcess = template;
        if (avoidAcess == null)
            synchronized (ResourceBundleRegistry.class) {
                avoidAcess = template;
                if (avoidAcess == null)
                    avoidAcess = template = ResourceBundleRegistry.getString(
                            "datadriven", key);
            }
        return avoidAcess;
    }

}
