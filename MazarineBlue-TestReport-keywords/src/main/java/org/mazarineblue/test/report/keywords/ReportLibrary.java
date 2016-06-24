/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.test.report.keywords;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.test.events.GetPlatformsFromReportEvent;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.UnwritableFileException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.logs.dom.Composite;
import org.mazarineblue.test.events.MiniLogEvent;
import org.mazarineblue.test.links.MiniLogLink;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.report.exceptions.ReportNotFoundException;
import org.mazarineblue.test.report.visitors.TestObjectVisitorException;
import org.mazarineblue.util.XmlUtil;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ReportLibrary
        extends Library {

    private final Map<String, Report> reports = new HashMap<>(4);
    private String activeName;
    private Report activeReport;
    private InnerReportLibrary innerLevelLibrary;

    public ReportLibrary() {
        super("org.mazarineblue.test.report");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    Report getActiveReport() {
        return activeReport;
    }

    @Keyword("Report")
    @Parameters(min = 2)
    public void report(String name, String title, String[] platforms) {
        Report report = new Report(title, platforms);
        reports.put(name, report);
    }

    @EventHandler
    public void eventHandler(GetPlatformsFromReportEvent event) {
        String key = event.getReport();
        String[] platforms = key == null
                ? activeReport.getPlatforms()
                : reports.get(key).getPlatforms();
        event.setPlatforms(platforms);
    }

    @EventHandler
    public void eventHandler(MiniLogEvent event)
            throws IOException {
        MiniLogLink link = event.getLog();
        for (MiniLogLink.Key key : link.getKeys()) {
            String suite = key.getSuite();
            String testcase = key.getTestcase();
            Composite composite = link.getComposite(key);
            activeReport.addTicket(new MiniLogTicket(key, composite), suite,
                                   testcase);
        }
    }

    @Keyword("Select report")
    @Parameters(min = 1, max = 1)
    public void selectReport(String name) {
        if (reports.containsKey(name) == false)
            throw new ReportNotFoundException(name);
        activeName = name;
        activeReport = reports.get(name);
        if (innerLevelLibrary == null) {
            innerLevelLibrary = new InnerReportLibrary(this);
            innerLevelLibrary.setup(this);
            executor().libraries().register(innerLevelLibrary);
        }
    }

    @Keyword("Save report as XML")
    @Parameters(min = 1, max = 1)
    public void saveAsXml(String filename) {
        String input = fetchInput();
        writeReport(filename, input);
    }
    
    private String fetchInput() {
        String input = activeReport.toXml();
        try {
            input = XmlUtil.convertToPrettyFormat(input);
        } catch (TransformerException ex) {
            Logger.getLogger(ReportLibrary.class.getName()).log(Level.SEVERE,
                                                                null, ex);
        }
        return input;
    }
    
    private void writeReport(String filename, String input) {
        DocumentMediator mediator = documentMediator();
        String folder = mediator.getDefaultFolder(executor().getStartDate());
        try {
            mediator.writeReportOutput(folder, filename, input);
        } catch (IOException ex) {
            throw new UnwritableFileException(
                    "Folder: " + folder + "Filename: " + filename);
        }
    }
}
