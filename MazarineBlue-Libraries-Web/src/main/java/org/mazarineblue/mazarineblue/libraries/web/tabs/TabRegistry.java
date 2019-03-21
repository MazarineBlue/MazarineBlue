/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web.tabs;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.TabNameTakeException;
import org.openqa.selenium.WebDriver;

/**
 * A TabRegistry contains a registry of all browser tab created.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TabRegistry {

    private static final String INITIAL_TAB_NAME = "Main tab";

    private Tab currentTab, insertTab;
    private final Map<String, MiddleTab> tabs = new HashMap<>();

    public TabRegistry(WebDriver driver) {
        currentTab = insertTab = new MiddleTab(INITIAL_TAB_NAME, driver.getWindowHandle());
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

    public void removeCurrentTab() {
        tabs.remove(currentTab.getName());
        currentTab.nextTab().setPreviousTab(currentTab.previousTab());
        currentTab.previousTab().setNextTab(currentTab.nextTab());

        if (currentTab.hasNext())
            currentTab = currentTab.nextTab();
        else
            currentTab = currentTab.previousTab();
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

    public boolean isCurrentTab(String name) {
        return currentTab.getName().equals(name);
    }

    public void removeTab(String name) {
        Tab tab = tabs.remove(name);
        tab.nextTab().setPreviousTab(tab.previousTab());
        tab.previousTab().setNextTab(tab.nextTab());
    }
}
