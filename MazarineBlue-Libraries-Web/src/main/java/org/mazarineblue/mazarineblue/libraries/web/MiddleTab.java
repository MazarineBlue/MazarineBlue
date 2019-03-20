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
    private Tab previousTab = new LeftBorderTab();
    private Tab nextTab = new RightBorderTab();

    MiddleTab(String name, String handle) {
        this.name = name;
        this.handle = handle;
    }

    @Override
    boolean containsName(String tabName) {
        return name.equals(tabName);
    }

    @Override
    String getName() {
        return name;
    }

    String getHandle() {
        return handle;
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
