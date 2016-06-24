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
package org.mazarineblue.keyworddriven.librarymanager;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Processor;
import org.mazarineblue.keyworddriven.exceptions.InstructionUnaccessableException;
import org.mazarineblue.keyworddriven.exceptions.InvalidParameterException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Instruction {

    private static final String betaInstructionTxt = "Beta instruction - the usage of this instruction is discuraged";
    private static final String deprecatedInstructionTxt = "Deprecated instruction - the usage of this instruction is discuraged";
    private final WeakReference<Library> library;
    private final String keyword;
    private final MethodWrapper method;
    private boolean beta = false;
    private boolean deprecated = false;

    Instruction(Library library, Keyword keyword, Method method) {
        this.library = new WeakReference<>(library);
        this.keyword = keyword.value();
        this.method = new MethodWrapper(method,library.getNamespace() + "." + keyword);
    }

    void setBeta(boolean flag) {
        beta = flag;
    }

    void setDeprecated(boolean flag) {
        deprecated = flag;
    }

    @Override
    public String toString() {
        return keyword;
    }

    public Library getLibrary() {
        return library.get();
    }

    public String getPath() {
        return library.get().getNamespace() + "." + keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void validate(Object[] parameters) {
        if (beta)
            library.get().log().warning(betaInstructionTxt);
        if (deprecated)
            library.get().log().warning(deprecatedInstructionTxt);
        method.check(parameters);
    }

    public void invoke(Object[] parameters) {
        try {
            method.invoke(library.get(), parameters);
        } catch (IllegalAccessException ex) {
            throw new InstructionUnaccessableException(getPath());
        } catch (IllegalArgumentException ex) {
            throw new InvalidParameterException(getPath(), toString(parameters));
        } catch (InvocationTargetException ex) {
            String error = getErrorMessage(ex);
            throw Processor.convertException(error, ex);
        }
    }

    static private String toString(Object[] parameters) {
        String str = null;
        for (Object param : parameters)
            str = str == null
                    ? param.toString()
                    : str + ", " + param.toString();
        return str;
    }

    public String getErrorMessage(Throwable cause) {
        String format = "Instruction %s resulted in error: %s";
        String msg = cause.getMessage();
        if (msg == null)
            if (cause instanceof NullPointerException) {
                format = "Developer issue: Null pointer exception %s";
                msg = String.format(format, getPath());
            } else if (cause instanceof ArrayIndexOutOfBoundsException) {
                format = "Developer issue: Array index out of bounds at instruction %s";
                msg = String.format(format, getPath());
            } else
                msg = "sorry, no message available!";
        return String.format(format, getPath(), msg);
    }
}
