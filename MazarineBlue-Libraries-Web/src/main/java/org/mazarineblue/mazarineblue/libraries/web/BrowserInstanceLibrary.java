/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mazarineblue.libraries.web;

import java.rmi.registry.Registry;
import java.util.Set;
import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.OnCurrentTabException;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.WindowHandleNotFoundException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class BrowserInstanceLibrary {

    private static final Duration NEW_TAB_TIMEOUT = Duration.FIVE_MINUTES;
    private static final CharSequence PAGE_RELOADED = "Detected a page unload event; script execution does not work across page loads.";

    private final WebDriver driver;
    private final Heuristics heuristics;

    private final TabRegistry tabRegistry;

    public BrowserInstanceLibrary(WebDriver driver) {
        this.driver = driver;
        this.heuristics = new Heuristics(driver);
        tabRegistry = new TabRegistry(driver);
    }

    public void open(String url) {
        try {
            driver.navigate().to(url);
        } catch (TimeoutException ex) {
            handleTimeoutException(ex);
        } finally {
            switchToDefaultContent();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for open(url)">
    private void handleTimeoutException(TimeoutException ex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
    //</editor-fold>

    public void openInNewTab(String url, String tabName) {
        Set<String> handles = driver.getWindowHandles();
        executeJavascript("window.open('" + url + "', '_blank');");
        await().atMost(NEW_TAB_TIMEOUT).until(() -> driver.getWindowHandles().size() > handles.size());
        tabRegistry.insertTab(tabName, newWindowHandle(handles));
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for openInNewTab(url, tabName)">
    private Object executeJavascript(String script, Object... arguments) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return executor.executeScript(script, arguments);
        } catch (WebDriverException ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.contains(PAGE_RELOADED))
                return executor.executeScript(script, arguments);
            else
                throw ex;
        }
    }

    private String newWindowHandle(Set<String> handles) {
        for (String handle : driver.getWindowHandles())
            if (!handles.contains(handle))
                return handle;
        throw new WindowHandleNotFoundException();
    }
    //</editor-fold>

    public void closeTab() {
        driver.close();
        tabRegistry.removeCurrentTab();
    }

    public void closeTab(String tabName) {
        if (tabRegistry.isCurrentTab(tabName))
            throw new OnCurrentTabException();
        closeTab(tabRegistry.getTab(tabName));
        tabRegistry.removeTab(tabName);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for closeTab()">
    private void closeTab(Tab tab) {
        String previousHandle = driver.getWindowHandle();
        driver.switchTo().window(tab.getHandle());
        driver.close();
        driver.switchTo().window(previousHandle);
    }
    //</editor-fold>

    void click(String url, String elementIdentifier) {
        WebElement e = heuristics.getElement(elementIdentifier);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void type(String url, String elementIdentifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
