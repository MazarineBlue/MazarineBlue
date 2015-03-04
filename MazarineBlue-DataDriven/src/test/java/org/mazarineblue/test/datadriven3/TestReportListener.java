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
package org.mazarineblue.test.datadriven3;

import org.mazarineblue.test.datadriven3.listeners.ReportListener;
import org.mazarineblue.test.datadriven3.registry.Profile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class TestReportListener implements ReportListener {

    private int passed = 0, failed = 0, skipped = 0;

    public TestReportListener() {
    }

    @Override
    public void setPassed(Profile profile) {
        ++passed;
    }

    @Override
    public void setFailed(Profile profile) {
        ++failed;
    }

    @Override
    public void setSkipped(Profile profile) {
        --skipped;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getSkipped() {
        return skipped;
    }
}
