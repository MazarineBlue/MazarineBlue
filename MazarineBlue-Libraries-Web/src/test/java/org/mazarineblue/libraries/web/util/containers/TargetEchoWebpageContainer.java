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
package org.mazarineblue.libraries.web.util.containers;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

public class TargetEchoWebpageContainer
        implements Container {

    @Override
    public void handle(Request in, Response out) {
        try {
            String str = in.getTarget().replace("%20", " ");
            String msg = String.format("<html><head><title>%s</title></head><body>%s</body></html>", str, str);
            out.getByteChannel().write(ByteBuffer.wrap(msg.getBytes()));
            out.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
