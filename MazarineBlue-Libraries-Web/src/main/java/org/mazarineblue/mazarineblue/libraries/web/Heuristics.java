/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class Heuristics {

    private final WebDriver webDriver;

    Heuristics(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    WebElement getElement(String identifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
