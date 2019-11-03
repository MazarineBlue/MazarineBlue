/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mazarineblue.libraries.web.exceptions.NoPortsAvailableException;

public class Ports {

    public static final int MIN = 1024;
    public static final int MAX = 49152;

    private Ports() {
    }

    public static int freePort() {
        return freePortWithinRange(MIN, MAX);
    }

    public static int freePortWithinRange(int min, int max) {
        List<Integer> ports = generateShuffeledList(min, max - min + 1);
        for (int port : ports)
            if (available(port))
                return port;
        throw new NoPortsAvailableException(min, max);
    }

    private static List<Integer> generateShuffeledList(int min, int n) {
        List<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; ++i)
            list.add(min + i);
        Collections.shuffle(list);
        return list;
    }

    private static boolean available(final int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            try (DatagramSocket dataSocket = new DatagramSocket(port)) {
                dataSocket.setReuseAddress(true);
                return true;
            }
        } catch (IOException | RuntimeException ex) {
            // An exception means the port is not available.
        }
        return false;
    }
}
