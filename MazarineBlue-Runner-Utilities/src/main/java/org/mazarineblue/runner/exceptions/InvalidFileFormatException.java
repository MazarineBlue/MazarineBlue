/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.runner.exceptions;

import org.mazarineblue.keyworddriven.exceptions.ConsumableException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InvalidFileFormatException
        extends ConsumableException {

    public InvalidFileFormatException(Throwable cause) {
        super(cause);
    }
}
