/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.keyworddriven.exceptions.ProcessorRunningException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MailFilterTest extends LibraryTestTemplate {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setup() throws Exception {
        addInstruction("", "Import", "org.mazarineblue.mail");
        execute();
    }

    @After
    public void teardown() {
    }

    @Test
    public void testMailFilter() throws Exception {
        String filterName = "filterName";
        addInstruction("", "Mail filter", filterName);
        execute();

        MailLibrary lib = fetchLibrary(MailLibrary.class);
        assertEquals(true, lib.isActiveMailFilter(filterName));
        addInstruction("", "End mail filter");
        execute();
        assertEquals(false, lib.isActiveMailFilter(filterName));
        assertEquals(true, lib.containsMailFilterProfile(filterName));
    }

    @Test(expected = ProcessorRunningException.class)
    public void testMailFilterIdentifierEmptyString() throws Exception {
        addInstruction("", "Mail filter", "");
        addInstruction("", "From", "testsutrecht@gmail.com");
        addInstruction("", "End mail filter");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testMailFilterProfileIdentifierAlreadyUsed() throws Exception {
        for (int v = 0; v < 2; v++) {
            addInstruction("", "Mail filter", "filterName");
            addInstruction("", "From", "testsutrecht@gmail.com");
            addInstruction("", "End mail filter");
        }
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testMailFilterProfileCalledTwice() throws Exception {
        for (int v = 0; v < 2; v++) {
            addInstruction("", "Mail filter", "filterName");
            addInstruction("", "From", "testsutrecht@gmail.com");
        }
        addInstruction("", "End mail filter");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testEndMailFilterWhenNotStarted() throws Exception {
        addInstruction("", "End mail filter");
        execute();
    }
}
