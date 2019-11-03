/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.libraries.web.tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mazarineblue.libraries.web.exceptions.TabNameTakenException;
import org.mazarineblue.libraries.web.exceptions.TabNotRegisteredForBrowserException;
import org.openqa.selenium.WebDriver;

/**
 * A TabRegistry contains a registry of all browser tab created.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TabRegistry {

    private Tab firstTab, currentTab, insertTab;
    private final Map<String, Tab> tabs = new HashMap<>();

    public TabRegistry(WebDriver driver, String initialTabName) {
        firstTab = currentTab = insertTab = new RealTab(initialTabName, driver.getWindowHandle());
        tabs.put(initialTabName, currentTab);
    }

    /**
     * Returns the tab with the specified name.
     *
     * @param name to look for
     * @return the tab with the specified name
     */
    public Tab getTab(String name) {
        if (!tabs.containsKey(name))
            throw new TabNotRegisteredForBrowserException(name);
        return tabs.get(name);
    }

    /**
     * Returns all tabs.
     *
     * @return a collection of all tabs
     */
    public Collection<Tab> getAllTabs() {
        List<Tab> list = new ArrayList<>(tabs.size());
        for (Tab tab = firstTab; RealTab.class.isAssignableFrom(tab.getClass()); tab = tab.next())
            list.add(tab);
        return list;
    }

    /**
     * Determines if the specificied tab is the current tab.
     *
     * @param tab the specified tab
     * @return {@literal true} if the specified tab is the current tab
     */
    public boolean isCurrentTab(Tab tab) {
        return tab.equals(currentTab);
    }

    /**
     * Gets the current tab.
     *
     * @return the current tab.
     */
    public Tab getCurrentTab() {
        return currentTab;
    }

    /**
     * Sets the specified tab as the current tab.
     *
     * @param tab the current tab to be
     */
    public void setCurrentTab(Tab tab) {
        currentTab = insertTab = tab;
    }

    /**
     * Returns the amount of tabs in this registry.
     *
     * @return the amount of tabs in this registry.
     */
    public int size() {
        return tabs.size();
    }

    /**
     * Registers a newly created tab right of the current tab.
     *
     * @param name   the name of the tab
     * @param handle the handle of the tab
     */
    public void insertTab(String name, String handle) {
        Tab tab = new RealTab(name, handle);
        linkTabsOnInsert(tab);
        registerTab(name, tab);
        insertTab = tab;
    }

    private void linkTabsOnInsert(Tab tab) {
        tab.setPreviousTab(insertTab).setNextTab(insertTab.next());
        tab.next().setPreviousTab(tab);
        tab.previous().setNextTab(tab);
    }

    private void registerTab(String tabName, Tab tab) {
        if (tabs.containsKey(tabName))
            throw new TabNameTakenException();
        tabs.put(tabName, tab);
    }

    /**
     * Removes the specified tab.
     *
     * @param name the name of the tab to remove.
     */
    public void removeTab(Tab tab) {
        if (currentTab.equals(tab))
            currentTab = currentTab.hasNext() ? currentTab.next() : currentTab.previous();
        linkBoundryTabs(tab);
        insertTab = currentTab;
    }

    private void linkBoundryTabs(Tab tab) {
        tab.next().setPreviousTab(tab.previous());
        tab.previous().setNextTab(tab.next());
    }
}
