/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.mazarineblue.keyworddriven.Library;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestLibraryExternalCaller
        extends Library {

    private final Object callee;

    public TestLibraryExternalCaller(String namespace) {
        super(namespace);
        this.callee = new WeakReference<>(this);
    }

    public TestLibraryExternalCaller(String namespace, Object callee) {
        super(namespace);
        this.callee = new WeakReference<>(callee);
    }

    @Override
    protected Object getCaller() {
        return ((Reference) callee).get();
    }
}
