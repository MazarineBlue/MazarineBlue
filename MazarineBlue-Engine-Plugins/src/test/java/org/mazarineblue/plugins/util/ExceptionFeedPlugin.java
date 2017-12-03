/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.plugins.util;

import java.io.InputStream;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.plugins.FeedPlugin;

public class ExceptionFeedPlugin
        implements FeedPlugin {

    private final String[] sheets;

    public ExceptionFeedPlugin(String... sheets) {
        this.sheets = sheets;
    }

    @Override
    public boolean canProcess(String mimeType) {
        return true;
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return sheets;
    }

    @Override
    public Feed createFeed(InputStream input, String sheet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
