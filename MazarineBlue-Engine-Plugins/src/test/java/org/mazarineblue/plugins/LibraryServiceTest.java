/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.plugins;

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.util.DummyLibrary;
import static org.mazarineblue.plugins.LibraryService.getLibraries;
import static org.mazarineblue.plugins.LibraryService.hasLibrary;
import org.mazarineblue.plugins.exceptions.LibraryPluginException;
import org.mazarineblue.plugins.util.TestLibraryPlugin;

public class LibraryServiceTest {

    private static final String AVAILABLE = "namespace";
    private static final String NOT_AVAILABLE = "foo";
    private static final String THROWS_EXCEPTION = "exception";

    private Library library;

    @Before
    public void setup() {
        library = new DummyLibrary(AVAILABLE);
        PluginLoader.getInstance().injectPlugin(new TestLibraryPlugin(AVAILABLE, library));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
    }

    @Test
    public void hasLibrary_NotAvailable() {
        assertFalse(hasLibrary(NOT_AVAILABLE));
    }

    @Test
    public void hasLibrary_Available() {
        assertTrue(hasLibrary(AVAILABLE));
    }

    @Test(expected = LibraryPluginException.class)
    public void getLibrary_ThrowsException() {
        PluginLoader.getInstance().injectPlugin(new TestLibraryPlugin(THROWS_EXCEPTION, () -> {
                                                                  throw new RuntimeException();
                                                              }));
        getLibraries(THROWS_EXCEPTION);
    }

    @Test
    public void getLibrary_NotAvailable() {
        assertEquals(asList(), getLibraries(NOT_AVAILABLE));
    }

    @Test
    public void getLibrary_Available() {
        List<Library> libraries = getLibraries(AVAILABLE);
        assertEquals(asList(library), libraries);
    }
}
