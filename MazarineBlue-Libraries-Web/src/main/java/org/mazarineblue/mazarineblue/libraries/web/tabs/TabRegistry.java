/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web.tabs;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.TabNameTakenException;
import org.openqa.selenium.WebDriver;

/**
 * A TabRegistry contains a registry of all browser tab created.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TabRegistry {

    private static final String INITIAL_TAB_NAME = "Main tab";

    private Tab currentTab, insertTab;
    private final Map<String, RealTab> tabs = new HashMap<>();

    public TabRegistry(WebDriver driver) {
        currentTab = insertTab = new RealTab(INITIAL_TAB_NAME, driver.getWindowHandle());
    }

    /**
     * Returns the tab with the specified name.
     *
     * @param name to look for
     * @return the tab with the specified name
     */
    public Tab getTab(String name) {
        return tabs.get(name);
    }

    public void insertTab(String name, String handle) {
        RealTab tab = new RealTab(name, handle);
        tab.setPreviousTab(insertTab).setNextTab(insertTab.nextTab());
        tab.nextTab().setPreviousTab(tab);
        tab.previousTab().setNextTab(tab);
        insertTab = tab;
        registerTab(name, tab);
    }

    private void registerTab(String tabName, RealTab tab) {
        if (tabs.containsKey(tabName))
            throw new TabNameTakenException();
        tabs.put(tabName, tab);
    }

    public void setCurrentTab(Tab tab) {
        currentTab = insertTab = tab;
    }

    public void removeCurrentTab() {
        removeTab(currentTab);
    }

    public void removeTab(String name) {
        removeTab(tabs.remove(name));
    }
    
    private void removeTab(Tab tab) {
        if (currentTab.equals(tab))
            currentTab = currentTab.hasNext() ? currentTab.nextTab() : currentTab.previousTab();
        tab.nextTab().setPreviousTab(tab.previousTab());
        tab.previousTab().setNextTab(tab.nextTab());
        insertTab = currentTab;
    }
}
