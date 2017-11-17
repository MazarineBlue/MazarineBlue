/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.logs;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.utililities.Timestamp;

/**
 * A {@code XmlLogBuilderLink} is a {@code Link} that builds a XML log by
 * collecting the event name, status, message and responce and combines with
 * the start date, end date and elapsed time in seconds.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class XmlLogBuilderLink
        extends Link {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<?xml-stylesheet type=\"text/xsl\" href=\"log.xsl\"?>";
    private static final long serialVersionUID = 1L;

    private final List<Record> list = new ArrayList<>(16);
    private final Timestamp timestamp;

    /**
     * Constructs a {@code XmlLogBuilderLink} with a utility to format the
     * start and end dates.
     *
     * @param timestamp the utility to use to format the dates.
     */
    public XmlLogBuilderLink(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "events logged = " + list.size();
    }

    @Override
    @EventHandler
    public void eventHandler(Event event) {
        list.add(new Record(event));
    }

    /**
     * Returns the complete XML content from all the evens that has passed so
     * far.
     *
     * @return the complete XML content.
     */
    public String toXml() {
        StringBuilder builder = new StringBuilder(HEADER);
        list.stream().forEach(r -> builder.append(r.toXml()));
        return builder.toString();
    }

    private class Record {

        private final Date start = new Date();
        private final Event event;

        Record(Event event) {
            this.event = event;
        }

        String toXml() {
            StringBuilder xml = new StringBuilder("<event>");
            xml.append(format("<name>%s</name>", event.getClass().getCanonicalName()));
            xml.append(format("<status>%s</status>", event.status()));
            xml.append(format("<message>%s</message>", event.message()));
            xml.append(format("<responce>%s</responce>", event.responce()));
            xml.append(format("<startDate>%s</startDate>", timestamp.getTimestamp(start)));
            xml.append(format("<endDate>%s</endDate>", timestamp.getTimestamp(event.dateConsumed())));
            xml.append(format("<elapsedTime>%d</elapsedTime>", diffInSeconds(start, event.dateConsumed())));
            return xml.append("</event>").toString();
        }

        private long diffInSeconds(Date start, Date end) {
            return end == null ? -1 : end.getTime() - start.getTime();
        }
    }

    @Override
    public int hashCode() {
        return 5 * 71 * 71
                + 71 * Objects.hashCode(this.list)
                + Objects.hashCode(this.timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.list, ((XmlLogBuilderLink) obj).list)
                && Objects.equals(this.timestamp, ((XmlLogBuilderLink) obj).timestamp);
    }
}
