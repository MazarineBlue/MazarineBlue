/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.plugins;

import java.io.File;

public class SheetNotFoundException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SheetNotFoundException(File file, String sheet) {
        super("file=" + file + " sheet=" + sheet);
    }

    public SheetNotFoundException(File file) {
        super("file=" + file);
    }
}
