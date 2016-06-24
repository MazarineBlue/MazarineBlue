/*
 * Copyright (c) 2015 Specialisterren
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.test.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Ticket {

    private String suite, testcase, platform;

    public Ticket(String suite, String testcase, String platform) {
        this.suite = suite;
        this.testcase = testcase;
        this.platform = platform;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite == null ? "" : suite;
    }

    public String getTestcase() {
        return testcase;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase == null ? "" : testcase;
    }

    public String getXml() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            writeOpenTag(output);
            writeNestedXml(output);
            writeCloseTag(output);
            return output.toString();
        } catch (IOException ex) {
            throw new AssertionError(
                    "Developer issue: error while writing to report.");
        }
    }

    private void writeOpenTag(OutputStream output)
            throws IOException {
        String format = "<ticket suite='%s' testcase='%s' platform='%s'>";
        String msg = String.format(format, suite, testcase, platform);
        output.write(msg.getBytes());
    }

    private void writeCloseTag(OutputStream output)
            throws IOException {
        output.write("</ticket>".getBytes());
    }

    protected abstract void writeNestedXml(OutputStream output)
            throws IOException;
}
