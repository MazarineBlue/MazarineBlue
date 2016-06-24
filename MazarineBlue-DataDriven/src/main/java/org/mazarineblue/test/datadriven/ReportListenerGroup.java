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
package org.mazarineblue.test.datadriven;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ReportListenerGroup
        implements ReportListener {

    private final List<ReportListener> listeners = new ArrayList<>();

    void add(ReportListener reportListener) {
        listeners.add(reportListener);
    }

    void remove(ReportListener reportListener) {
        listeners.remove(reportListener);
    }

    @Override
    public void setStatus(boolean result, String platform, String suite,
                          String testcase) {
        for (ReportListener listener : listeners)
            listener.setStatus(result, platform, suite, testcase);
    }

    @Override
    public void setPassed(String platform, String suite, String testcase) {
        for (ReportListener listener : listeners)
            listener.setPassed(platform, suite, testcase);
    }

    @Override
    public void setFailed(String platform, String suite, String testcase) {
        for (ReportListener listener : listeners)
            listener.setFailed(platform, suite, testcase);
    }

    @Override
    public void setStatus(boolean result, String platform, String suite,
                          String testcase, String step) {
        for (ReportListener listener : listeners)
            listener.setStatus(result, platform, suite, testcase, step);
    }

    @Override
    public void setPassed(String platform, String suite, String testcase,
                          String step) {
        for (ReportListener listener : listeners)
            listener.setPassed(platform, suite, testcase, step);
    }

    @Override
    public void setFailed(String platform, String suite, String testcase,
                          String step) {
        for (ReportListener listener : listeners)
            listener.setFailed(platform, suite, testcase, step);
    }
}
