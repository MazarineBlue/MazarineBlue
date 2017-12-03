/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.exceptions;

import java.lang.reflect.Method;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;

/**
 * A {@code KeywordConflictException} is thrown by a {@link Library} when
 * trying to register an instruction for a {@link Keyword} that was already
 * taken, through either:
 * <ol type="a"><li>
 * using the {@link Keyword} annotation multiple times within a Library; or
 * </li><li>
 * trying to register a method for a keyword that already is taken.
 * </li></ol>
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library#Library(String)
 * @see Library#registerInstruction(String, Method, int)
 */
public class KeywordConflictException
        extends KeywordDrivenException {

    private static final long serialVersionUID = 1L;

    public KeywordConflictException(String keyword) {
        super("keyword: " + keyword);
    }
}
