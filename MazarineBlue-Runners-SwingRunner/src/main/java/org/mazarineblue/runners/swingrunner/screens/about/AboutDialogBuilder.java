/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.awt.Frame;
import java.util.ResourceBundle;
import static java.util.ResourceBundle.getBundle;
import org.mazarineblue.runners.swingrunner.exceptions.ImageFetcherNotSetException;
import org.mazarineblue.runners.swingrunner.exceptions.ResourceBundlerNotSetException;

public class AboutDialogBuilder {

    private final Frame owner;
    private ImageFetcher fetcher;
    private ResourceBundle bundle;

    public AboutDialogBuilder() {
        this(null);
    }

    public AboutDialogBuilder(Frame owner) {
        this.owner = owner;
    }

    public Frame getOwner() {
        return owner;
    }

    public void validate() {
        if (fetcher == null)
            throw new ImageFetcherNotSetException();
        if (bundle == null)
            throw new ResourceBundlerNotSetException();
    }

    public AboutDialogBuilder setImageFetcher(ImageFetcher fetcher) {
        this.fetcher = fetcher;
        return this;
    }

    public AboutDialogBuilder setResourceBundle(String baseName) {
        this.bundle = baseName == null ? null : getBundle(baseName);
        return this;
    }

    ImageFetcher getFetcher() {
        return fetcher;
    }

    ResourceBundle getResourceBundle() {
        return bundle;
    }
}
