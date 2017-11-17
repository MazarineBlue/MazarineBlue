/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.mbt.gui;

public class StringConstants {

    static final String NEW_STATE = "New State";
    static final String NEW_TRANSITION = "New Transition";
    static final String CANT_BE_BLANK = "can't be blank";
    static final String INVALID_CHARACTERS_USED = "invalid characters used";
    static final String IS_ALREADY_ADDED = "is already added";
    static final String AFTER_STATE_DOESNT_SHARE_VIEW = "doesn't share a view with the before state";
    static final String BEFORE_STATE_DOESNT_SHARE_VIEW = "doesn't share a view with the after state";
    static final String FILLED_REGEX = "^.+$";
    static final String VALID_CHARACTERS_REGEX = "^[\\s\\w-]*$";
    static final String VALID_VARIABLE_CHARACTERS_REGEX = "^[\\s\\w$\\{\\}\\(\\)<>=|&-]*$";
    public static final int BUSINESS_VALUE_MIN = 0;
    public static final int BUSINESS_VALUE_MAX = 100;

    private StringConstants() {
    }
}
