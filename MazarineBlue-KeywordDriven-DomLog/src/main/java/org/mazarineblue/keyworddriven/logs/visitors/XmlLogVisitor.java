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
package org.mazarineblue.keyworddriven.logs.visitors;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.lang.StringEscapeUtils;
import org.mazarineblue.keyworddriven.logs.dom.CollectionElement;
import org.mazarineblue.keyworddriven.logs.dom.Component;
import org.mazarineblue.keyworddriven.logs.dom.ExceptionResult;
import org.mazarineblue.keyworddriven.logs.dom.LineElement;
import org.mazarineblue.keyworddriven.logs.dom.MessageResult;
import org.mazarineblue.util.Timestamp;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class XmlLogVisitor
        implements LogVisitor {

    private final OutputStream output;
    private boolean even = false;
    private final Timestamp timestamp = Timestamp.getDefaultInstance();

    public XmlLogVisitor(OutputStream output) {
        this.output = output;
    }

    @Override
    public void startCollection(CollectionElement collection)
            throws IOException {
        write("<collection status='%s' startDate='%s' endDate='%s'>",
              getStatus(collection),
              timestamp.getTimestamp(collection.getStartDate()),
              timestamp.getTimestamp(collection.getEndDate()));
    }

    @Override
    public void endCollection(CollectionElement collection)
            throws IOException {
        write("</collection>");
    }

    @Override
    public void openLine(LineElement line)
            throws IOException {
        write("<line processingType='%s' status='%s' path='%s' even='%s'>",
              line.getProcessingType().name().toLowerCase(),
              getStatus(line), line.getPath(),
              even ? "even" : "uneven");
        writeEscape("<lineIdentifier>%s</lineIdentifier>",
                    line.getLineIdentifier());
        write("<startDate>%s</startDate>", timestamp.getTimestamp(
              line.getStartDate()));
        write("<endDate>%s</endDate>", timestamp.getTimestamp(line.getEndDate()));
        write("<elapsedTime>%s</elapsedTime>", line.getElapsedTime());
        writeEscape("<namespace>%s</namespace>", line.getNamespace());
        writeEscape("<keyword>%s</keyword>", line.getKeyword());
        writeEscape("<parameters>%s</parameters>", line.getParameters());
        even = !even;
    }

    @Override
    public void endLine(LineElement line)
            throws IOException {
        write("</line>");
    }

    @Override
    public void message(MessageResult msg)
            throws IOException {
        String status = getStatus(msg);
        String message = msg.getMessage();
        write("<message status='%s' even='%s'>", status,
              even ? "even" : "uneven");
        write("<date>%s</date>", timestamp.getTimestamp(msg.getStartDate()));
        writeEscape("<value>%s</value>", message);
        write("</message>");
        even = !even;
    }

    @Override
    public void exception(ExceptionResult ex)
            throws IOException {
        String status = getStatus(ex);
        String message = ex.getMessage();
        write("<exception status='%s' even='%s'>", status,
              even ? "even" : "uneven");
        write("<date>%s</date>", timestamp.getTimestamp(ex.getStartDate()));
        writeEscape("<value>%s</value>", message);
        writeStackTrace(ex);
        write("</exception>");
        even = !even;
    }

    private void writeStackTrace(ExceptionResult ex)
            throws IOException {
        write("<stacktrace><![CDATA[");
        ex.writeStackTrace(output);
        write("]]></stacktrace>");
    }

    private void writeEscape(String format, Object... args)
            throws IOException {
        for (int i = 0; i < args.length; ++i)
            if (args[i] instanceof String)
                args[i] = StringEscapeUtils.escapeXml((String) args[i]);
        write(format, args);
    }

    private void write(String format, Object... args)
            throws IOException {
        String str = String.format(format, args);
        output.write(str.getBytes());
    }

    private String getStatus(Component leaf) {
        return leaf.getStatus().name().toLowerCase();
    }
}
