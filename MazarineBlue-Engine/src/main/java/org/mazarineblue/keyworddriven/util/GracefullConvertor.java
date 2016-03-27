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
package org.mazarineblue.keyworddriven.util;

import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

/**
 * A {@code GracefullConvertor} converts a string with spaces to camel toe andalusia
 * back.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class GracefullConvertor {

    private final char[] input;
    private String output = "";
    private boolean firstLetterOrDigitSeen;
    private boolean nextCharCapitalized;

    private GracefullConvertor(String input, boolean firstCharCapitalized) {
        this.input = input.toCharArray();
        this.nextCharCapitalized = firstCharCapitalized;
    }

    public static String degraceNamespace(String namespace) {
        return namespace.toLowerCase();
    }

    public static String degraceKeyword(String input) {
        return isGracefull(input) ? input
                : new GracefullConvertor(input, false).convertKeyword();
    }

    private static boolean isGracefull(String input) {
        for (char c : input.toCharArray())
            if (!isGracefull(c))
                return false;
        return true;
    }

    private static boolean isGracefull(char c) {
        return isLetterOrDigit(c);
    }

    private String convertKeyword() {
        for (char c : input)
            appendInProperCase(c);
        return output;
    }

    private void appendInProperCase(char c) {
        if (isLetterOrDigit(c))
            output += Character.toString(convertToProperCase(c));
        if (firstLetterOrDigitSeen)
            nextCharCapitalized = !isGracefull(c);
    }

    private char convertToProperCase(char c) {
        firstLetterOrDigitSeen = true;
        return nextCharCapitalized ? toUpperCase(c) : toLowerCase(c);
    }
}
