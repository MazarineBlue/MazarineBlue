/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web;

abstract class Tab {

    abstract boolean containsName(String tabName);

    abstract String getName();

    abstract String getHandle();

    abstract boolean hasPrevious();

    abstract Tab previousTab();

    abstract void setPreviousTab(Tab previousTab);

    abstract boolean hasNext();

    abstract Tab nextTab();

    abstract void setNextTab(Tab nextTab);
}
