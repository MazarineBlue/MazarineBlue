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
package org.mazarineblue.keyworddriven.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class PatternFixture {

    private final Pattern pattern;
    private final int start;
    private final int end;

    public PatternFixture(String regex, int start, int end) {
        pattern = Pattern.compile(regex);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

    public Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }

    public String getName(String group) {
        return group.substring(start, group.length() + end);
    }
}
