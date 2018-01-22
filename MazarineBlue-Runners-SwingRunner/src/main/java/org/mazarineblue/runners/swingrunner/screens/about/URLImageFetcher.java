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
package org.mazarineblue.runners.swingrunner.screens.about;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import static java.util.logging.Level.SEVERE;
import javax.imageio.ImageIO;
import org.mazarineblue.runners.swingrunner.util.ExceptionReporter;

/**
 * An {@code URLImageFetcher} is a {@code ImageFetcher} that fetches an image
 * though the user of an URL. If the logo can not be read, then another
 * {@code ImageFetcher} is asked for the logo.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class URLImageFetcher
        implements ImageFetcher {

    private final URL url;
    private final ImageFetcher backupFetcher;
    private final ExceptionReporter reporter;

    /**
     * Constructs the {@code URLImageFetcher} with a specified URL as the main
     * source and a specified backup source.
     *
     * @param url           the main source for the logo.
     * @param backupFetcher the backup source for the logo.
     * @param logger        the logger wrapper for exceptions.
     */
    public URLImageFetcher(URL url, ImageFetcher backupFetcher, ExceptionReporter logger) {
        this.url = url;
        this.backupFetcher = backupFetcher;
        this.reporter = logger;
    }

    @Override
    public Image getImage() {
        try {
            return ImageIO.read(url);
        } catch (IOException ex) {
            reporter.log(SEVERE, null, ex);
            return backupFetcher.getImage();
        }
    }
}
