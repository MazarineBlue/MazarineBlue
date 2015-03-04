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
package org.mazarineblue.test.datadriven3.listeners;

import java.util.ArrayList;
import java.util.Collection;
import org.mazarineblue.test.datadriven3.registry.Profile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class GroupReportListeners implements ReportListener {

    private final Collection<ReportListener> group = new ArrayList();

    public boolean add(ReportListener reportListener) {
        return group.add(reportListener);
    }

    public boolean remove(ReportListener reportListener) {
        return group.remove(reportListener);
    }

    @Override
    public void setPassed(Profile profile) {
        for (ReportListener listener : group)
            listener.setPassed(profile);
    }

    @Override
    public void setFailed(Profile profile) {
        for (ReportListener listener : group)
            listener.setFailed(profile);
    }

    @Override
    public void setSkipped(Profile profile) {
        for (ReportListener listener : group)
            listener.setSkipped(profile);
    }
}
