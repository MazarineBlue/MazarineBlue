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
package org.mazarineblue.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class XmlUtil {

    static public String getVersion() {
        String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        output += System.getProperty("line.separator");
        return output;
    }

    static public String getStylesheet(String file) {
        String output = String.format(
                "<?xml-stylesheet type=\"text/xsl\" href=\"%s\"?>", file);
        output += System.getProperty("line.separator");
        return output;
    }

    static public String convertToPrettyFormat(String input)
            throws TransformerException {
        return convertToPrettyFormat(input, 2);
    }

    static public String convertToPrettyFormat(String input, int indent)
            throws TransformerException {
        try {
            return convertToPrettyFormat(input, indent, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    static public String convertToPrettyFormat(String input, int indent,
                                               String charsetName)
            throws TransformerException, UnsupportedEncodingException {
        Source source = new StreamSource(new StringReader(input));

        int n = input.length();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(n);
        StreamResult result = new StreamResult(buffer);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "" + indent);
        transformer.transform(source, result);

        return buffer.toString(charsetName);
    }
}
