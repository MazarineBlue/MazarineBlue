/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.TabNameTakeException;
import org.openqa.selenium.WebDriver;

class TabRegistry {

    private static final String INITIAL_TAB_NAME = "Main tab";

    private Tab currentTab, insertTab;
    private final Map<String, MiddleTab> tabs = new HashMap<>();

    TabRegistry(WebDriver driver) {
        currentTab = new MiddleTab(INITIAL_TAB_NAME, driver.getWindowHandle());
    }

    Tab getTab(String tabName) {
        return tabs.get(tabName);
    }

    void insertTab(String name, String handle) {
        registerTab(name, new MiddleTab(name, handle));

        if (insertTab == null)
            insertTab = currentTab.nextTab();
        insertTab.nextTab().setPreviousTab(insertTab.previousTab());
        insertTab.previousTab().setNextTab(insertTab.nextTab());
    }

    private void registerTab(String tabName, MiddleTab tab) {
        if (tabs.containsKey(tabName))
            throw new TabNameTakeException();
        tabs.put(tabName, tab);
    }

    void removeCurrentTab() {
        unregisterTab(currentTab.getName());
        currentTab.nextTab().setPreviousTab(currentTab.previousTab());
        currentTab.previousTab().setNextTab(currentTab.nextTab());

        if (currentTab.hasNext())
            currentTab = currentTab.nextTab();
        else
            currentTab = currentTab.previousTab();
    }

    private void unregisterTab(String tabName) {
        tabs.remove(tabName);
    }

    //<editor-fold defaultstate="collapsed" desc="Tab finder methods">
    private Tab firstTab() {
        Tab find = currentTab;
        while (find.hasPrevious())
            find = find.previousTab();
        return find;
    }

    private Tab lastTab() {
        Tab find = currentTab;
        while (find.hasNext())
            find = find.nextTab();
        return find;
    }
    //</editor-fold>

    boolean isCurrentTab(String tabName) {
        return currentTab.getName().equals(tabName);
    }

    void removeTab(String name) {
        Tab tab = tabs.get(name);
        tab.nextTab().setPreviousTab(tab.previousTab());
        tab.previousTab().setNextTab(tab.nextTab());
        unregisterTab(name);
    }
}
