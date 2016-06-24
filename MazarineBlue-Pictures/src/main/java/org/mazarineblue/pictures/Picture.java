/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.pictures;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.mazarineblue.pictures.compounders.comperator.FullPixelComperator;
import org.mazarineblue.pictures.compounders.PixelComperator;
import org.mazarineblue.pictures.compounders.EqualCompounder;
import org.mazarineblue.pictures.compounders.ScreenshotsCompounder;
import org.mazarineblue.pictures.exceptions.PictureMissingException;
import org.mazarineblue.pictures.compounders.CompoundMethod;
import org.mazarineblue.pictures.compounders.methods.DiffFilter;
import org.mazarineblue.pictures.compounders.methods.SameFilter;
import org.mazarineblue.pictures.exceptions.DimensionMissingException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class Picture {

    static private final int BUFFER_CAPACITY = 4096;
    static protected final int THUMNNAIL_WIDTH = 200;

    private final Raster raster;

    protected Picture(Raster raster) {
        this.raster = raster;
    }

    public abstract byte[] getData();

    public Dimension getDimension() {
        return new Dimension(raster.width, raster.height);
    }

    public final int getWidth() {
        return raster.width;
    }

    public final int getHeight() {
        return raster.height;
    }

    public Picture clip(Dimension dimension)
            throws IOException {
        if (dimension == null)
            throw new DimensionMissingException();
        if (isThisImageSmallerOrEqual(dimension))
            return this;
        
        Raster raster = new Raster(dimension.width, dimension.height);
        raster.copyAndClip(this.raster);
        return createPicture(raster);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for clip()">
    private boolean isThisImageSmallerOrEqual(Dimension dimension) {
        return raster.width <= dimension.width
                && raster.height <= dimension.height;
    }
    // </editor-fold>

    public abstract Picture fetchThumbnail()
            throws IOException ;

    public abstract void write(OutputStream output)
            throws IOException;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Arrays.hashCode(this.raster.rgbArray);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            return equals((Picture) obj);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // Never thrown
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for equals()">
    private boolean equals(Picture other)
            throws IOException {
        return compare(other) == EqualCompounder.Result.EQUAL;
    }
    // </editor-fold>

    public final EqualCompounder.Result compare(Picture right)
            throws IOException {
        return compare(right, new FullPixelComperator());
    }

    public final EqualCompounder.Result compare(Picture right,
                                                PixelComperator pixelComperator)
            throws IOException {
        if (right == null)
            return EqualCompounder.Result.FAIL;
        EqualCompounder check = new EqualCompounder(raster, right.raster,
                                                    pixelComperator);
        check.computeScreenshot();
        return check.getComperation();
    }

    public Picture diff(Picture right)
            throws IOException {
        return diff(right, new FullPixelComperator());
    }

    public Picture diff(Picture right, PixelComperator comperator)
            throws IOException {
        return compound(right, new DiffFilter(comperator));
    }

    public Picture same(Picture right)
            throws IOException {
        return same(right, new FullPixelComperator());
    }

    public Picture same(Picture right, PixelComperator comperator)
            throws IOException {
        return compound(right, new SameFilter(comperator));
    }

    public final Picture compound(Picture right, CompoundMethod method)
            throws IOException {
        if (right == null)
            throw new PictureMissingException();

        ScreenshotsCompounder compounder
                = new ScreenshotsCompounder(raster, right.raster, method);
        compounder.computeScreenshot();
        Raster result = compounder.getComperation();
        return createPicture(result);
    }
    
    protected abstract Picture createPicture(Raster raster) throws IOException;
}
