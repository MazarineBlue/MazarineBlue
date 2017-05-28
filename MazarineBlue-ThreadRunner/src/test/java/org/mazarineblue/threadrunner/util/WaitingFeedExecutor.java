/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.threadrunner.util;

import java.io.File;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.executors.util.DummyFeedExecutor;

public class WaitingFeedExecutor
        extends DummyFeedExecutor {

    private static final int TIME = 15000;

    private int interrupted;

    @Override
    public void execute(File file, String sheet) {
        try {
            while (true)
                Thread.sleep(TIME);
        } catch (InterruptedException ex) {
            ++interrupted;
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void execute(Feed feed) {
        try {
            while (true)
                Thread.sleep(TIME);
        } catch (InterruptedException ex) {
            ++interrupted;
            Thread.currentThread().interrupt();
        }
    }
}
