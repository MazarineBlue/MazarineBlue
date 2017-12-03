/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.logger;

import java.util.regex.Pattern;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.util.DateFactory;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DateFactoryTest {

    @Test
    public void test() {
        DateFactory factory = DateFactory.newInstance();
        String format = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}";
        String actual = factory.getCurrentDate();
        assertTrue(Pattern.matches(format, actual));
    }
}
