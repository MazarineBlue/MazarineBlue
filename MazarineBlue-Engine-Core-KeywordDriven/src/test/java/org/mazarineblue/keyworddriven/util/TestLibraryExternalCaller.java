/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestLibraryExternalCaller
        extends Library {

    private final LibrarySpy spy;
    private final Object callee;

    public TestLibraryExternalCaller(String namespace) {
        super(namespace);
        this.spy = new LibrarySpy(namespace);
        this.callee = new WeakReference<>(this);
    }

    public TestLibraryExternalCaller(String namespace, Object callee) {
        super(namespace);
        this.spy = new LibrarySpy(namespace);
        this.callee = new WeakReference<>(callee);
    }

    public TestLibraryExternalCaller(String namespace, LibrarySpy spy) {
        super(namespace);
        this.spy = spy;
        this.callee = new WeakReference<>(this);
    }

    public TestLibraryExternalCaller(String namespace, LibrarySpy spy, Object callee) {
        super(namespace);
        this.spy = spy;
        this.callee = new WeakReference<>(callee);
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
        spy.doValidate(event);
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        spy.doBeforeExecution(event);
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        spy.doAfterExecution(event);
    }

    @Override
    protected Object getCaller() {
        return ((Reference) callee).get();
    }
}
