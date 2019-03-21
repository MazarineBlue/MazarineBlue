/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web;

class MiddleTab
        extends Tab {

    private final String name;
    private final String handle;
    private Tab previousTab;
    private Tab nextTab;

    MiddleTab(String name, String handle) {
        this.name = name;
        this.handle = handle;
        previousTab = new BorderTab(null, this);
        nextTab = new BorderTab(this, null);
    }

    @Override
    boolean containsName(String tabName) {
        return name.equals(tabName);
    }

    @Override
    String getName() {
        return name;
    }

    @Override
    String getHandle() {
        return handle;
    }

    @Override
    boolean hasPrevious() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    Tab previousTab() {
        return previousTab;
    }

    @Override
    void setPreviousTab(Tab tab) {
        this.previousTab = tab;
    }

    @Override
    boolean hasNext() {
        return nextTab != null;
    }

    @Override
    Tab nextTab() {
        return nextTab;
    }

    @Override
    void setNextTab(Tab tab) {
        this.nextTab = tab;
    }
}
